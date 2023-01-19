package main027.server.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import main027.server.domain.member.entity.Member;
import main027.server.global.auth.Redis.RedisService;
import main027.server.global.auth.dto.LoginDto;
import main027.server.global.auth.jwt.JwtTokenizer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>JWT 인증 필터</p>
 * <p>(1) JwtAuthenticationFilter가 클라이언트의 인증 정보를 수신</p>
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    private final RedisService redisService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer,
                                   RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.redisService = redisService;
    }

    /**
     * (2) 클라이언트로부터 받은 로그인 정보를 가지고 아직 인증되지 않은 토큰인 authenticationToken(UsernamePasswordAuthenticationToken) 객체를 생성한 후에 인증 처리를 위해 authenticationManager에게 전달
     * @param request 클라이언트가 로그인 시에 입력한 이메일과 비밀번호를 가지고 ObjectMapper를 이용하여 loginDto로 변환
     *                -> loginDto를 통해 authenticationToken(UsernamePasswordAuthenticationToken) 생성
     * @return authenticationToken을 authenricationManager에게 전달해 인증 위임 처리
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * <p>인증에 성공할 경우 호출이 된다.</p>
     * (6) authResult(Authentication)에는 인증에 성공한 사용자 정보(Principal, Credential, GrantedAuthorities)가 있음.
     * <p>authResult.getPrincipal()로 Member 객체를 생성한다.</p>
     * <p>Member 객체를 이용해 accessToken과 RefreshToken을 생성한다.</p>
     * <p>응답 헤더에 accessToken과 RefreshToken을 추가한다.</p>
     * <p>[추가설명]</p>
     * <p>AccessToken은 클라이언트 측에서 백엔드 애플리케이션 측에 요청을 보낼때마다 request header에 추가해서 클라이언트 측의 자격을 증명하는게 사용된다.</p>
     * <p>RefreshToken은 AccessToken이 만료될 경우, 클라이언트 측이 AccessToken을 새로 발급받기 위해 클라이언트측에게 추가적으로 제공될 수 있다.</p>
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        Member member = (Member) authResult.getPrincipal();

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        /**
         * 레디스에 key :refreshToken, value email, duration expirationMinutes으로 refreshToken 저장
         */
        redisService.setRefreshToken(refreshToken, member.getEmail(), jwtTokenizer.getRefreshTokenExpirationMinutes());

        response.setHeader("Authorization", "Bearer" + accessToken);
        response.setHeader("Refresh", refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    /**
     * JwtTokenizer에게 AccessToken 생성을 위임
     * @param member
     * @return accessToken
     */
    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("email", member.getEmail());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    /**
     * JwtTokenizer에게 RefreshToken 생성을 위임
     * @param member
     * @return RefreshToken
     */
    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
