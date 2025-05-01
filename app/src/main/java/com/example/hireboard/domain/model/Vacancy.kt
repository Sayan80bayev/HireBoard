package com.example.hireboard.domain.model

data class Vacancy(
    val id: Long,
    val employerId: Long,
    val title: String,
    val description: String,
    val salary: String,
    val experienceRequired: String,
    val skillsRequired: String,
    val location: String,
    val postDate: Long, // timestamp
    val isActive: Boolean
)