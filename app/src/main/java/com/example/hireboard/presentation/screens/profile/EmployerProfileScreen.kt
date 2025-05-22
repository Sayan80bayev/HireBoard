package com.example.hireboard.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun EmployerProfileScreen(
    userProfileViewModel: UserProfileViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by userProfileViewModel.userProfile.collectAsState()
    val profileState by userProfileViewModel.profileState.collectAsState()
    val context = LocalContext.current

    var phone by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var companyDescription = remember { mutableStateOf(TextFieldValue("")) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Load initial state
    LaunchedEffect(userProfile) {
        if (userProfile is User.Employer) {
            val emp = userProfile as User.Employer
            phone = emp.phone
            companyName = emp.companyName
            companyDescription.value = TextFieldValue(emp.companyDescription)
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
                    (userProfile as? User.Employer)?.let {
                        userProfileViewModel.updateEmployerProfile(
                            it.copy(
                                phone = phone,
                                companyName = companyName,
                                companyDescription = companyDescription.value.text
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
                value = companyName,
                onValueChange = { companyName = it },
                label = "Education",
                isRequired = true
            )


            HireBoardTextArea(
                value = companyDescription,
                onValueChange = { companyDescription.value = it },
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