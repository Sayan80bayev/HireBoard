package com.example.hireboard.presentation.navigation

object ApplicationRoutes {
    const val ApplicationList = "applications/{vacancyId}"
    fun applicationList(vacancyId: Long) = "applications/$vacancyId"
}