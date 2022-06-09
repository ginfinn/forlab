package com.saprikin.labprojekt.repository

import com.saprikin.labprojekt.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository: JpaRepository<Role,Int> {
    fun findByName(name: String):Role?
}