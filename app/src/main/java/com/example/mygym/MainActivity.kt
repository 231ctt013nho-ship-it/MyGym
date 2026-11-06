package com.example.mygym

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.mygym.data.AccountStorage
import com.example.mygym.data.Exercise
import com.example.mygym.data.exercises
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
    val currentScreen = remember { mutableStateOf("login") }
    val currentUsername = remember { mutableStateOf("") }
    val selectedMuscle = remember { mutableStateOf("Tất cả") }
    val selectedDay = remember { mutableStateOf("THỨ 2") }
    val selectedExercise = remember { mutableStateOf<Exercise?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
    ) {
        when (currentScreen.value) {
            // Đăng nhập
            "login" -> {
                LoginScreen(
                    context = context,
                    onLoginSuccess = { username ->
                        currentUsername.value = username
                        currentScreen.value = "home"
                    },
                    onRegisterClick = { currentScreen.value = "register" },
                    onForgotPasswordClick = { currentScreen.value = "forgot_password" }
                )
            }

            // Quên mật khẩu
            "forgot_password" -> {
                ForgotPasswordScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "login" }
                )
            }

            // Đăng ký
            "register" -> {
                RegisterScreen(
                    context = context,
                    onRegisterSuccess = { currentScreen.value = "login" },
                    onBackClick = { currentScreen.value = "login" }
                )
            }

            // Trang chủ
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

            // Lịch tập (Thứ 2 - Chủ Nhật)
            "schedule" -> {
                ScheduleScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "home" },
                    onDayClick = { dayTitle ->
                        selectedDay.value = dayTitle
                        currentScreen.value = "day_workout"
                    },
                    onProgressClick = { currentScreen.value = "progress" }
                )
            }

            // Chi tiết từng ngày (Có checkbox hoàn thành bài tập)
            "day_workout" -> {
                DayWorkoutScreen(
                    dayTitle = selectedDay.value,
                    onBackClick = { currentScreen.value = "schedule" }
                )
            }

            // Tiến độ
            "progress" -> {
                ProgressScreen(
                    context = context,
                    onBackClick = { currentScreen.value = "home" }
                )
            }

            // Danh sách bài tập
            "exercises" -> {
                ExerciseListScreen(
                    title = "Bài tập",
                    exerciseList = exercises,
                    selectedMuscle = selectedMuscle.value,
                    onMuscleSelected = { muscle ->
                        selectedMuscle.value = muscle
                    },
                    onBackClick = { currentScreen.value = "home" },
                    onExerciseClick = { exercise ->
                        selectedExercise.value = exercise
                        currentScreen.value = "detail"
                    }
                )
            }

            // Danh sách nhóm cơ
            "muscles" -> {
                MuscleGroupScreen(
                    onBackClick = { currentScreen.value = "home" },
                    onMuscleSelected = { muscle ->
                        selectedMuscle.value = muscle
                        currentScreen.value = "muscleExercises"
                    }
                )
            }

            // Lọc bài tập theo nhóm cơ đã chọn
            "muscleExercises" -> {
                val filteredExercises = if (selectedMuscle.value == "Tất cả") {
                    exercises
                } else {
                    exercises.filter { it.muscle == selectedMuscle.value }
                }

                ExerciseListScreen(
                    title = selectedMuscle.value,
                    exerciseList = filteredExercises,
                    selectedMuscle = selectedMuscle.value,
                    onMuscleSelected = { muscle ->
                        selectedMuscle.value = muscle
                    },
                    onBackClick = { currentScreen.value = "muscles" },
                    onExerciseClick = { exercise ->
                        selectedExercise.value = exercise
                        currentScreen.value = "detail"
                    }
                )
            }

            // Chi tiết bài tập
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