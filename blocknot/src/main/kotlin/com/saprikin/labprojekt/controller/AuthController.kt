package com.saprikin.labprojekt.controller

import com.saprikin.labprojekt.config.jwt.JwtProvider
import com.saprikin.labprojekt.entity.Member
import com.saprikin.labprojekt.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
class AuthController(var userService: UserService,var jwtProvider: JwtProvider) {
    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid registrationRequest: RegistrationRequest): String? {
       //val u : Member? = null
       val u = Member()
        u.memberPassword = registrationRequest.password
        u.memberName = registrationRequest.login
        userService.saveUser(u)
        return "OK"
    }

    @PostMapping("/auth")
    fun auth(@RequestBody request: AuthRequest): AuthResponse? {
        val userEntity: Member? = userService.findByLoginAndPassword(request.login, request.password)
        val token = jwtProvider.generateToken(userEntity?.memberName)
        return AuthResponse(token)
    }
}