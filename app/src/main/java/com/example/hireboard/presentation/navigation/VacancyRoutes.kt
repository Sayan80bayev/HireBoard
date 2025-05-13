package com.example.hireboard.presentation.navigation

object VacancyRoutes {
    const val VacancyList = "vacancy_list"
    const val VacancyCreation = "vacancy_creation"
    const val VacancyDetails = "vacancy_details/{vacancyId}"

    fun vacancyDetails(vacancyId: Long) = "vacancy_details/$vacancyId"
    const val VacancyUpdate = "vacancy_update/{vacancyId}"
    fun vacancyUpdate(vacancyId: Long) = "vacancy_update/$vacancyId"

    const val VacancyDetailsEmployee = "vacancy_details_employee/{vacancyId}"
    fun vacancyDetailsEmployee(vacancyId: Long) = "vacancy_details_employee/$vacancyId"

}