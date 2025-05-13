package com.example.hireboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.hireboard.data.local.dao.impl.UserDaoImpl
import com.example.hireboard.data.local.dao.impl.VacancyDaoImpl
import com.example.hireboard.data.local.db.AppDatabase
import com.example.hireboard.data.repository.UserRepository
import com.example.hireboard.data.repository.VacancyRepository
import com.example.hireboard.domain.usecase.CreateVacancyUseCase
import com.example.hireboard.domain.usecase.DeleteVacancyUseCase
import com.example.hireboard.domain.usecase.GetAllActiveVacanciesUseCase
import com.example.hireboard.domain.usecase.GetEmployerVacanciesUseCase
import com.example.hireboard.domain.usecase.GetVacancyUseCase
import com.example.hireboard.domain.usecase.LoginUseCase
import com.example.hireboard.domain.usecase.RegisterEmployeeUseCase
import com.example.hireboard.domain.usecase.RegisterEmployerUseCase
import com.example.hireboard.domain.usecase.UpdateVacancyUseCase
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


        val loginUseCase = LoginUseCase(userRepository)
        val registerEmployeeUseCase = RegisterEmployeeUseCase(userRepository)
        val registerEmployerUseCase = RegisterEmployerUseCase(userRepository)
        val createVacancyUseCase = CreateVacancyUseCase(vacancyRepository)
        val getEmployerVacanciesUseCase = GetEmployerVacanciesUseCase(vacancyRepository)
        val getVacancyUseCase = GetVacancyUseCase(vacancyRepository)
        val updateVacancyUseCase = UpdateVacancyUseCase(vacancyRepository)
        val deleteVacancyUseCase = DeleteVacancyUseCase(vacancyRepository)
        val getAllActiveVacanciesUseCase = GetAllActiveVacanciesUseCase(vacancyRepository)

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
                        getAllActiveVacanciesUseCase = getAllActiveVacanciesUseCase
                    )
                }
            }
        }
    }
}