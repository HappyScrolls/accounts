package com.yedongsoon.accounts

import com.yedongsoon.accounts.JwtTokenUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
@Component
class OAuth2AuthenticationSuccessHandler(
        private val jwtTokenUtil: JwtTokenUtil
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as OAuth2User

        val kakaoAccount = oAuth2User.attributes["kakao_account"] as Map<*, *>
        val email = kakaoAccount["email"] as String
        val properties = oAuth2User.attributes["properties"] as Map<*, *>
        val nickname = properties["nickname"] as String
        val profileImage = properties["profile_image"] as String
        val token = jwtTokenUtil.generateToken("kakao", email, nickname, profileImage)

        val url = makeRedirectUrl(token)

        if (response.isCommitted) {
            logger.debug("응답이 이미 커밋된 상태입니다. $url 로 리다이렉트할 수 없습니다.")
            return
        }
        redirectStrategy.sendRedirect(request, response, url)
    }

    private fun makeRedirectUrl(token: String): String {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/redirect")
                .queryParam("token", token)
                .build()
                .toUriString()
    }
}
