package com.example.hireboard.presentation.navigation

object ApplicationRoutes {
    const val ApplicationList = "applications/{vacancyId}"
    const val ApplicantScreen = "applicant/{employeeId}/{applicationId}"
    fun applicationList(vacancyId: Long) = "applications/$vacancyId"
    fun applicantScreen(employeeId: Long, applicationId: Long ) = "applicant/$employeeId/$applicationId"
}