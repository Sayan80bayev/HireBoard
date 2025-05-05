package com.example.hireboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.model.Vacancy
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
import com.example.hireboard.presentation.screens.vacancy.VacancyCreationState
import com.example.hireboard.presentation.screens.vacancy.VacancyUpdateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VacancyViewModel(
    private val currentUser: User?,
    private val createVacancyUseCase: CreateVacancyUseCase,
    private val getEmployerVacanciesUseCase: GetEmployerVacanciesUseCase,
    private val getVacancyUseCase: GetVacancyUseCase,
    private val updateVacancyUseCase: UpdateVacancyUseCase,
    private val deleteVacancyUseCase: DeleteVacancyUseCase
) : ViewModel() {

    private val _selectedVacancy = MutableStateFlow<Vacancy?>(null)
    val selectedVacancy: StateFlow<Vacancy?> = _selectedVacancy

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

    fun getVacancyDetails(id: Long) {
        viewModelScope.launch {
            _vacancyState.value = VacancyState.Loading
            getVacancyUseCase(id).fold(
                onSuccess = { vacancy ->
                    _selectedVacancy.value = vacancy
                    _vacancyState.value = VacancyState.Success
                },
                onFailure = { error ->
                    _vacancyState.value = VacancyState.Error(error.message ?: "Failed to load vacancy details")
                }
            )
        }
    }

    fun updateVacancy(state: VacancyUpdateState) {
        viewModelScope.launch {
            _vacancyState.value = VacancyState.Loading
            val vacancy = Vacancy(
                id = state.id,
                title = state.title,
                description = state.description,
                location = state.location,
                salary = state.salary,
                experienceRequired = state.experience,
                skillsRequired = state.skills,
                employerId = (currentUser as? User.Employer)?.id ?: 0L,
                isActive = true,
                postDate = System.currentTimeMillis()
            )

            _vacancyState.value = VacancyState.Loading
            updateVacancyUseCase(vacancy).fold(
                onSuccess = {
                    _selectedVacancy.value = vacancy
                    _vacancyState.value = VacancyState.VacancyUpdated
                },
                onFailure = { error ->
                    _vacancyState.value = VacancyState.Error(error.message ?: "Failed to update vacancy")
                }
            )
        }
    }

    fun deleteVacancy(vacancyId: Long) {
        viewModelScope.launch {
            _vacancyState.value = VacancyState.Loading
            deleteVacancyUseCase(vacancyId).fold(
                onSuccess = {
                    _vacancyState.value = VacancyState.VacancyDeleted
                },
                onFailure = { error ->
                    _vacancyState.value = VacancyState.Error(error.message ?: "Failed to delete vacancy")
                }
            )
        }
    }

    fun resetState() {
        _vacancyState.value = VacancyState.Idle
    }

    fun setSelectedVacancy(vacancy: Vacancy) {
        _selectedVacancy.value = vacancy
    }

    fun getSelectedVacancy(): Vacancy? {
        return _selectedVacancy.value
    }

    fun clearSelectedVacancy() {
        _selectedVacancy.value = null
    }
}

sealed class VacancyState {
    data object Idle : VacancyState()
    data object Loading : VacancyState()
    data object Success : VacancyState()
    data object VacancyCreated : VacancyState()
    data object VacancyUpdated : VacancyState()
    data object VacancyDeleted : VacancyState()
    data class Error(val message: String) : VacancyState()
}
