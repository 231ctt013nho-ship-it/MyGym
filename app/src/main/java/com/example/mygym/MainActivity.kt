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

import com.example.mygym.screens.ExerciseDetailScreen
import com.example.mygym.screens.ExerciseListScreen
import com.example.mygym.screens.HomeScreen
import com.example.mygym.screens.LoginScreen
import com.example.mygym.screens.MuscleGroupScreen
import com.example.mygym.screens.ProgressScreen
import com.example.mygym.screens.RegisterScreen
import com.example.mygym.screens.ScheduleScreen

import com.example.mygym.ui.theme.GymBackground
import com.example.mygym.ui.theme.MyGymTheme


// =====================================================
// MAIN ACTIVITY
// =====================================================

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


// =====================================================
// MY GYM APP
// =====================================================

@Composable
fun GymApp(context: Context) {

    // Màn hình hiện tại
    val currentScreen =
        remember {
            mutableStateOf("login")
        }

    // Tên tài khoản đang đăng nhập
    val currentUsername =
        remember {
            mutableStateOf("")
        }

    // Nhóm cơ đang được chọn
    val selectedMuscle =
        remember {
            mutableStateOf("Tất cả")
        }

    // Bài tập đang được chọn
    val selectedExercise =
        remember {
            mutableStateOf<Exercise?>(null)
        }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
    ) {

        when (currentScreen.value) {

            // =================================================
            // LOGIN
            // =================================================

            "login" -> {

                LoginScreen(
                    context = context,

                    onLoginSuccess = { username ->

                        currentUsername.value =
                            username

                        currentScreen.value =
                            "home"
                    },

                    onRegisterClick = {

                        currentScreen.value =
                            "register"
                    }
                )
            }


            // =================================================
            // REGISTER
            // =================================================

            "register" -> {

                RegisterScreen(
                    context = context,

                    onRegisterSuccess = {

                        currentScreen.value =
                            "login"
                    },

                    onBackClick = {

                        currentScreen.value =
                            "login"
                    }
                )
            }


            // =================================================
            // HOME
            // =================================================

            "home" -> {

                HomeScreen(

                    username =
                        currentUsername.value,

                    // Bấm Bài tập
                    onExercisesClick = {

                        selectedMuscle.value =
                            "Tất cả"

                        currentScreen.value =
                            "exercises"
                    },

                    // Bấm Nhóm cơ
                    onMuscleClick = {

                        currentScreen.value =
                            "muscles"
                    },

                    // Bấm Lịch tập
                    onScheduleClick = {

                        currentScreen.value =
                            "schedule"
                    },

                    // Bấm Tiến độ
                    onProgressClick = {

                        currentScreen.value =
                            "progress"
                    },

                    // Đăng xuất
                    onLogout = {

                        AccountStorage.logout(
                            context
                        )

                        currentUsername.value =
                            ""

                        currentScreen.value =
                            "login"
                    }
                )
            }


            // =================================================
            // SCHEDULE
            // =================================================

            "schedule" -> {

                ScheduleScreen(

                    context = context,

                    onBackClick = {

                        currentScreen.value =
                            "home"
                    },

                    onProgressClick = {

                        currentScreen.value =
                            "progress"
                    }
                )
            }


            // =================================================
            // PROGRESS
            // =================================================

            "progress" -> {

                ProgressScreen(

                    context = context,

                    onBackClick = {

                        currentScreen.value =
                            "home"
                    }
                )
            }


            // =================================================
            // ALL EXERCISES
            // =================================================

            "exercises" -> {

                ExerciseListScreen(

                    title = "Bài tập",

                    exerciseList =
                        exercises,

                    selectedMuscle =
                        selectedMuscle.value,

                    onMuscleSelected = { muscle ->

                        selectedMuscle.value =
                            muscle
                    },

                    onBackClick = {

                        currentScreen.value =
                            "home"
                    },

                    onExerciseClick = { exercise ->

                        selectedExercise.value =
                            exercise

                        currentScreen.value =
                            "detail"
                    }
                )
            }


            // =================================================
            // MUSCLE GROUP
            // =================================================

            "muscles" -> {

                MuscleGroupScreen(

                    onBackClick = {

                        currentScreen.value =
                            "home"
                    },

                    onMuscleSelected = { muscle ->

                        selectedMuscle.value =
                            muscle

                        currentScreen.value =
                            "muscleExercises"
                    }
                )
            }


            // =================================================
            // EXERCISES BY MUSCLE
            // =================================================

            "muscleExercises" -> {

                val filteredExercises =
                    if (
                        selectedMuscle.value ==
                        "Tất cả"
                    ) {

                        exercises

                    } else {

                        exercises.filter {

                            it.muscle ==
                                    selectedMuscle.value
                        }
                    }


                ExerciseListScreen(

                    title =
                        selectedMuscle.value,

                    exerciseList =
                        filteredExercises,

                    selectedMuscle =
                        selectedMuscle.value,

                    onMuscleSelected = { muscle ->

                        selectedMuscle.value =
                            muscle
                    },

                    onBackClick = {

                        currentScreen.value =
                            "muscles"
                    },

                    onExerciseClick = { exercise ->

                        selectedExercise.value =
                            exercise

                        currentScreen.value =
                            "detail"
                    }
                )
            }


            // =================================================
            // EXERCISE DETAIL
            // =================================================

            "detail" -> {

                selectedExercise.value?.let { exercise ->

                    ExerciseDetailScreen(

                        exercise =
                            exercise,

                        onBackClick = {

                            if (
                                selectedMuscle.value !=
                                "Tất cả"
                            ) {

                                currentScreen.value =
                                    "muscleExercises"

                            } else {

                                currentScreen.value =
                                    "exercises"
                            }
                        }
                    )
                }
            }
        }
    }
}