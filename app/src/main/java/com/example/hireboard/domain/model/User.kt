package com.example.hireboard.domain.model

sealed class User(
    open val id: Long,
    open val email: String,
    open val passwordHash: String,
    open val name: String,
    open val phone: String
) {
    data class Employee(
        override val id: Long,
        override val email: String,
        override val passwordHash: String,
        override val name: String,
        override val phone: String,
        val skills: String,
        val experience: String,
        val education: String
    ) : User(id, email, passwordHash, name, phone)

    data class Employer(
        override val id: Long,
        override val email: String,
        override val passwordHash: String,
        override val name: String,
        override val phone: String,
        val companyName: String,
        val companyDescription: String
    ) : User(id, email, passwordHash, name, phone)
}