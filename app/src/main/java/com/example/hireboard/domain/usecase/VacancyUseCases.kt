package com.example.hireboard.domain.usecase

import com.example.hireboard.data.repository.VacancyRepository
import com.example.hireboard.domain.model.Vacancy

class CreateVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(vacancy: Vacancy): Result<Long> = try {
        val vacancyId = vacancyRepository.createVacancy(vacancy)
        Result.success(vacancyId)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(vacancyId: Long): Result<Vacancy> = try {
        val vacancy = vacancyRepository.getVacancyById(vacancyId)
        if (vacancy != null) {
            Result.success(vacancy)
        } else {
            Result.failure(Exception("Vacancy not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetEmployerVacanciesUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(employerId: Long): Result<List<Vacancy>> = try {
        val vacancies = vacancyRepository.getEmployerVacancies(employerId)
        Result.success(vacancies)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class GetAllActiveVacanciesUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(): Result<List<Vacancy>> = try {
        val vacancies = vacancyRepository.getAllActiveVacancies()
        Result.success(vacancies)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class SearchVacanciesUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(query: String): Result<List<Vacancy>> = try {
        val vacancies = vacancyRepository.searchVacancies(query)
        Result.success(vacancies)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class UpdateVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(vacancy: Vacancy): Result<Unit> = try {
        val rowsUpdated = vacancyRepository.updateVacancy(vacancy)
        if (rowsUpdated > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to update vacancy"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

class DeleteVacancyUseCase(private val vacancyRepository: VacancyRepository) {
    suspend operator fun invoke(vacancyId: Long): Result<Unit> = try {
        val rowsDeleted = vacancyRepository.deleteVacancy(vacancyId)
        if (rowsDeleted > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Failed to delete vacancy"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}