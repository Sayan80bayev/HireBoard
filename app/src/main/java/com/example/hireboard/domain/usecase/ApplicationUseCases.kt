package com.example.hireboard.domain.usecase

import com.example.hireboard.data.repository.ApplicationRepository
import com.example.hireboard.data.repository.VacancyRepository
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.ApplicationStatus

// ApplicationUseCases.kt
class ApplyForVacancyUseCase(
    private val applicationRepository: ApplicationRepository,
    private val vacancyRepository: VacancyRepository
) {
    suspend operator fun invoke(
        employeeId: Long,
        vacancyId: Long,
        coverLetter: String
    ): Result<Long> {
        return try {
            // Check if vacancy exists and is active
            val vacancy = vacancyRepository.getVacancyById(vacancyId)
            if (vacancy == null || !vacancy.isActive) {
                Result.failure(Exception("Vacancy not available"))
            } else {
                // Check if already applied
                val existingApplications = applicationRepository.getEmployeeApplications(employeeId)
                if (existingApplications.any { it.vacancyId == vacancyId }) {
                    Result.failure(Exception("Already applied to this vacancy"))
                } else {
                    val application = Application(
                        id = 0, // Will be auto-generated
                        vacancyId = vacancyId,
                        employeeId = employeeId,
                        coverLetter = coverLetter,
                        applicationDate = System.currentTimeMillis(),
                        status = ApplicationStatus.PENDING
                    )

                    val applicationId = applicationRepository.applyForVacancy(application)
                    Result.success(applicationId)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetEmployeeApplicationsUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(employeeId: Long): Result<List<Application>> = try {
        val applications = applicationRepository.getEmployeeApplications(employeeId)
        Result.success(applications)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetVacancyApplicationsUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(vacancyId: Long): Result<List<Application>> = try {
        val applications = applicationRepository.getVacancyApplications(vacancyId)
        Result.success(applications)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class UpdateApplicationStatusUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(
        applicationId: Long,
        status: ApplicationStatus
    ): Result<Unit> = try {
        val rowsUpdated = applicationRepository.updateApplicationStatus(applicationId, status)
        if (rowsUpdated > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update application status"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class WithdrawApplicationUseCase(private val applicationRepository: ApplicationRepository) {
    suspend operator fun invoke(applicationId: Long): Result<Unit> = try {
        val rowsDeleted = applicationRepository.withdrawApplication(applicationId)
        if (rowsDeleted > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to withdraw application"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}