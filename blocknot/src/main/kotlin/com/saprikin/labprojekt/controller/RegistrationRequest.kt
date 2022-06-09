package com.saprikin.labprojekt.controller

import javax.validation.constraints.NotEmpty


class RegistrationRequest {
    var login: @NotEmpty String? = null

    var password: @NotEmpty String? = null
}