package com.yedongsoon.accounts

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Collections

@Service
class UserOAuth2Service(
        private val memberRepository: MemberRepository,
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val attributes = oAuth2User.attributes
        attributes.map {
            println(it.key)
            println(it.value)
        }
        val kakaoAccount = attributes["kakao_account"] as Map<*, *>
        val email = kakaoAccount["email"] as String

        val properties = attributes["properties"] as Map<*, *>
        properties.map {
            println(it.key)
            println(it.value)
        }
        val nickname = properties["nickname"] as String
        val picture = properties["profile_image"] as String

        println("!!!!")



//        val memberOptional = memberRepository.findByEmail(email)
//        if (!memberOptional.isPresent) {
//            val newMember = Member.builder()
//                    .email(email)
//                    .nickname(nickname)
//                    .thumbnail(picture)
//                    .point(100)
//                    .build()
//            memberRepository.save(newMember)
//        }

        return DefaultOAuth2User(
                Collections.singleton(SimpleGrantedAuthority("ROLE_MEMBER")),
                attributes,
                "id"
        )
    }
}
