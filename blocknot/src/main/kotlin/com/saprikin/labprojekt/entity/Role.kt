package com.saprikin.labprojekt.entity

import javax.persistence.*
import javax.persistence.JoinColumn





@Entity
@Table(name = "role_table")


class Role(var  name :String?) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val roleId: Int? = null


}