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

enum class CreationStep { BASIC_INFO, REQUIREMENTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyCreationScreen(
    viewModel: VacancyViewModel,
    onBackClick: () -> Unit,
    onCreateClick: (VacancyCreationState) -> Unit,
    onCreationSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableStateOf(CreationStep.BASIC_INFO) }
    val state = remember { mutableStateOf(VacancyCreationState()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue(state.value.description)) }
    val vacancyState by viewModel.vacancyState.collectAsState()

    LaunchedEffect(vacancyState) {
        if (vacancyState is VacancyState.VacancyCreated) {
            onCreationSuccess()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Создание вакансии (${currentStep.ordinal + 1}/2)") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (currentStep == CreationStep.BASIC_INFO) onBackClick()
                        else currentStep = CreationStep.values()[currentStep.ordinal - 1]
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    StepIndicator(
                        currentStep = currentStep.ordinal,
                        totalSteps = CreationStep.values().size,
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
                CreationStep.BASIC_INFO -> BasicInfoStep(
                    state = state,
                    descriptionState = descriptionState,
                    onNext = { currentStep = CreationStep.REQUIREMENTS },
                    modifier = Modifier.fillMaxWidth()
                )
                CreationStep.REQUIREMENTS -> RequirementsStep(
                    state = state,
                    onSubmit = onCreateClick,
                    onCreationSuccess = onCreationSuccess,
                    modifier = Modifier.widthIn(max = 500.dp)
                )
            }
        }
    }

}

@Composable
private fun BasicInfoStep(
    state: MutableState<VacancyCreationState>,
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
    state: MutableState<VacancyCreationState>,
    onSubmit: (VacancyCreationState) -> Unit,
    onCreationSuccess: () -> Unit,
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
            text = "Создать вакансию",
            onClick = {
                onSubmit(state.value)
                onCreationSuccess()
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

data class VacancyCreationState(
    val title: String = "",
    val description: String = "",
    val salary: String = "",
    val experience: String = "",
    val skills: String = "",
    val location: String = ""
)
