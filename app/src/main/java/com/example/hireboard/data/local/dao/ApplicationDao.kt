package com.example.hireboard.data.local.dao

import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.ApplicationStatus

interface ApplicationDao {
    fun insertApplication(application: Application): Long
    fun getApplicationById(id: Long): Application?
    fun getApplicationsByEmployee(employeeId: Long): List<Application>
    fun getApplicationsForVacancy(vacancyId: Long): List<Application>
    fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus): Int
    fun deleteApplication(id: Long): Int
}