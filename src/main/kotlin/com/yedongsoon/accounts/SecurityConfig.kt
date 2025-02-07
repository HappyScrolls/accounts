package com.yedongsoon.accounts

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
//import org.springframework.security.config.http.SessionCreationPolicy
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.crypto.password.PasswordEncoder
//import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
//
//@Configuration
//@EnableWebSecurity
//class WebSecurityConfig(
//) {
//
//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//
//    @Bean
//    fun corsConfigurationSource(): CorsConfigurationSource {
//        val configuration = CorsConfiguration()
//        configuration.allowedOriginPatterns = listOf("*") // ✅ allowedOrigins 대신 allowedOriginPatterns 사용
////        configuration.allowedOrigins = listOf("http://localhost:3000","http://158.247.197.4:3001","http://158.247.197.4,https://togethery.store","*")
//        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        configuration.allowedHeaders = listOf("Authorization", "Cache-Control", "Content-Type")
//        configuration.allowCredentials = true  // 자격 증명 포함
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//        return source
//    }
//
//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//                .cors { cors ->
//                    cors.configurationSource(corsConfigurationSource())
//                }
//                .csrf { csrf ->
//                    csrf.disable()
//                }
//                .authorizeHttpRequests { auth ->
//                    auth.anyRequest().authenticated()
//                }
//                .sessionManagement { session ->
//                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                }
//
//        return http.build()
//    }
//
//
//
//}

@Configuration
class AppConfig {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}
