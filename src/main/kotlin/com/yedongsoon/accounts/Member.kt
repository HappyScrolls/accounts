package com.yedongsoon.accounts

import jakarta.persistence.*
import java.time.LocalDateTime
@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_no")
    val no: Int = 0,

    @Column(name = "account_id")
    val accountId: String,

    @Column(name = "name")
    val name: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "profile_photo")
    val profilePhoto: String,
) {
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()

    companion object {
        fun create(accountId: String,name: String,email: String,profilePhoto:String) = Member(
                accountId=accountId,
                name = name,
                email = email,
                profilePhoto=profilePhoto,
        )
    }
}