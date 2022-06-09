package com.saprikin.labprojekt.entity

import javax.persistence.*

@Entity
class Member{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0
    var memberName: String? = null
    var memberPassword: String? = null

    @OneToMany(mappedBy = "memberId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var chequeSet: List<Cheque>? = null

    @ManyToOne
    @JoinColumn(name = "role_id")
    var roleEntity: Role? = null

}
