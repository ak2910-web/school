package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.InputScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.StudentListScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.StudentViewModel

// Named Navigation Routes matching user demand mapping precisely
const val ROUTE_LOGIN = "/"
const val ROUTE_DASHBOARD = "/dashboard"
const val ROUTE_STUDENTS = "/students"
const val ROUTE_PROFILE = "/profile"
const val ROUTE_INPUT = "/input"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val vm: StudentViewModel = viewModel()

                // State collection from StateFlows
                val rawStudents by vm.students.collectAsState()
                val selectedStudentId by vm.selectedStudentId.collectAsState()
                val searchResults by vm.filteredStudents.collectAsState(initial = emptyList())
                val searchQuery by vm.searchQuery.collectAsState()
                val isSchoolSide by vm.isSchoolSide.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_LOGIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Sign In / Authentication Route
                        composable(ROUTE_LOGIN) {
                            LoginScreen(
                                students = rawStudents,
                                onSchoolLogin = { email, password ->
                                    if (vm.loginAsSchool(email, password)) {
                                        navController.navigate(ROUTE_DASHBOARD) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                },
                                onStudentLogin = { studentId ->
                                    if (vm.loginAsStudent(studentId)) {
                                        navController.navigate(ROUTE_PROFILE) {
                                            popUpTo(ROUTE_LOGIN) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        // 2. Dashboard Insight Hub Route
                        composable(ROUTE_DASHBOARD) {
                            DashboardScreen(
                                totalStudents = vm.getTotalStudentsCount(),
                                averageScore = vm.getAverageAcademicScore(),
                                topSkill = vm.getTopSkillCategory(),
                                weakArea = vm.getWeakAreaCategory(),
                                onViewStudentsDirectory = {
                                    navController.navigate(ROUTE_STUDENTS)
                                },
                                onLogoutClick = {
                                    vm.logout()
                                    navController.navigate(ROUTE_LOGIN) {
                                        popUpTo(ROUTE_DASHBOARD) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 3. Searchable Student Directory Listing Route
                        composable(ROUTE_STUDENTS) {
                            StudentListScreen(
                                students = searchResults,
                                searchQuery = searchQuery,
                                onSearchQueryChanged = { vm.updateSearchQuery(it) },
                                onStudentClick = { student ->
                                    vm.selectStudent(student.id)
                                    navController.navigate(ROUTE_PROFILE)
                                },
                                onBackToDashboard = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // 4. Detailed Academic & Multi-skill Profile Card Route
                        composable(ROUTE_PROFILE) {
                            val activeStudent = rawStudents.firstOrNull { it.id == selectedStudentId }
                            StudentProfileScreen(
                                student = activeStudent,
                                isSchoolSide = isSchoolSide,
                                onAddEvaluationClick = {
                                    navController.navigate(ROUTE_INPUT)
                                },
                                onBackClick = {
                                    if (isSchoolSide) {
                                        navController.popBackStack()
                                    } else {
                                        vm.logout()
                                        navController.navigate(ROUTE_LOGIN) {
                                            popUpTo(ROUTE_PROFILE) { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }

                        // 5. Input Evaluations & Scoring Forms Route
                        composable(ROUTE_INPUT) {
                            val activeStudent = rawStudents.firstOrNull { it.id == selectedStudentId }
                            InputScreen(
                                student = activeStudent,
                                onSaveClick = { math, science, english, history, art, creativity, leadership, communication, discipline, activity, remarks ->
                                    if (activeStudent != null) {
                                        vm.updateEvaluation(
                                            studentId = activeStudent.id,
                                            math = math,
                                            science = science,
                                            english = english,
                                            history = history,
                                            art = art,
                                            creativity = creativity,
                                            leadership = leadership,
                                            communication = communication,
                                            discipline = discipline,
                                            activity = activity,
                                            remarks = remarks
                                        )
                                    }
                                    // Successfully saved -> trigger navigational pop to instantly refresh active profile details
                                    navController.popBackStack()
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
