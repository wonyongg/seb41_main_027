package main027.server.domain.review.service;

import lombok.RequiredArgsConstructor;
import main027.server.domain.member.verifier.MemberVerifier;
import main027.server.domain.place.verifier.PlaceVerifier;
import main027.server.domain.review.entity.Review;
import main027.server.domain.review.repository.ReviewRepository;
import main027.server.domain.review.verifier.EmojiVerifier;
import main027.server.global.aop.logging.annotation.TimeTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final EmojiVerifier emojiVerifier;
    private final PlaceVerifier placeVerifier;
    private final MemberVerifier memberVerifier;

    /**
     * 리뷰의 Emoji, Place, Member가 유효한지 검사 후 저장
     */
    @TimeTrace
    public Review save(Review review) {
        emojiVerifier.findVerifiedEmoji(review.getEmoji().getEmojiId());
        memberVerifier.findVerifiedMember(review.getMember().getMemberId());
        placeVerifier.findVerifiedPlace(review.getPlace().getPlaceId());

        return reviewRepository.save(review);
    }

    /**
     * 페이징 처리 된 Review를 리턴하는 서비스 메서드
     *
     * @return 페이징 처리 된 Review
     */
    @TimeTrace
    @Override
    public Page<Review> findReviews(Long placeId, Pageable pageable) {
        placeVerifier.findVerifiedPlace(placeId);

        return reviewRepository.findReviews(placeId, pageable);
    }
}
