package com.example.hireboard.presentation.screens.vacancy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.hireboard.presentation.components.*
import com.example.hireboard.presentation.viewmodels.VacancyState
import com.example.hireboard.presentation.viewmodels.VacancyViewModel

enum class UpdateStep { BASIC_INFO, REQUIREMENTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyUpdateScreen(
    id: Long,
    viewModel: VacancyViewModel,
    onBackClick: () -> Unit,
    onUpdateClick: (VacancyUpdateState) -> Unit,
    onUpdateSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(id) {
        viewModel.getVacancyDetails(id)
    }

    val selectedVacancy by viewModel.selectedVacancy.collectAsState()

    if (selectedVacancy == null) {
        // Show loading state
        CircularProgressIndicator()
    } else {
        var currentStep by remember { mutableStateOf(UpdateStep.BASIC_INFO) }
        val vacancyState by viewModel.vacancyState.collectAsState()

        println("Selected vacancy: $selectedVacancy")
        val initialState = remember {
            selectedVacancy?.let {
                VacancyUpdateState(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    location = it.location,
                    salary = it.salary,
                    experience = it.experienceRequired,
                    skills = it.skillsRequired
                )
            } ?: VacancyUpdateState()
        }

        val state = remember { mutableStateOf(initialState) }
        val descriptionState = remember { mutableStateOf(TextFieldValue(state.value.description)) }

        LaunchedEffect(vacancyState) {
            if (vacancyState is VacancyState.VacancyCreated) {
                viewModel.clearSelectedVacancy()
                onUpdateSuccess()
            }
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text("Обвновление вакансии (${currentStep.ordinal + 1}/2)") },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (currentStep == UpdateStep.BASIC_INFO) onBackClick()
                            else currentStep = UpdateStep.values()[currentStep.ordinal - 1]
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                        }
                    },
                    actions = {
                        StepIndicator(
                            currentStep = currentStep.ordinal,
                            totalSteps = UpdateStep.values().size,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (currentStep) {
                    UpdateStep.BASIC_INFO -> BasicInfoStep(
                        state = state,
                        descriptionState = descriptionState,
                        onNext = { currentStep = UpdateStep.REQUIREMENTS },
                        modifier = Modifier.fillMaxWidth()
                    )
                    UpdateStep.REQUIREMENTS -> RequirementsStep(
                        state = state,
                        onSubmit = onUpdateClick,
                        onUpdateSuccess =onUpdateSuccess,
                        modifier = Modifier.widthIn(max = 500.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicInfoStep(
    state: MutableState<VacancyUpdateState>,
    descriptionState: MutableState<TextFieldValue>,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Основная информация", style = MaterialTheme.typography.titleMedium)

        HireBoardTextField(
            value = state.value.title,
            onValueChange = { state.value = state.value.copy(title = it) },
            label = "Название вакансии",
            isRequired = true
        )

        HireBoardTextArea(
            value = descriptionState,
            onValueChange = {
                descriptionState.value = it
                state.value = state.value.copy(description = it.text)
            },
            label = "Описание вакансии",
            isRequired = true
        )

        HireBoardTextField(
            value = state.value.location,
            onValueChange = { state.value = state.value.copy(location = it) },
            label = "Местоположение",
            isRequired = true
        )

        Spacer(modifier = Modifier.weight(1f))

        HireBoardButton(
            text = "Далее →",
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RequirementsStep(
    state: MutableState<VacancyUpdateState>,
    onSubmit: (VacancyUpdateState) -> Unit,
    onUpdateSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Требования", style = MaterialTheme.typography.titleMedium)

        HireBoardTextField(
            value = state.value.salary,
            onValueChange = { state.value = state.value.copy(salary = it) },
            label = "Зарплата",
            isRequired = true
        )

        HireBoardTextField(
            value = state.value.experience,
            onValueChange = { state.value = state.value.copy(experience = it) },
            label = "Требуемый опыт",
            isRequired = true
        )

        HireBoardTextField(
            value = state.value.skills,
            onValueChange = { state.value = state.value.copy(skills = it) },
            label = "Необходимые навыки",
            isRequired = true
        )

        Spacer(modifier = Modifier.weight(1f))

        HireBoardButton(
            text = "Обновить вакансию",
            onClick = {
                onSubmit(state.value)
                onUpdateSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun StepIndicator(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (index == currentStep) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

data class VacancyUpdateState(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val salary: String = "",
    val experience: String = "",
    val skills: String = "",
    val location: String = ""
)