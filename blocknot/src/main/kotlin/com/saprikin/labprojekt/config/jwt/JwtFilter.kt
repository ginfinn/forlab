package com.saprikin.labprojekt.config.jwt


import com.saprikin.labprojekt.config.CustomUserDetailsService
import lombok.extern.java.Log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils.hasText
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


@Component
@Log
class JwtFilter() :
    GenericFilterBean() {
    @Autowired
    private val jwtProvider: JwtProvider? = null

    @Autowired
    private val customUserDetailsService: CustomUserDetailsService? = null

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse?, filterChain: FilterChain) {
        logger.info("do filter...")
        val token = getTokenFromRequest(servletRequest as HttpServletRequest)
        if (jwtProvider != null) {
            if (token != null && jwtProvider.validateToken(token)) {
                val userLogin = jwtProvider.getLoginFromToken(token)
                val customUserDetails = customUserDetailsService?.loadUserByUsername(userLogin)
                val auth = UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails!!.authorities)
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearer = request.getHeader(AUTHORIZATION)
        return if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            bearer.substring(7)
        } else null
    }
}