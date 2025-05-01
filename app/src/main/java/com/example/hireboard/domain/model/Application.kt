package com.example.hireboard.domain.model

data class Application(
    val id: Long,
    val vacancyId: Long,
    val employeeId: Long,
    val coverLetter: String,
    val applicationDate: Long, // timestamp
    val status: ApplicationStatus
)

enum class ApplicationStatus {
    PENDING, REVIEWED, ACCEPTED, REJECTED
}
