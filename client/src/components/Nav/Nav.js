import { Link } from 'react-router-dom'
import styled from 'styled-components'

import Logo from '../Nav/images/Logo.svg'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMap, faComment, faBug, faUser } from '@fortawesome/free-solid-svg-icons'
import { faPagelines } from '@fortawesome/free-brands-svg-icons'

// li사이즈만 빼둠 Nav css 셋업 안된 상태 -> 추후 Acitve 스타일링 필요
// <a href="/" className={type.page === "home" ? "selected" : ""}>

// const StyleFontAwesomeIcon = styled(FontAwesomeIcon)`
//   font-size: 28px;
//   color: #fff;
//   :hover {
//     color: #13c57c;
//   }
// `
const Wrapper = styled.nav`
  z-index: 1000;
  display: flex;
  align-items: flex-start;
  justify-content: center;
  width: 88px !important;
  height: 100%;

  .logo {
    cursor: pointer;
    position: relative;
    left: 5px;
    top: 5px;
    width: 57px;
    height: 61px;
    margin-bottom: 80px;
  }
  ul {
    padding: 16px;
    li:not(:first-child) {
      cursor: pointer;
      margin-bottom: 16px;
      a {
        width: 68px;
        height: 75px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: rgba(255, 255, 255, 1);
        /* color: rgba(255, 255, 255, 0.7); */
        font-size: 14px;
        line-height: 140%;
        font-weight: 600;
        border-radius: 16px;
        span {
          margin-top: 8px;
          letter-spacing: -0.5px;
        }
      }
      a:hover:not(.logo) {
        color: #13c57c;
        font-weight: 700;
        background-color: rgba(255, 255, 255, 0.99);
        box-shadow: -8px -4px 30px rgba(0, 129, 76, 0.4);
      }
    }
  }
`

const Nav = () => {
  return (
    <Wrapper>
      <ul>
        <li className="logo">
          <Link to="/" className="logo">
            <img src={Logo} alt="EcoGreenSeoul Logo" />
          </Link>
        </li>
        <li>
          <Link to="/">
            <FontAwesomeIcon icon={faMap} size="2x" />
            <span>친환경지도</span>
          </Link>
        </li>
        <li>
          <Link to="/mypage">
            <FontAwesomeIcon icon={faUser} size="2x" />
            <span>마이페이지</span>
          </Link>
        </li>
        <li>
          <Link to="/Aboutus">
            <FontAwesomeIcon icon={faPagelines} size="2x" />
            <span>서비스소개</span>
          </Link>
        </li>
        <li>
          <a href="https://open.kakao.com/o/g8FLpt1e">
            <FontAwesomeIcon icon={faComment} size="2x" />
            <span>오픈채팅</span>
          </a>
        </li>
        <li>
          <Link to="">
            <FontAwesomeIcon icon={faBug} size="2x" />
            <span>오류제보</span>
          </Link>
        </li>
      </ul>
    </Wrapper>
  )
}

export default Nav
