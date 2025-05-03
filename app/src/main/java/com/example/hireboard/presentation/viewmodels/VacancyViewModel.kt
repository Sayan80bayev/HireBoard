package com.example.hireboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.presentation.screens.vacancy.VacancyCreationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VacancyViewModel(
    private val currentUser: User?,
    private val createVacancyUseCase: CreateVacancyUseCase,
    private val getEmployerVacanciesUseCase: GetEmployerVacanciesUseCase
) : ViewModel() {

    private val _vacancyState = MutableStateFlow<VacancyState>(VacancyState.Idle)
    val vacancyState: StateFlow<VacancyState> = _vacancyState

    private val _vacancies = MutableStateFlow<List<Vacancy>>(emptyList())
    val vacancies: StateFlow<List<Vacancy>> = _vacancies

    init {
        if (currentUser is User.Employer) {
            loadVacancies()
        }
    }

    fun loadVacancies() {
        viewModelScope.launch {
            _vacancyState.value = VacancyState.Loading
            val result = currentUser?.id?.let { getEmployerVacanciesUseCase(it) }
            if (result != null) {
                result.fold(
                    onSuccess = { vacancies ->
                        _vacancies.value = vacancies
                        _vacancyState.value = VacancyState.Success
                    },
                    onFailure = {
                        _vacancyState.value = VacancyState.Error(it.message ?: "Failed to load vacancies")
                    }
                )
            }
        }
    }

    fun createVacancy(vacancy: VacancyCreationState) {
        viewModelScope.launch {
            _vacancyState.value = VacancyState.Loading
            val newVacancy = Vacancy(
                id = 0L,
                title = vacancy.title,
                description = vacancy.description,
                location = vacancy.location,
                salary = vacancy.salary,
                experienceRequired = vacancy.experience,
                skillsRequired = vacancy.skills,
                employerId = (currentUser as? User.Employer)?.id ?: 0L,
                isActive = true,
                postDate = System.currentTimeMillis()
            )

            val result = createVacancyUseCase(newVacancy)
            result.fold(
                onSuccess = {
                    loadVacancies() // Refresh the list
                    _vacancyState.value = VacancyState.VacancyCreated
                },
                onFailure = {
                    _vacancyState.value = VacancyState.Error(it.message ?: "Failed to create vacancy")
                }
            )
        }
    }

    fun resetState() {
        _vacancyState.value = VacancyState.Idle
    }
}

sealed class VacancyState {
    data object Idle : VacancyState()
    data object Loading : VacancyState()
    data object Success : VacancyState()
    data object VacancyCreated : VacancyState()
    data class Error(val message: String) : VacancyState()
}
