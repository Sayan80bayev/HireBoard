package com.example.hireboard.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireboard.domain.model.Application
import com.example.hireboard.domain.model.ApplicationStatus
import com.example.hireboard.domain.model.User
import com.example.hireboard.domain.usecase.ApplyForVacancyUseCase
import com.example.hireboard.domain.usecase.GetEmployeeApplicationsUseCase
import com.example.hireboard.domain.usecase.GetVacancyApplicationsUseCase
import com.example.hireboard.domain.usecase.UpdateApplicationStatusUseCase
import com.example.hireboard.domain.usecase.WithdrawApplicationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class ApplicationViewModel(
    private val currentUser: User?,
    private val applyForVacancyUseCase: ApplyForVacancyUseCase,
    private val getEmployeeApplicationsUseCase: GetEmployeeApplicationsUseCase,
    private val getVacancyApplicationsUseCase: GetVacancyApplicationsUseCase,
    private val updateApplicationStatusUseCase: UpdateApplicationStatusUseCase,
    private val withdrawApplicationUseCase: WithdrawApplicationUseCase
) : ViewModel() {

    private val _applicationState = MutableStateFlow<ApplicationState>(ApplicationState.Idle)
    open val applicationState: StateFlow<ApplicationState> = _applicationState

    private val _applications = MutableStateFlow<List<Application>>(emptyList())
    open val applications: StateFlow<List<Application>> = _applications

    init {
        when (currentUser) {
            is User.Employee -> loadEmployeeApplications(currentUser.id)
            is User.Employer -> { /* Employer may load applications per vacancy explicitly */ }
            else -> {}
        }
    }

    fun applyForVacancy(vacancyId: Long, coverLetter: String) {
        val employeeId = (currentUser as? User.Employee)?.id ?: return

        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            applyForVacancyUseCase(employeeId, vacancyId, coverLetter).fold(
                onSuccess = {
                    loadEmployeeApplications(employeeId)
                    _applicationState.value = ApplicationState.ApplicationSubmitted
                },
                onFailure = {
                    _applicationState.value = ApplicationState.Error(it.message ?: "Failed to apply")
                }
            )
        }
    }

    fun loadEmployeeApplications(employeeId: Long) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            getEmployeeApplicationsUseCase(employeeId).fold(
                onSuccess = {
                    _applications.value = it
                    _applicationState.value = ApplicationState.Success
                },
                onFailure = {
                    _applicationState.value = ApplicationState.Error(it.message ?: "Failed to load applications")
                }
            )
        }
    }

    fun loadVacancyApplications(vacancyId: Long) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            getVacancyApplicationsUseCase(vacancyId).fold(
                onSuccess = {
                    _applications.value = it
                    _applicationState.value = ApplicationState.Success
                },
                onFailure = {
                    _applicationState.value = ApplicationState.Error(it.message ?: "Failed to load applications")
                }
            )
        }
    }

    fun updateApplicationStatus(applicationId: Long, status: ApplicationStatus) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            updateApplicationStatusUseCase(applicationId, status).fold(
                onSuccess = {
                    _applicationState.value = ApplicationState.StatusUpdated
                },
                onFailure = {
                    _applicationState.value = ApplicationState.Error(it.message ?: "Failed to update status")
                }
            )
        }
    }

    fun withdrawApplication(applicationId: Long) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading
            withdrawApplicationUseCase(applicationId).fold(
                onSuccess = {
                    _applicationState.value = ApplicationState.ApplicationWithdrawn
                },
                onFailure = {
                    _applicationState.value = ApplicationState.Error(it.message ?: "Failed to withdraw application")
                }
            )
        }
    }

    fun resetState() {
        _applicationState.value = ApplicationState.Idle
    }
}

sealed class ApplicationState {
    data object Idle : ApplicationState()
    data object Loading : ApplicationState()
    data object Success : ApplicationState()
    data object ApplicationSubmitted : ApplicationState()
    data object StatusUpdated : ApplicationState()
    data object ApplicationWithdrawn : ApplicationState()
    data class Error(val message: String) : ApplicationState()
}