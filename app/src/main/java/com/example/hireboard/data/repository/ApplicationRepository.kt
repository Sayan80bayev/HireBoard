package com.example.hireboard.data.repository

import com.example.hireboard.data.local.dao.ApplicationDao
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.ApplicationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplicationRepository(private val applicationDao: ApplicationDao) {
    suspend fun applyForVacancy(application: Application): Long = withContext(Dispatchers.IO) {
        applicationDao.insertApplication(application)
    }

    suspend fun getApplicationById(id: Long): Application? = withContext(Dispatchers.IO) {
        applicationDao.getApplicationById(id)
    }

    suspend fun getEmployeeApplications(employeeId: Long): List<Application> = withContext(
        Dispatchers.IO) {
        applicationDao.getApplicationsByEmployee(employeeId)
    }

    suspend fun getVacancyApplications(vacancyId: Long): List<Application> = withContext(Dispatchers.IO) {
        applicationDao.getApplicationsForVacancy(vacancyId)
    }

    suspend fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus): Int = withContext(
        Dispatchers.IO) {
        applicationDao.updateApplicationStatus(applicationId, status)
    }

    suspend fun withdrawApplication(id: Long): Int = withContext(Dispatchers.IO) {
        applicationDao.deleteApplication(id)
    }
}
