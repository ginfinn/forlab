package com.saprikin.labprojekt.config

import com.saprikin.labprojekt.entity.Member
import com.saprikin.labprojekt.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component


@Component
class CustomUserDetailsService() : UserDetailsService {
    @Autowired
    private val userService: UserService? = null
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): CustomUserDetails? {
        val userEntity: Member? = userService?.findByLogin(username)
        return userEntity?.let { CustomUserDetails.fromUserEntityToCustomUserDetails(it) }
    }
}