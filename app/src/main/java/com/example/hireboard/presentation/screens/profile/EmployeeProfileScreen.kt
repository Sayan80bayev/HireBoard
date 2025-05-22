package com.example.hireboard.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.hireboard.domain.model.User
import com.example.hireboard.presentation.components.HireBoardButton
import com.example.hireboard.presentation.components.HireBoardTextArea
import com.example.hireboard.presentation.components.HireBoardTextField
import com.example.hireboard.presentation.viewmodels.UserProfileState
import com.example.hireboard.presentation.viewmodels.UserProfileViewModel

@Composable
fun EmployeeProfileScreen(
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by userProfileViewModel.userProfile.collectAsState()
    val profileState by userProfileViewModel.profileState.collectAsState()
    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var skills = remember { mutableStateOf(TextFieldValue("")) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Load initial state
    LaunchedEffect(userProfile) {
        if (userProfile is User.Employee) {
            val emp = userProfile as User.Employee
            phone = emp.phone
            education = emp.education
            experience = emp.experience
            skills.value = TextFieldValue(emp.skills)
        }
    }

    // Handle state changes
    LaunchedEffect(profileState) {
        when (profileState) {
            is UserProfileState.ProfileUpdated -> {
                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                userProfileViewModel.resetState()
            }
            is UserProfileState.Error -> {
                Toast.makeText(context, (profileState as UserProfileState.Error).message, Toast.LENGTH_LONG).show()
                userProfileViewModel.resetState()
            }
            else -> {}
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Update") },
            text = { Text("Are you sure you want to update your profile information?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    (userProfile as? User.Employee)?.let {
                        userProfileViewModel.updateEmployeeProfile(
                            it.copy(
                                phone = phone,
                                education = education,
                                experience = experience,
                                skills = skills.value.text
                            )
                        )
                    }
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(dismissOnClickOutside = true)
        )
    }

    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Update Profile",
                style = MaterialTheme.typography.headlineSmall
            )

            HireBoardTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone",
                isRequired = true
            )

            HireBoardTextField(
                value = education,
                onValueChange = { education = it },
                label = "Education",
                isRequired = true
            )

            HireBoardTextField(
                value = experience,
                onValueChange = { experience = it },
                label = "Experience",
                isRequired = true
            )

            HireBoardTextArea(
                value = skills,
                onValueChange = { skills.value = it },
                label = "Skills",
                isRequired = true
            )

            if (profileState is UserProfileState.Loading) {
                CircularProgressIndicator()
            } else {
                HireBoardButton(
                    text = "Save Changes",
                    onClick = { showConfirmDialog = true }
                )
            }
        }
    }
}