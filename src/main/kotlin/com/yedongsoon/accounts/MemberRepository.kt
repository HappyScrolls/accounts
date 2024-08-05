package com.yedongsoon.accounts

import org.springframework.data.jpa.repository.JpaRepository


interface MemberRepository : JpaRepository<Member, Int>