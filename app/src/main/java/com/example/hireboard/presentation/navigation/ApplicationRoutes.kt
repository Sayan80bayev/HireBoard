package com.example.hireboard.presentation.navigation

object ApplicationRoutes {
    const val ApplicationList = "applications/{vacancyId}"
    const val ApplicantScreen = "applicant/{employeeId}/{applicationId}"
    const val ApplicationEmployeeList = "employee/applications/{employeeId}"
    fun applicationEmployeeList(employeeId: Long) = "employee/applications/$employeeId"
    fun applicationList(vacancyId: Long) = "applications/$vacancyId"
    fun applicantScreen(employeeId: Long, applicationId: Long ) = "applicant/$employeeId/$applicationId"
}