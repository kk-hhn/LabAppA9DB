package com.example.labappa9db.database

data class todo (
    val id: Int = 0,
    val name: String,
    val priority: Int,
    val deadline: String,
    val description: String,
    var status: Int = 0
)
