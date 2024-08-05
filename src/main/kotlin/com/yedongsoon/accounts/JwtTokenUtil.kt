package com.yedongsoon.accounts

import io.jsonwebtoken.*
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtTokenUtil {

    private var secretKey: String = "tokensecreteyasbsdasdasdyjgiuhgiuhiasd"

    @PostConstruct
    protected fun init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.toByteArray())
    }

    fun generateToken(platform: String, uid: String, nickname: String, thumbnail: String): String {
        val tokenPeriod = 1000L * 60L * 10L
        val refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L

        val claims = Jwts.claims().setSubject(uid).apply {
            this["platform"] = platform
            this["nickname"] = nickname
        }

        val now = Date()

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("test")
                .setIssuedAt(now)
                .setExpiration(Date(System.currentTimeMillis() + 60 * 10000000 * 60))
                .claim("nickname", nickname)
                .claim("uid", uid)
                .claim("thumbnail", thumbnail)
                .claim("platform", platform)
                .setSubject(uid)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
    }

    fun verifyToken(token: String): Boolean {
        println("verify")
        return try {
            val claims = Jwts.parser()
                    .setSigningKey(secretKey.toByteArray())
                    .parseClaimsJws(token)

            claims.body
                    .expiration
                    .after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getUid(token: String): String {
        return Jwts.parser()
                .setSigningKey(secretKey.toByteArray())
                .parseClaimsJws(token)
                .body
                .subject
    }
}
