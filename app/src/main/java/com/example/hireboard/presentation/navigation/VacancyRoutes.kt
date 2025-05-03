package com.example.hireboard.presentation.navigation

object VacancyRoutes {
    const val VacancyList = "vacancy_list"
    const val VacancyCreation = "vacancy_creation"
    const val VacancyDetails = "vacancy_details/{vacancyId}"

    fun vacancyDetails(vacancyId: Long) = "vacancy_details/$vacancyId"
}