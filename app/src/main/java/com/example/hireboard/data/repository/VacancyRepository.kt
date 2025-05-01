package com.example.hireboard.data.repository

import com.example.hireboard.data.local.dao.VacancyDao
import com.example.hireboard.domain.model.Vacancy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VacancyRepository(private val vacancyDao: VacancyDao) {
    suspend fun createVacancy(vacancy: Vacancy): Long = withContext(Dispatchers.IO) {
        vacancyDao.insertVacancy(vacancy)
    }

    suspend fun getVacancyById(id: Long): Vacancy? = withContext(Dispatchers.IO) {
        vacancyDao.getVacancyById(id)
    }

    suspend fun getEmployerVacancies(employerId: Long): List<Vacancy> = withContext(Dispatchers.IO) {
        vacancyDao.getVacanciesByEmployer(employerId)
    }

    suspend fun getAllActiveVacancies(): List<Vacancy> = withContext(Dispatchers.IO) {
        vacancyDao.getAllActiveVacancies()
    }

    suspend fun searchVacancies(query: String): List<Vacancy> = withContext(Dispatchers.IO) {
        vacancyDao.searchVacancies(query)
    }

    suspend fun updateVacancy(vacancy: Vacancy): Int = withContext(Dispatchers.IO) {
        vacancyDao.updateVacancy(vacancy)
    }

    suspend fun deleteVacancy(id: Long): Int = withContext(Dispatchers.IO) {
        vacancyDao.deleteVacancy(id)
    }
}
