package com.yedongsoon.accounts

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

data class KakaoTokenResponse(
    @JsonProperty("token_type")
    val tokenType: String, // 토큰 타입 (bearer 고정)

    @JsonProperty("access_token")
    val accessToken: String, // 액세스 토큰 값

    @JsonProperty("id_token")
    val idToken: String?, // ID 토큰 (선택 사항, OpenID Connect가 활성화된 경우 제공)

    @JsonProperty("expires_in")
    val expiresIn: Int, // 액세스 토큰 만료 시간 (초)

    @JsonProperty("refresh_token")
    val refreshToken: String, // 리프레시 토큰 값

    @JsonProperty("refresh_token_expires_in")
    val refreshTokenExpiresIn: Int, // 리프레시 토큰 만료 시간 (초)

    @JsonProperty("scope")
    val scope: String? // 인증된 사용자 정보 조회 권한 범위 (선택 사항)
)
data class KakaoUserResponse(
    val id: Long,
    val connected_at: String?,
    val properties: Properties?,
    val kakao_account: KakaoAccount?
) {
    data class Properties(
        val nickname: String?,
        val profile_image: String?,
        val thumbnail_image: String?
    )

    data class KakaoAccount(
        val profile: Profile?,
        val email: String?,
        val name: String?,
        val birthday: String?,
        val gender: String?,
        val age_range: String?
    ) {
        data class Profile(
            val nickname: String?,
            val profile_image_url: String?,
            val thumbnail_image_url: String?
        )
    }
}

@RestController
@RequestMapping("/auth")
class KakaoController(
    private val restTemplate: RestTemplate,
    private val jwtTokenUtil: JwtTokenUtil,
    private val memberRepository: MemberRepository
) {
    private val kakaoUserUrl = "https://kapi.kakao.com/v2/user/me"
    @GetMapping("/callback")
    fun kakaoLogin(@RequestParam code: String): ResponseEntity<Any> {
        val tokenResponse = getKakaoToken(code)
        val token = getKakaoUserInfo(tokenResponse.accessToken)?.let{createJwtToken(it)}?:""

        val redirectUrl = UriComponentsBuilder.fromUriString("https://togethery.store/redirect")
            .queryParam("token", token)
            .build()
            .toUriString()

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", redirectUrl).build()
    }

    private fun getKakaoToken(code: String): KakaoTokenResponse {
        val params = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", "YOUR_KAKAO_REST_API_KEY")
            add("redirect_uri", "https://api.togethery.store/auth/callback")
            add("code", code)
            add("client_secret", "sGODStcWmvZh4hFxDoWcpyfe9ilmbQ5J")
        }

        val request = HttpEntity(params, HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        })

        return restTemplate.postForObject("https://kauth.kakao.com/oauth/token", request, KakaoTokenResponse::class.java)!!
    }


    private fun createJwtToken(userInfo: KakaoUserResponse): String {
        println("userInfo = ${userInfo}")
        val email = userInfo.kakao_account?.email?:""
        val nickname = userInfo.properties?.nickname?:""
        val profileImage = userInfo.properties?.profile_image?:""

        val memberNo = memberRepository.findByEmail(email)?.no ?: memberRepository.save(
            Member.create(accountId = "account", name = nickname, email = email, profilePhoto = profileImage)
        ).no

        return jwtTokenUtil.generateToken("kakao", email, nickname, profileImage, memberNo)
    }
    fun getKakaoUserInfo(accessToken: String): KakaoUserResponse? {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.set("Authorization", "Bearer $accessToken")

        val request = HttpEntity<String>(headers)
        val response = restTemplate.exchange(
            kakaoUserUrl, HttpMethod.GET, request, KakaoUserResponse::class.java
        )

        return response.body
    }
}
