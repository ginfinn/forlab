package com.saprikin.labprojekt.service

import com.saprikin.labprojekt.entity.Member
import com.saprikin.labprojekt.entity.Role
import com.saprikin.labprojekt.repository.MemberRepository
import com.saprikin.labprojekt.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService() {
    @Autowired
    private val memberRepository:MemberRepository? = null

    @Autowired
    private val roleRepository:RoleRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null
    fun saveUser(userEntity: Member): Member? {
        val userRole: Role? = roleRepository?.findByName("ROLE_USER")
        userEntity.roleEntity = userRole
        if (passwordEncoder != null) {
            userEntity.memberPassword = passwordEncoder.encode(userEntity.memberPassword)
        }
        return memberRepository!!.save(userEntity)
    }
    fun findByLogin(login: String?): Member? {
        return memberRepository!!.findByMemberName(login)
    }
    fun findByLoginAndPassword(login: String?,password:String?) : Member? {
        var userEntity : Member? =findByLogin(login)
        if(userEntity!= null){
          if(  passwordEncoder!!.matches(password,userEntity.memberPassword)){
              return userEntity
          }
        }
        return null
    }
}