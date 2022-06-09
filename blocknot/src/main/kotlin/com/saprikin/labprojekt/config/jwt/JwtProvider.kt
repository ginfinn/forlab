package com.saprikin.labprojekt.config.jwt

import lombok.extern.java.Log
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.hibernate.annotations.common.util.impl.LoggerFactory
import java.lang.Exception

import java.time.ZoneId

import java.time.LocalDate
import java.util.*
import io.jsonwebtoken.Claims





@Component
@Log
class JwtProvider {
    @Value("$(jwt.secret)")
     val jwtSecret: String? = null
    fun generateToken(login: String?): String? {
        val date: Date = Date.from(LocalDate.now().plusDays(15).atStartOfDay(ZoneId.systemDefault()).toInstant())
        return Jwts.builder()
            .setSubject(login)
            .setExpiration(date)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }
    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            LoggerFactory.make("invalid token")

        }
        return false
    }
    fun getLoginFromToken(token: String?): String? {
        val claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
        return claims.subject
    }
}