package com.example.mygym

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.mygym.data.AccountStorage
import com.example.mygym.data.Exercise
import com.example.mygym.data.GymDatabaseHelper
import com.example.mygym.data.exercises
import com.example.mygym.screens.AdminScreen
import com.example.mygym.screens.DayWorkoutScreen
import com.example.mygym.screens.ExerciseDetailScreen
import com.example.mygym.screens.ExerciseListScreen
import com.example.mygym.screens.ForgotPasswordScreen
import com.example.mygym.screens.HomeScreen
import com.example.mygym.screens.LoginScreen
import com.example.mygym.screens.MuscleGroupScreen
import com.example.mygym.screens.ProgressScreen
import com.example.mygym.screens.RegisterScreen
import com.example.mygym.screens.ScheduleScreen
import com.example.mygym.screens.WeeklyReviewScreen
import com.example.mygym.ui.theme.GymBackground
import com.example.mygym.ui.theme.MyGymTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGymTheme {
                GymApp(this)
            }
        }
    }
}

@Composable
fun GymApp(context: Context) {
    val dbHelper = remember { GymDatabaseHelper(context) }

    val currentScreen = remember { mutableStateOf("login") }
    val currentUsername = remember { mutableStateOf("") }
    val selectedMuscle = remember { mutableStateOf("Tất cả") }
    val selectedDay = remember { mutableStateOf("THỨ 2") }
    val selectedExercise = remember { mutableStateOf<Exercise?>(null) }

    // Tải danh sách bài tập mới nhất từ CSDL SQLite
    val dynamicExercises = remember(currentScreen.value) {
        val dbList = dbHelper.getAllExercises()
        if (dbList.isNotEmpty()) {
            dbList.map { entity ->
                Exercise(
                    name = entity.name,
                    muscle = entity.muscle,
                    imageRes = entity.imageRes,
                    description = "${entity.sets} • ${entity.reps}\n\n${entity.description}"
                )
            }
        } else {
            exercises
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
    ) {
        when (currentScreen.value) {
            "login" -> {
                LoginScreen(
                    context = context,
                    onLoginSuccess = { username, role ->
                        currentUsername.value = username
                        currentScreen.value = if (role == "ADMIN") "admin" else "home"
                    },
                    onRegisterClick = { currentScreen.value = "register" },
                    onForgotPasswordClick = { currentScreen.value = "forgot_password" }
                )
            }

            "admin" -> {
                AdminScreen(
                    context = context,
                    onLogout = {
                        AccountStorage.logout(context)
                        currentUsername.value = ""
                        currentScreen.value = "login"
                    }
                )
            }

            "forgot_password" -> {
                ForgotPasswordScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "login" }
                )
            }

            "register" -> {
                RegisterScreen(
                    context = context,
                    onRegisterSuccess = { currentScreen.value = "login" },
                    onBackClick = { currentScreen.value = "login" }
                )
            }

            "home" -> {
                HomeScreen(
                    username = currentUsername.value,
                    onExercisesClick = {
                        selectedMuscle.value = "Tất cả"
                        currentScreen.value = "exercises"
                    },
                    onMuscleClick = { currentScreen.value = "muscles" },
                    onScheduleClick = { currentScreen.value = "schedule" },
                    onProgressClick = { currentScreen.value = "progress" },
                    onLogout = {
                        AccountStorage.logout(context)
                        currentUsername.value = ""
                        currentScreen.value = "login"
                    }
                )
            }

            "schedule" -> {
                ScheduleScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "home" },
                    onDayClick = { dayTitle ->
                        selectedDay.value = dayTitle
                        currentScreen.value = "day_workout"
                    },
                    onProgressClick = { currentScreen.value = "weekly_review" }
                )
            }

            "day_workout" -> {
                DayWorkoutScreen(
                    context = context,
                    dayTitle = selectedDay.value,
                    onBackClick = { currentScreen.value = "schedule" }
                )
            }

            "progress" -> {
                ProgressScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "home" }
                )
            }

            "weekly_review" -> {
                WeeklyReviewScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "schedule" }
                )
            }

            "exercises" -> {
                val filteredList = if (selectedMuscle.value == "Tất cả") {
                    dynamicExercises
                } else {
                    dynamicExercises.filter {
                        it.muscle.contains(selectedMuscle.value, ignoreCase = true) ||
                        selectedMuscle.value.contains(it.muscle, ignoreCase = true)
                    }
                }

                ExerciseListScreen(
                    title = if (selectedMuscle.value == "Tất cả") "Tất cả bài tập" else selectedMuscle.value,
                    exerciseList = filteredList,
                    selectedMuscle = selectedMuscle.value,
                    onMuscleSelected = { muscle -> selectedMuscle.value = muscle },
                    onBackClick = { currentScreen.value = "home" },
                    onExerciseClick = { exercise ->
                        selectedExercise.value = exercise
                        currentScreen.value = "detail"
                    }
                )
            }

            "muscles" -> {
                MuscleGroupScreen(
                    onBackClick = { currentScreen.value = "home" },
                    onMuscleSelected = { muscle ->
                        selectedMuscle.value = muscle
                        currentScreen.value = "muscleExercises"
                    }
                )
            }

            "muscleExercises" -> {
                val filteredExercises = if (selectedMuscle.value == "Tất cả") {
                    dynamicExercises
                } else {
                    dynamicExercises.filter {
                        it.muscle.contains(selectedMuscle.value, ignoreCase = true) ||
                        selectedMuscle.value.contains(it.muscle, ignoreCase = true)
                    }
                }

                ExerciseListScreen(
                    title = selectedMuscle.value,
                    exerciseList = filteredExercises,
                    selectedMuscle = selectedMuscle.value,
                    onMuscleSelected = { muscle -> selectedMuscle.value = muscle },
                    onBackClick = { currentScreen.value = "muscles" },
                    onExerciseClick = { exercise ->
                        selectedExercise.value = exercise
                        currentScreen.value = "detail"
                    }
                )
            }

            "detail" -> {
                selectedExercise.value?.let { exercise ->
                    ExerciseDetailScreen(
                        exercise = exercise,
                        onBackClick = {
                            if (selectedMuscle.value != "Tất cả") {
                                currentScreen.value = "muscleExercises"
                            } else {
                                currentScreen.value = "exercises"
                            }
                        }
                    )
                }
            }
        }
    }
}
