package com.example.esop.OrgnazationDropdown

data class RoleItem(
    val org_id: String?,
    val login_id: String?,
    val name_of_the_org: String?
)

typealias RoleResponse = List<RoleItem>