package com.saprikin.labprojekt.config

import com.saprikin.labprojekt.entity.Member
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.GrantedAuthority

import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*


class CustomUserDetails: UserDetails {
    private var login: String? = null
    private var password: String? = null
    private var grantedAuthorities: Collection<GrantedAuthority?>? = null
    companion object {
        fun fromUserEntityToCustomUserDetails(userEntity: Member): CustomUserDetails {
            val c = CustomUserDetails()
            c.login = userEntity.memberName
            c.password = userEntity.memberPassword
            c.grantedAuthorities = Collections.singletonList(SimpleGrantedAuthority(userEntity.roleEntity?.name))
            return c
        }
    }
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return grantedAuthorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}