package com.example.hireboard.data.local.dao

import com.example.hireboard.domain.model.Vacancy

interface VacancyDao {
    fun insertVacancy(vacancy: Vacancy): Long
    fun getVacancyById(id: Long): Vacancy?
    fun getVacanciesByEmployer(employerId: Long): List<Vacancy>
    fun getAllActiveVacancies(): List<Vacancy>
    fun updateVacancy(vacancy: Vacancy): Int
    fun deleteVacancy(id: Long): Int
    fun searchVacancies(query: String): List<Vacancy>
}