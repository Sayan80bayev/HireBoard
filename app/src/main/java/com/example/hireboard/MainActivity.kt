package com.example.hireboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.hireboard.data.local.dao.impl.ApplicationDaoImpl
import com.example.hireboard.data.local.dao.impl.UserDaoImpl
import com.example.hireboard.data.local.dao.impl.VacancyDaoImpl
import com.example.hireboard.data.local.db.AppDatabase
import com.example.hireboard.data.repository.ApplicationRepository
import com.example.hireboard.data.repository.UserRepository
import com.example.hireboard.data.repository.VacancyRepository
import com.example.hireboard.domain.usecase.ApplyForVacancyUseCase
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetAllActiveVacanciesUseCase
import com.example.hireboard.domain.usecase.GetEmployeeApplicationsUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetUserUseCase
import com.example.hireboard.domain.usecase.GetVacancyApplicationsUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.domain.usecase.UpdateApplicationStatusUseCase
import com.example.hireboard.domain.usecase.UpdateEmployeeProfileUseCase
import com.example.hireboard.domain.usecase.UpdateEmployerProfileUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
import com.example.hireboard.domain.usecase.WithdrawApplicationUseCase
import com.example.hireboard.presentation.navigation.AppNavHost
import com.example.hireboard.ui.theme.HireBoardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase(context = this)
        val userDao = UserDaoImpl(db)
        val userRepository = UserRepository(userDao)
        val vacancyDao = VacancyDaoImpl(db)
        val vacancyRepository = VacancyRepository(vacancyDao)
        val applicationDao = ApplicationDaoImpl(db)
        val applicationRepository = ApplicationRepository(applicationDao)
        val userProfileDao = UserDaoImpl(db)
        val userProfileRepository = UserRepository(userProfileDao)


        val loginUseCase = LoginUseCase(userRepository)
        val registerEmployeeUseCase = RegisterEmployeeUseCase(userRepository)
        val registerEmployerUseCase = RegisterEmployerUseCase(userRepository)
        val createVacancyUseCase = CreateVacancyUseCase(vacancyRepository)
        val getEmployerVacanciesUseCase = GetEmployerVacanciesUseCase(vacancyRepository)
        val getVacancyUseCase = GetVacancyUseCase(vacancyRepository)
        val updateVacancyUseCase = UpdateVacancyUseCase(vacancyRepository)
        val deleteVacancyUseCase = DeleteVacancyUseCase(vacancyRepository)
        val getAllActiveVacanciesUseCase = GetAllActiveVacanciesUseCase(vacancyRepository)
        val applyForVacancyUseCase = ApplyForVacancyUseCase(applicationRepository, vacancyRepository)
        val getEmployeeApplicationsUseCase = GetEmployeeApplicationsUseCase(applicationRepository)
        val getVacancyApplicationsUseCase = GetVacancyApplicationsUseCase(applicationRepository)
        val updateApplicationStatusUseCase = UpdateApplicationStatusUseCase(applicationRepository)
        val withdrawApplicationUseCase = WithdrawApplicationUseCase(applicationRepository)
        val getUserUseCase = GetUserUseCase(userRepository)
        val updateEmployeeProfileUseCase = UpdateEmployeeProfileUseCase(userProfileRepository)
        val updateEmployerProfileUseCase = UpdateEmployerProfileUseCase(userProfileRepository)
        setContent {
            HireBoardTheme {
                val navController = rememberNavController()
                Surface {
                    AppNavHost(
                        navController = navController,
                        loginUseCase = loginUseCase,
                        registerEmployeeUseCase = registerEmployeeUseCase,
                        registerEmployerUseCase = registerEmployerUseCase,
                        createVacancyUseCase = createVacancyUseCase,
                        getEmployerVacanciesUseCase = getEmployerVacanciesUseCase,
                        getVacancyUseCase = getVacancyUseCase,
                        updateVacancyUseCase = updateVacancyUseCase,
                        deleteVacancyUseCase = deleteVacancyUseCase,
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase,
                        applyForVacancyUseCase = applyForVacancyUseCase,
                        getEmployeeApplicationsUseCase = getEmployeeApplicationsUseCase,
                        getVacancyApplicationsUseCase = getVacancyApplicationsUseCase,
                        updateApplicationStatusUseCase = updateApplicationStatusUseCase,
                        withdrawApplicationUseCase = withdrawApplicationUseCase,
                        getUserUseCase = getUserUseCase,
                        updateEmployeeProfileUseCase = updateEmployeeProfileUseCase,
                        updateEmployerProfileUseCase = updateEmployerProfileUseCase
                    )
                }
            }
        }
    }
}