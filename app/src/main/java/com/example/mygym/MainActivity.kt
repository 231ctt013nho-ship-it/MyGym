package com.example.mygym

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.mygym.ui.theme.MyGymTheme


// =====================================================
// 🎨 MYGYM MODERN FITNESS THEME
// =====================================================

val GymBackground = Color(0xFF080B12)

val GymSurface = Color(0xFF101522)

val GymCard = Color(0xFF151C2B)

val GymCardLight = Color(0xFF1C2537)

val GymImageBackground = Color(0xFF202A3D)

val GymWhite = Color(0xFFF5F7FB)

val GymGray = Color(0xFF9CA6B8)

val GymDarkGray = Color(0xFF5E687A)

val GymAccent = Color(0xFF8EA7FF)

val GymAccentLight = Color(0xFFB8C6FF)

val GymAccentDark = Color(0xFF667ED8)

val GymRed = Color(0xFFFF6175)

val GymDivider = Color(0xFF293448)


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
// DATA
// =====================================================

data class Exercise(

    val name: String,

    val muscle: String,

    val description: String,

    val imageRes: Int

)


data class MuscleGroup(

    val name: String,

    val shortName: String

)


// =====================================================
// EXERCISES
// =====================================================

val exercises = listOf(

    Exercise(
        "Đẩy ngực",
        "Ngực",
        "Bài tập compound tập trung vào cơ ngực, vai trước và tay sau.",
        R.drawable.bench_press
    ),

    Exercise(
        "Đẩy ngực dốc",
        "Ngực",
        "Bài tập tập trung nhiều hơn vào phần ngực trên.",
        R.drawable.incline_bench_press
    ),

    Exercise(
        "Ép ngực tạ đơn",
        "Ngực",
        "Bài tập cô lập giúp kéo giãn và phát triển cơ ngực.",
        R.drawable.dumbbell_fly
    ),

    Exercise(
        "Kéo xô",
        "Lưng",
        "Bài tập kéo dọc giúp phát triển cơ xô và lưng trên.",
        R.drawable.lat_pulldown
    ),

    Exercise(
        "Kéo tạ đòn",
        "Lưng",
        "Bài tập kéo ngang giúp phát triển độ dày của lưng.",
        R.drawable.barbell_row
    ),

    Exercise(
        "Squat",
        "Chân",
        "Bài tập compound cơ bản cho đùi, mông và thân dưới.",
        R.drawable.squat
    ),

    Exercise(
        "Đạp chân",
        "Chân",
        "Bài tập máy tập trung vào cơ đùi và cơ chân.",
        R.drawable.leg_press
    ),

    Exercise(
        "Cuốn tay trước",
        "Tay trước",
        "Bài tập cô lập giúp phát triển cơ tay trước.",
        R.drawable.biceps_curl
    ),

    Exercise(
        "Kéo cáp tay sau",
        "Tay sau",
        "Bài tập dây cáp giúp phát triển cơ tay sau.",
        R.drawable.triceps_pushdown
    )
)


// =====================================================
// MUSCLE GROUPS
// =====================================================

val muscleGroups = listOf(

    MuscleGroup("Tất cả", "ALL"),

    MuscleGroup("Ngực", "CHEST"),

    MuscleGroup("Lưng", "BACK"),

    MuscleGroup("Chân", "LEGS"),

    MuscleGroup("Tay trước", "BICEPS"),

    MuscleGroup("Tay sau", "TRICEPS")

)


// =====================================================
// APP
// =====================================================

@Composable
fun GymApp(context: Context) {

    // =====================================================
    // APP STATE - QUẢN LÝ MÀN HÌNH HIỆN TẠI
    // =====================================================
    // Nếu đã chọn "Ghi nhớ đăng nhập" -> mở thẳng HOME.
    val currentScreen = remember {
        mutableStateOf(
            if (AccountStorage.isLoggedIn(context)) "home" else "login"
        )
    }

    val currentUsername = remember {
        mutableStateOf(AccountStorage.getUsername(context))
    }

    val selectedMuscle = remember { mutableStateOf("Tất cả") }
    val selectedExercise = remember { mutableStateOf<Exercise?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GymBackground)
    ) {
        when (currentScreen.value) {

            // =================================================
            // ĐĂNG NHẬP
            // =================================================
            "login" -> LoginScreen(
                context = context,
                onLoginSuccess = { username ->
                    currentUsername.value = username
                    currentScreen.value = "home"
                },
                onRegisterClick = {
                    currentScreen.value = "register"
                },
                onForgotPasswordClick = {
                    currentScreen.value = "forgotPassword"
                }
            )

            // =================================================
            // QUÊN MẬT KHẨU
            // =================================================
            "forgotPassword" -> ForgotPasswordScreen(
                context = context,
                onResetSuccess = {
                    currentScreen.value = "login"
                },
                onBackClick = {
                    currentScreen.value = "login"
                }
            )

            // =================================================
            // ĐĂNG KÝ
            // =================================================
            "register" -> RegisterScreen(
                context = context,
                onRegisterSuccess = {
                    currentScreen.value = "login"
                },
                onBackClick = {
                    currentScreen.value = "login"
                }
            )

            // =================================================
            // TRANG CHỦ
            // =================================================
            "home" -> HomeScreen(
                username = currentUsername.value,
                onExercisesClick = {
                    selectedMuscle.value = "Tất cả"
                    currentScreen.value = "exercises"
                },
                onMuscleClick = {
                    currentScreen.value = "muscles"
                },
                onScheduleClick = {
                    currentScreen.value = "schedule"
                },
                onProgressClick = {
                    currentScreen.value = "progress"
                },
                onLogout = {
                    AccountStorage.logout(context)
                    currentUsername.value = ""
                    currentScreen.value = "login"
                }
            )

            // =================================================
            // LỊCH TẬP
            // =================================================
            "schedule" -> ScheduleScreen(
                context = context,
                onBackClick = { currentScreen.value = "home" },
                onProgressClick = { currentScreen.value = "progress" }
            )

            // =================================================
            // TIẾN ĐỘ
            // =================================================
            "progress" -> ProgressScreen(
                context = context,
                onBackClick = { currentScreen.value = "home" }
            )

            // =================================================
            // DANH SÁCH BÀI TẬP
            // =================================================
            "exercises" -> ExerciseListScreen(
                title = "Bài tập",
                exerciseList = exercises,
                selectedMuscle = selectedMuscle.value,
                onMuscleSelected = { muscle -> selectedMuscle.value = muscle },
                onBackClick = { currentScreen.value = "home" },
                onExerciseClick = { exercise ->
                    selectedExercise.value = exercise
                    currentScreen.value = "detail"
                }
            )

            // =================================================
            // NHÓM CƠ
            // =================================================
            "muscles" -> MuscleGroupScreen(
                onBackClick = { currentScreen.value = "home" },
                onMuscleClick = { muscle ->
                    selectedMuscle.value = muscle
                    currentScreen.value = "muscleExercises"
                }
            )

            // =================================================
            // BÀI TẬP THEO NHÓM CƠ
            // =================================================
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
                    onMuscleSelected = { muscle -> selectedMuscle.value = muscle },
                    onBackClick = { currentScreen.value = "muscles" },
                    onExerciseClick = { exercise ->
                        selectedExercise.value = exercise
                        currentScreen.value = "detail"
                    }
                )
            }

            // =================================================
            // CHI TIẾT BÀI TẬP
            // =================================================
            "detail" -> {
                selectedExercise.value?.let { exercise ->
                    ExerciseDetailScreen(
                        exercise = exercise,
                        onBackClick = {
                            currentScreen.value = if (selectedMuscle.value != "Tất cả") {
                                "muscleExercises"
                            } else {
                                "exercises"
                            }
                        }
                    )
                }
            }
        }
    }
}

// =====================================================
// PRIMARY BUTTON
// =====================================================

@Composable
fun PrimaryButton(

    text: String,

    onClick: () -> Unit,

    modifier: Modifier =
        Modifier

) {

    Button(

        onClick =
            onClick,

        modifier =
            modifier
                .height(52.dp)
                .shadow(
                    5.dp,
                    RoundedCornerShape(14.dp)
                ),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    GymAccent,

                contentColor =
                    Color(0xFF0A0D15)
            )
    ) {

        Text(

            text =
                text,

            fontSize =
                14.sp,

            fontWeight =
                FontWeight.ExtraBold,

            letterSpacing =
                0.5.sp
        )
    }
}


// =====================================================
// SECONDARY BUTTON
// =====================================================

@Composable
fun SecondaryButton(

    text: String,

    onClick: () -> Unit,

    modifier: Modifier =
        Modifier

) {

    Button(

        onClick =
            onClick,

        modifier =
            modifier
                .height(48.dp),

        shape =
            RoundedCornerShape(13.dp),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    GymCard,

                contentColor =
                    GymWhite
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Text(

            text =
                text,

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold
        )
    }
}


// =====================================================
// LOGIN
// =====================================================

@Composable
fun LoginScreen(
    context: Context,
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    // =====================================================
    // LOGIN STATE - DỮ LIỆU FORM ĐĂNG NHẬP
    // =====================================================
    val username = remember {
        mutableStateOf(AccountStorage.getUsername(context))
    }

    val password = remember { mutableStateOf("") }

    // =====================================================
    // GHI NHỚ ĐĂNG NHẬP
    // =====================================================
    val rememberLogin = remember { mutableStateOf(false) }

    // =====================================================
    // THÔNG BÁO LỖI
    // =====================================================
    val message = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // =================================================
            // LOGO
            // =================================================
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(GymAccent.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MG",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymAccent
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "MYGYM",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = GymWhite
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "THỂ HÌNH CÁ NHÂN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = GymGray
            )

            Spacer(Modifier.height(30.dp))

            // =================================================
            // CARD ĐĂNG NHẬP
            // =================================================
            DarkCard {
                Text(
                    text = "CHÀO MỪNG TRỞ LẠI",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Đăng nhập để tiếp tục",
                    fontSize = 13.sp,
                    color = GymGray
                )

                Spacer(Modifier.height(22.dp))

                // =================================================
                // Ô TÀI KHOẢN
                // =================================================
                ModernTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                        message.value = ""
                    },
                    label = "Tài khoản"
                )

                Spacer(Modifier.height(13.dp))

                // =================================================
                // Ô MẬT KHẨU
                // =================================================
                ModernTextField(
                    value = password.value,
                    onValueChange = {
                        password.value = it
                        message.value = ""
                    },
                    label = "Mật khẩu",
                    password = true
                )

                // =================================================
                // GHI NHỚ + QUÊN MẬT KHẨU
                // =================================================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberLogin.value,
                        onCheckedChange = { rememberLogin.value = it }
                    )

                    Text(
                        text = "ghi nhớ đăng nhập",
                        fontSize = 12.sp,
                        color = GymWhite,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                rememberLogin.value = !rememberLogin.value
                            }
                    )

                    Text(
                        text = "Quên mật khẩu?",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GymRed,
                        modifier = Modifier.clickable {
                            onForgotPasswordClick()
                        }
                    )
                }

                // =================================================
                // THÔNG BÁO
                // =================================================
                if (message.value.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message.value,
                        fontSize = 12.sp,
                        color = GymRed,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(14.dp))

                // =================================================
                // NÚT ĐĂNG NHẬP
                // =================================================
                PrimaryButton(
                    text = "ĐĂNG NHẬP",
                    onClick = {
                        if (username.value.isBlank() || password.value.isBlank()) {
                            message.value = "Vui lòng nhập đầy đủ thông tin"
                        } else {
                            val savedPassword = AccountStorage.getPassword(
                                context,
                                username.value
                            )

                            if (savedPassword == null) {
                                message.value = "Tài khoản chưa đăng ký"
                            } else if (savedPassword != password.value) {
                                message.value = "Mật khẩu không đúng"
                            } else {

                                // =================================================
                                // LƯU / HỦY GHI NHỚ ĐĂNG NHẬP
                                // =================================================
                                if (rememberLogin.value) {
                                    AccountStorage.saveLogin(
                                        context,
                                        username.value
                                    )
                                } else {
                                    AccountStorage.logout(context)
                                }

                                onLoginSuccess(username.value)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                // =================================================
                // GOOGLE + FACEBOOK
                // =================================================
                Text(
                    text = "Hoặc đăng nhập với",
                    fontSize = 12.sp,
                    color = GymGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // GOOGLE - GIAO DIỆN, CHƯA KẾT NỐI FIREBASE
                    SecondaryButton(
                        text = "G  Google",
                        onClick = {
                            message.value = "Google: sẽ cấu hình Firebase ở bước sau"
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // FACEBOOK - GIAO DIỆN, CHƯA KẾT NỐI FIREBASE
                    SecondaryButton(
                        text = "f  Facebook",
                        onClick = {
                            message.value = "Facebook: sẽ cấu hình Firebase ở bước sau"
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // =================================================
                // TẠO TÀI KHOẢN
                // =================================================
                SecondaryButton(
                    text = "TẠO TÀI KHOẢN",
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


// =====================================================
// FORGOT PASSWORD - QUÊN MẬT KHẨU
// =====================================================

@Composable
fun ForgotPasswordScreen(
    context: Context,
    onResetSuccess: () -> Unit,
    onBackClick: () -> Unit
) {

    // =====================================================
    // DỮ LIỆU FORM QUÊN MẬT KHẨU
    // =====================================================
    val username = remember { mutableStateOf("") }
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "QUÊN MẬT KHẨU",
                fontSize = 27.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GymWhite,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Đặt lại mật khẩu tài khoản MyGym",
                fontSize = 13.sp,
                color = GymGray,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(25.dp))

            DarkCard {
                // =================================================
                // TÀI KHOẢN
                // =================================================
                ModernTextField(
                    value = username.value,
                    onValueChange = {
                        username.value = it
                        message.value = ""
                    },
                    label = "Tên tài khoản"
                )

                Spacer(Modifier.height(13.dp))

                // =================================================
                // MẬT KHẨU MỚI
                // =================================================
                ModernTextField(
                    value = newPassword.value,
                    onValueChange = {
                        newPassword.value = it
                        message.value = ""
                    },
                    label = "Mật khẩu mới",
                    password = true
                )

                Spacer(Modifier.height(13.dp))

                // =================================================
                // XÁC NHẬN MẬT KHẨU
                // =================================================
                ModernTextField(
                    value = confirmPassword.value,
                    onValueChange = {
                        confirmPassword.value = it
                        message.value = ""
                    },
                    label = "Nhập lại mật khẩu",
                    password = true
                )

                if (message.value.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = message.value,
                        color = GymRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(18.dp))

                // =================================================
                // ĐỔI MẬT KHẨU
                // =================================================
                PrimaryButton(
                    text = "ĐỔI MẬT KHẨU",
                    onClick = {
                        if (
                            username.value.isBlank() ||
                            newPassword.value.isBlank() ||
                            confirmPassword.value.isBlank()
                        ) {
                            message.value = "Vui lòng nhập đầy đủ thông tin"
                        } else if (newPassword.value != confirmPassword.value) {
                            message.value = "Mật khẩu nhập lại không khớp"
                        } else if (!AccountStorage.accountExists(context, username.value)) {
                            message.value = "Tài khoản không tồn tại"
                        } else {
                            AccountStorage.resetPassword(
                                context,
                                username.value,
                                newPassword.value
                            )
                            onResetSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                // =================================================
                // QUAY LẠI
                // =================================================
                SecondaryButton(
                    text = "QUAY LẠI",
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


// =====================================================
// REGISTER
// =====================================================

@Composable
fun RegisterScreen(

    context: Context,

    onRegisterSuccess:
        () -> Unit,

    onBackClick:
        () -> Unit

) {

    val username =
        remember {
            mutableStateOf("")
        }


    val password =
        remember {
            mutableStateOf("")
        }


    val confirmPassword =
        remember {
            mutableStateOf("")
        }


    val message =
        remember {
            mutableStateOf("")
        }


    LazyColumn(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        item {

            Box(

                modifier =
                    Modifier
                        .size(65.dp)
                        .clip(
                            RoundedCornerShape(18.dp)
                        )
                        .background(
                            GymAccent.copy(
                                alpha = 0.15f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(

                    text =
                        "MG",

                    fontSize =
                        22.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        GymAccent
                )
            }


            Spacer(
                Modifier.height(15.dp)
            )


            Text(

                text =
                    "TẠO TÀI KHOẢN",

                fontSize =
                    27.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    GymWhite,

                textAlign =
                    TextAlign.Center
            )


            Spacer(
                Modifier.height(25.dp)
            )


            DarkCard {

                ModernTextField(

                    value =
                        username.value,

                    onValueChange = {

                        username.value =
                            it

                        message.value =
                            ""
                    },

                    label =
                        "Tên tài khoản"
                )


                Spacer(
                    Modifier.height(13.dp)
                )


                ModernTextField(

                    value =
                        password.value,

                    onValueChange = {

                        password.value =
                            it

                        message.value =
                            ""
                    },

                    label =
                        "Mật khẩu",

                    password =
                        true
                )


                Spacer(
                    Modifier.height(13.dp)
                )


                ModernTextField(

                    value =
                        confirmPassword.value,

                    onValueChange = {

                        confirmPassword.value =
                            it

                        message.value =
                            ""
                    },

                    label =
                        "Nhập lại mật khẩu",

                    password =
                        true
                )


                if (
                    message.value.isNotEmpty()
                ) {

                    Spacer(
                        Modifier.height(12.dp)
                    )


                    Text(

                        text =
                            message.value,

                        color =
                            GymRed,

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }


                Spacer(
                    Modifier.height(18.dp)
                )


                PrimaryButton(

                    text =
                        "TẠO TÀI KHOẢN",

                    onClick = {

                        if (
                            username.value.isBlank() ||
                            password.value.isBlank() ||
                            confirmPassword.value.isBlank()
                        ) {

                            message.value =
                                "Vui lòng nhập đầy đủ thông tin"

                        } else if (
                            password.value !=
                            confirmPassword.value
                        ) {

                            message.value =
                                "Mật khẩu không khớp"

                        } else if (
                            AccountStorage.accountExists(

                                context,

                                username.value
                            )
                        ) {

                            message.value =
                                "Tài khoản đã tồn tại"

                        } else {

                            AccountStorage.register(

                                context,

                                username.value,

                                password.value
                            )

                            onRegisterSuccess()
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    Modifier.height(10.dp)
                )


                SecondaryButton(

                    text =
                        "QUAY LẠI",

                    onClick =
                        onBackClick,

                    modifier =
                        Modifier.fillMaxWidth()
                )
            }
        }
    }
}


// =====================================================
// HOME
// =====================================================

@Composable
fun HomeScreen(
    username: String,
    onExercisesClick: () -> Unit,
    onMuscleClick: () -> Unit,
    onScheduleClick: () -> Unit,
    onProgressClick: () -> Unit,
    onLogout: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 22.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "MYGYM",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        color = GymWhite
                    )

                    Text(
                        text = "BẢNG ĐIỀU KHIỂN TẬP LUYỆN",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = GymGray
                    )
                }

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(GymCard),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.take(1).uppercase(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymAccent
                    )
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Xin chào,",
                    fontSize = 14.sp,
                    color = GymGray
                )

                Text(
                    text = username,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = GymWhite
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Sẵn sàng cho buổi tập hôm nay?",
                    fontSize = 13.sp,
                    color = GymGray
                )
            }
        }

        item {
            DashboardHeroCard(
                onClick = onExercisesClick
            )
        }

        item {
            Text(
                text = "KHÁM PHÁ",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp,
                color = GymGray
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSmallCard(
                    title = "BÀI TẬP",
                    subtitle = "Thư viện bài tập",
                    modifier = Modifier.weight(1f),
                    onClick = onExercisesClick
                )

                DashboardSmallCard(
                    title = "NHÓM CƠ",
                    subtitle = "Chọn nhóm cơ",
                    modifier = Modifier.weight(1f),
                    onClick = onMuscleClick
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardSmallCard(
                    title = "LỊCH TẬP",
                    subtitle = "Kế hoạch tuần",
                    modifier = Modifier.weight(1f),
                    onClick = onScheduleClick
                )

                DashboardSmallCard(
                    title = "TIẾN ĐỘ",
                    subtitle = "Theo dõi kết quả",
                    modifier = Modifier.weight(1f),
                    onClick = onProgressClick
                )
            }
        }

        item {
            SectionTitle(
                title = "THƯ VIỆN BÀI TẬP",
                subtitle = "Chọn bài tập để bắt đầu"
            )
        }

        item {
            ExercisePreviewCard(
                exercise = exercises[0],
                onClick = onExercisesClick
            )
        }

        item {
            SecondaryButton(
                text = "ĐĂNG XUẤT",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// =====================================================
// LỊCH TẬP
// =====================================================

data class WorkoutDay(
    val day: String,
    val title: String,
    val exercises: List<String>
)

val weeklyWorkoutPlan = listOf(
    WorkoutDay("THỨ 2", "Ngực & Tay sau", listOf("Đẩy ngực", "Đẩy ngực dốc", "Kéo cáp tay sau")),
    WorkoutDay("THỨ 3", "Lưng & Tay trước", listOf("Kéo xô", "Kéo tạ đòn", "Cuốn tay trước")),
    WorkoutDay("THỨ 4", "Nghỉ phục hồi", emptyList()),
    WorkoutDay("THỨ 5", "Chân", listOf("Squat", "Đạp chân")),
    WorkoutDay("THỨ 6", "Ngực & Tay", listOf("Ép ngực tạ đơn", "Cuốn tay trước", "Kéo cáp tay sau")),
    WorkoutDay("THỨ 7", "Toàn thân", listOf("Squat", "Đẩy ngực", "Kéo xô")),
    WorkoutDay("CHỦ NHẬT", "Nghỉ & phục hồi", emptyList())
)

@Composable
fun ScheduleScreen(
    context: Context,
    onBackClick: () -> Unit,
    onProgressClick: () -> Unit
) {
    val completedDays = remember {
        mutableStateOf(
            AccountStorage.getCompletedDays(context)
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹",
                    fontSize = 38.sp,
                    color = GymWhite,
                    modifier = Modifier.clickable { onBackClick() }
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Lịch tập",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Text(
                        text = "KẾ HOẠCH TẬP LUYỆN TRONG TUẦN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = GymGray
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GymAccent
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "MỤC TIÊU TUẦN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = Color(0xFF101522)
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "${completedDays.value} / ${weeklyWorkoutPlan.count { it.exercises.isNotEmpty() }} buổi đã hoàn thành",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF080B12)
                    )
                }
            }
        }

        items(weeklyWorkoutPlan) { day ->
            val isRest = day.exercises.isEmpty()
            val isDone = completedDays.value.contains(day.day)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GymCard
                ),
                border = BorderStroke(1.dp, GymDivider)
            ) {
                Column(
                    modifier = Modifier.padding(17.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = day.day,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp,
                                color = GymAccent
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = day.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = GymWhite
                            )
                        }

                        if (!isRest) {
                            SecondaryButton(
                                text = if (isDone) "ĐÃ XONG" else "HOÀN THÀNH",
                                onClick = {
                                    val newSet = completedDays.value.toMutableSet()
                                    if (isDone) {
                                        newSet.remove(day.day)
                                    } else {
                                        newSet.add(day.day)
                                    }
                                    completedDays.value = newSet
                                    AccountStorage.saveCompletedDays(
                                        context,
                                        newSet
                                    )
                                }
                            )
                        }
                    }

                    if (!isRest) {
                        Spacer(Modifier.height(12.dp))

                        day.exercises.forEachIndexed { index, exercise ->
                            Text(
                                text = "${index + 1}. $exercise",
                                fontSize = 13.sp,
                                color = GymGray,
                                modifier = Modifier.padding(vertical = 3.dp)
                            )
                        }
                    } else {
                        Spacer(Modifier.height(7.dp))
                        Text(
                            text = "Ngày phục hồi — nghỉ ngơi và bổ sung năng lượng.",
                            fontSize = 13.sp,
                            color = GymGray
                        )
                    }
                }
            }
        }

        item {
            PrimaryButton(
                text = "XEM TIẾN ĐỘ",
                onClick = onProgressClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// =====================================================
// TIẾN ĐỘ
// =====================================================

@Composable
fun ProgressScreen(
    context: Context,
    onBackClick: () -> Unit
) {
    val completedDays = remember {
        mutableStateOf(
            AccountStorage.getCompletedDays(context)
        )
    }

    val totalWorkoutDays =
        weeklyWorkoutPlan.count { it.exercises.isNotEmpty() }

    val completedCount =
        completedDays.value.size.coerceAtMost(totalWorkoutDays)

    val progress =
        if (totalWorkoutDays == 0) 0f
        else completedCount.toFloat() / totalWorkoutDays.toFloat()

    val progressPercent =
        (progress * 100).toInt()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "‹",
                    fontSize = 38.sp,
                    color = GymWhite,
                    modifier = Modifier.clickable { onBackClick() }
                )

                Spacer(Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Tiến độ",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Text(
                        text = "THEO DÕI KẾT QUẢ TẬP LUYỆN",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = GymGray
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GymCard
                ),
                border = BorderStroke(1.dp, GymDivider)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "TIẾN ĐỘ TUẦN NÀY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = GymAccent
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = "$progressPercent%",
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = GymWhite
                    )

                    Spacer(Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        color = GymAccent,
                        trackColor = GymDivider
                    )

                    Spacer(Modifier.height(9.dp))

                    Text(
                        text = "$completedCount / $totalWorkoutDays buổi tập đã hoàn thành",
                        fontSize = 13.sp,
                        color = GymGray
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailStatCard(
                    label = "ĐÃ HOÀN THÀNH",
                    value = "$completedCount BUỔI",
                    modifier = Modifier.weight(1f)
                )

                DetailStatCard(
                    label = "CÒN LẠI",
                    value = "${totalWorkoutDays - completedCount} BUỔI",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            DarkCard {
                Text(
                    text = "MỤC TIÊU",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp,
                    color = GymAccent
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = when {
                        progress >= 1f -> "Xuất sắc! Bạn đã hoàn thành toàn bộ lịch tập tuần này."
                        progress >= 0.5f -> "Rất tốt! Hãy tiếp tục duy trì nhịp tập luyện."
                        progress > 0f -> "Bạn đã bắt đầu tốt. Cố gắng hoàn thành các buổi còn lại."
                        else -> "Chưa có buổi nào hoàn thành. Hãy bắt đầu ngay hôm nay!"
                    },
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = GymGray
                )
            }
        }

        item {
            SecondaryButton(
                text = "QUAY LẠI",
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// =====================================================
// HERO CARD
// =====================================================

@Composable
fun DashboardHeroCard(

    onClick: () -> Unit

) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(175.dp)
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymAccent
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(22.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Column {

                Text(

                    text =
                        "TẬP LUYỆN THÔNG MINH",

                    fontSize =
                        11.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    letterSpacing =
                        1.5.sp,

                    color =
                        Color(0xFF111521)
                )


                Spacer(
                    Modifier.height(6.dp)
                )


                Text(

                    text =
                        "Khám phá bài tập",

                    fontSize =
                        27.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        Color(0xFF080B12)
                )


                Text(

                    text =
                        "Khám phá thư viện bài tập",

                    fontSize =
                        13.sp,

                    color =
                        Color(0xFF30394D)
                )
            }


            Text(

                text =
                    "XEM THƯ VIỆN  →",

                fontSize =
                    11.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing =
                    1.sp,

                color =
                    Color(0xFF080B12)
            )
        }
    }
}


// =====================================================
// SMALL DASHBOARD CARD
// =====================================================

@Composable
fun DashboardSmallCard(

    title: String,

    subtitle: String,

    modifier: Modifier,

    onClick: () -> Unit

) {

    Card(

        modifier =
            modifier
                .height(110.dp)
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(19.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),

            verticalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(

                text =
                    title,

                fontSize =
                    15.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    GymWhite
            )


            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        subtitle,

                    fontSize =
                        12.sp,

                    color =
                        GymGray,

                    modifier =
                        Modifier.weight(1f)
                )


                Text(

                    text =
                        "→",

                    fontSize =
                        20.sp,

                    color =
                        GymAccent
                )
            }
        }
    }
}


// =====================================================
// SECTION TITLE
// =====================================================

@Composable
fun SectionTitle(

    title: String,

    subtitle: String

) {

    Column {

        Text(

            text =
                title,

            fontSize =
                16.sp,

            fontWeight =
                FontWeight.ExtraBold,

            letterSpacing =
                1.sp,

            color =
                GymWhite
        )


        Spacer(
            Modifier.height(3.dp)
        )


        Text(

            text =
                subtitle,

            fontSize =
                12.sp,

            color =
                GymGray
        )
    }
}


// =====================================================
// EXERCISE PREVIEW
// =====================================================

@Composable
fun ExercisePreviewCard(

    exercise: Exercise,

    onClick: () -> Unit

) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            )
    ) {

        Row(

            modifier =
                Modifier.fillMaxSize()
        ) {

            Image(

                painter =
                    painterResource(
                        id =
                            exercise.imageRes
                    ),

                contentDescription =
                    exercise.name,

                modifier =
                    Modifier
                        .width(145.dp)
                        .fillMaxSize()
                        .background(
                            GymImageBackground
                        ),

                contentScale =
                    ContentScale.Crop
            )


            Column(

                modifier =
                    Modifier
                        .weight(1f)
                        .padding(15.dp),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(

                    text =
                        exercise.muscle.uppercase(),

                    fontSize =
                        10.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        1.sp,

                    color =
                        GymAccent
                )


                Spacer(
                    Modifier.height(5.dp)
                )


                Text(

                    text =
                        exercise.name,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        GymWhite,

                    maxLines =
                        2
                )


                Spacer(
                    Modifier.height(7.dp)
                )


                Text(

                    text =
                        "XEM CHI TIẾT  →",

                    fontSize =
                        9.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        0.8.sp,

                    color =
                        GymGray
                )
            }
        }
    }
}


// =====================================================
// EXERCISE LIST
// =====================================================

@Composable
fun ExerciseListScreen(

    title: String,

    exerciseList:
    List<Exercise>,

    selectedMuscle: String,

    onMuscleSelected:
        (String) -> Unit,

    onBackClick:
        () -> Unit,

    onExerciseClick:
        (Exercise) -> Unit

) {

    LazyVerticalGrid(

        columns =
            GridCells.Fixed(2),

        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 14.dp,
                    vertical = 18.dp
                ),

        horizontalArrangement =
            Arrangement.spacedBy(10.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item(
            span = {
                GridItemSpan(
                    maxLineSpan
                )
            }
        ) {

            Column {

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        text =
                            "‹",

                        fontSize =
                            38.sp,

                        color =
                            GymWhite,

                        modifier =
                            Modifier.clickable {
                                onBackClick()
                            }
                    )


                    Spacer(
                        Modifier.width(7.dp)
                    )


                    Column {

                        Text(

                            text =
                                title,

                            fontSize =
                                27.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            color =
                                GymWhite
                        )


                        Text(

                            text =
                                "${exerciseList.size} BÀI TẬP",

                            fontSize =
                                10.sp,

                            fontWeight =
                                FontWeight.Bold,

                            letterSpacing =
                                1.3.sp,

                            color =
                                GymGray
                        )
                    }
                }


                Spacer(
                    Modifier.height(15.dp)
                )


                MuscleFilterRow(

                    selected =
                        selectedMuscle,

                    onSelected =
                        onMuscleSelected
                )


                Spacer(
                    Modifier.height(5.dp)
                )
            }
        }


        items(
            items =
                exerciseList
        ) {

                exercise ->

            ModernExerciseCard(

                exercise =
                    exercise,

                onClick = {

                    onExerciseClick(
                        exercise
                    )
                }
            )
        }
    }
}


// =====================================================
// MUSCLE FILTER
// =====================================================

@Composable
fun MuscleFilterRow(

    selected: String,

    onSelected:
        (String) -> Unit

) {

    androidx.compose.foundation.lazy.LazyRow(

        horizontalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        items(
            muscleGroups
        ) {

                group ->

            val isSelected =
                selected ==
                        group.name


            Card(

                modifier =
                    Modifier
                        .height(46.dp)
                        .clickable {
                            onSelected(
                                group.name
                            )
                        },

                shape =
                    RoundedCornerShape(13.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            if (isSelected)
                                GymAccent
                            else
                                GymCard
                    ),

                border =
                    if (isSelected)
                        null
                    else
                        BorderStroke(
                            1.dp,
                            GymDivider
                        )
            ) {

                Box(

                    modifier =
                        Modifier.padding(
                            horizontal = 14.dp
                        ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(

                        text =
                            group.shortName,

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        letterSpacing =
                            0.7.sp,

                        color =
                            if (isSelected)
                                Color(0xFF090C14)
                            else
                                GymGray
                    )
                }
            }
        }
    }
}


// =====================================================
// MODERN EXERCISE CARD
// =====================================================

@Composable
fun ModernExerciseCard(

    exercise: Exercise,

    onClick: () -> Unit

) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            )
    ) {

        Column {

            Box(

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(
                            0.82f
                        )
                        .background(
                            GymImageBackground
                        )
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                exercise.imageRes
                        ),

                    contentDescription =
                        exercise.name,

                    modifier =
                        Modifier.fillMaxSize(),

                    contentScale =
                        ContentScale.Crop
                )


                // TOP LABEL

                Box(

                    modifier =
                        Modifier
                            .align(
                                Alignment.TopStart
                            )
                            .padding(9.dp)
                            .clip(
                                RoundedCornerShape(
                                    8.dp
                                )
                            )
                            .background(
                                GymBackground.copy(
                                    alpha =
                                        0.82f
                                )
                            )
                            .padding(
                                horizontal = 8.dp,
                                vertical = 5.dp
                            )
                ) {

                    Text(

                        text =
                            exercise.muscle
                                .uppercase(),

                        fontSize =
                            8.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        letterSpacing =
                            0.7.sp,

                        color =
                            GymAccentLight
                    )
                }


                // BOOKMARK

                Box(

                    modifier =
                        Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .padding(9.dp)
                            .size(30.dp)
                            .clip(
                                RoundedCornerShape(
                                    9.dp
                                )
                            )
                            .background(
                                GymBackground.copy(
                                    alpha =
                                        0.82f
                                )
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(

                        text =
                            "LƯU",

                        fontSize =
                            7.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            GymWhite
                    )
                }
            }


            Column(

                modifier =
                    Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 11.dp
                    )
            ) {

                Text(

                    text =
                        exercise.name,

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        GymWhite,

                    maxLines =
                        2
                )


                Spacer(
                    Modifier.height(4.dp)
                )


                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(

                        text =
                            "XEM",

                        fontSize =
                            9.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.sp,

                        color =
                            GymAccent
                    )


                    Spacer(
                        Modifier.weight(1f)
                    )


                    Text(

                        text =
                            "→",

                        fontSize =
                            17.sp,

                        color =
                            GymAccent
                    )
                }
            }
        }
    }
}


// =====================================================
// MUSCLE GROUP SCREEN
// =====================================================

@Composable
fun MuscleGroupScreen(

    onBackClick:
        () -> Unit,

    onMuscleClick:
        (String) -> Unit

) {

    LazyColumn(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(20.dp),

        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        item {

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        "‹",

                    fontSize =
                        38.sp,

                    color =
                        GymWhite,

                    modifier =
                        Modifier.clickable {
                            onBackClick()
                        }
                )


                Spacer(
                    Modifier.width(8.dp)
                )


                Column {

                    Text(

                        text =
                            "Nhóm cơ",

                        fontSize =
                            27.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            GymWhite
                    )


                    Text(

                        text =
                            "CHỌN NHÓM CƠ",

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.3.sp,

                        color =
                            GymGray
                    )
                }
            }


            Spacer(
                Modifier.height(12.dp)
            )
        }


        items(
            muscleGroups.drop(1)
        ) {

                group ->

            MuscleGroupModernCard(

                group =
                    group,

                onClick = {

                    onMuscleClick(
                        group.name
                    )
                }
            )
        }
    }
}


// =====================================================
// MUSCLE MODERN CARD
// =====================================================

@Composable
fun MuscleGroupModernCard(

    group: MuscleGroup,

    onClick: () -> Unit

) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .height(92.dp)
                .clickable {
                    onClick()
                },

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(18.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier =
                    Modifier
                        .size(55.dp)
                        .clip(
                            RoundedCornerShape(15.dp)
                        )
                        .background(
                            GymAccent.copy(
                                alpha =
                                    0.12f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(

                    text =
                        group.shortName
                            .take(2),

                    fontSize =
                        15.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        GymAccent
                )
            }


            Spacer(
                Modifier.width(15.dp)
            )


            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        group.name,

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.ExtraBold,

                    color =
                        GymWhite
                )


                Spacer(
                    Modifier.height(3.dp)
                )


                Text(

                    text =
                        "XEM ${group.name.uppercase()}",

                    fontSize =
                        9.sp,

                    fontWeight =
                        FontWeight.Bold,

                    letterSpacing =
                        0.8.sp,

                    color =
                        GymGray
                )
            }


            Text(

                text =
                    "→",

                fontSize =
                    22.sp,

                color =
                    GymAccent
            )
        }
    }
}


// =====================================================
// EXERCISE DETAIL
// =====================================================

@Composable
fun ExerciseDetailScreen(

    exercise: Exercise,

    onBackClick:
        () -> Unit

) {

    LazyColumn(

        modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 18.dp,
                    vertical = 18.dp
                ),

        verticalArrangement =
            Arrangement.spacedBy(15.dp)
    ) {

        item {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        "‹",

                    fontSize =
                        38.sp,

                    color =
                        GymWhite,

                    modifier =
                        Modifier.clickable {
                            onBackClick()
                        }
                )


                Spacer(
                    Modifier.width(8.dp)
                )


                Column {

                    Text(

                        text =
                            exercise.name,

                        fontSize =
                            24.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            GymWhite
                    )


                    Text(

                        text =
                            exercise.muscle.uppercase(),

                        fontSize =
                            10.sp,

                        fontWeight =
                            FontWeight.Bold,

                        letterSpacing =
                            1.2.sp,

                        color =
                            GymAccent
                    )
                }
            }
        }


        item {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymImageBackground
                    )
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                exercise.imageRes
                        ),

                    contentDescription =
                        exercise.name,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .padding(10.dp),

                    contentScale =
                        ContentScale.Fit
                )
            }
        }


        item {

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                DetailStatCard(

                    label =
                        "NHÓM CƠ",

                    value =
                        exercise.muscle.uppercase(),

                    modifier =
                        Modifier.weight(1f)
                )


                DetailStatCard(

                    label =
                        "MỨC ĐỘ",

                    value =
                        "CƠ BẢN",

                    modifier =
                        Modifier.weight(1f)
                )
            }
        }


        item {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymCard
                    ),

                border =
                    BorderStroke(
                        1.dp,
                        GymDivider
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(18.dp)
                ) {

                    Text(

                        text =
                            "MÔ TẢ",

                        fontSize =
                            11.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        letterSpacing =
                            1.3.sp,

                        color =
                            GymAccent
                    )


                    Spacer(
                        Modifier.height(10.dp)
                    )


                    Text(

                        text =
                            exercise.description,

                        fontSize =
                            15.sp,

                        lineHeight =
                            23.sp,

                        color =
                            GymGray
                    )
                }
            }
        }


        item {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(20.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            GymCard
                    )
            ) {

                Row(

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(18.dp),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column {

                        Text(

                            text =
                                "DỮ LIỆU TẬP LUYỆN",

                            fontSize =
                                11.sp,

                            fontWeight =
                                FontWeight.ExtraBold,

                            letterSpacing =
                                1.2.sp,

                            color =
                                GymAccent
                        )


                        Spacer(
                            Modifier.height(5.dp)
                        )


                        Text(

                            text =
                                "Chưa thiết lập số hiệp và số lần",

                            fontSize =
                                13.sp,

                            color =
                                GymGray
                        )
                    }


                    Text(

                        text =
                            "—",

                        fontSize =
                            22.sp,

                        color =
                            GymDarkGray
                    )
                }
            }
        }
    }
}


// =====================================================
// DETAIL STAT
// =====================================================

@Composable
fun DetailStatCard(

    label: String,

    value: String,

    modifier: Modifier

) {

    Card(

        modifier =
            modifier.height(85.dp),

        shape =
            RoundedCornerShape(17.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(14.dp),

            verticalArrangement =
                Arrangement.Center
        ) {

            Text(

                text =
                    label,

                fontSize =
                    9.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                letterSpacing =
                    1.sp,

                color =
                    GymGray
            )


            Spacer(
                Modifier.height(4.dp)
            )


            Text(

                text =
                    value,

                fontSize =
                    14.sp,

                fontWeight =
                    FontWeight.ExtraBold,

                color =
                    GymWhite
            )
        }
    }
}


// =====================================================
// MODERN TEXT FIELD
// =====================================================

@Composable
fun ModernTextField(

    value: String,

    onValueChange:
        (String) -> Unit,

    label: String,

    password: Boolean =
        false

) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        label = {

            Text(
                text =
                    label
            )
        },

        modifier =
            Modifier.fillMaxWidth(),

        singleLine =
            true,

        visualTransformation =
            if (password)
                PasswordVisualTransformation()
            else
                androidx.compose.ui.text.input.VisualTransformation.None,

        shape =
            RoundedCornerShape(14.dp),

        colors =
            OutlinedTextFieldDefaults.colors(

                focusedBorderColor =
                    GymAccent,

                unfocusedBorderColor =
                    GymDivider,

                focusedLabelColor =
                    GymAccent,

                unfocusedLabelColor =
                    GymGray,

                focusedTextColor =
                    GymWhite,

                unfocusedTextColor =
                    GymWhite,

                cursorColor =
                    GymAccent
            )
    )
}


// =====================================================
// DARK CARD
// =====================================================

@Composable
fun DarkCard(

    content:
    @Composable () -> Unit

) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(23.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    GymCard
            ),

        border =
            BorderStroke(
                1.dp,
                GymDivider
            )
    ) {

        Column(

            modifier =
                Modifier.padding(21.dp)
        ) {

            content()
        }
    }
}


// =====================================================
// ACCOUNT STORAGE
// =====================================================

object AccountStorage {

    private const val PREF_NAME =
        "MyGymAccount"

    private const val USERNAME =
        "username"

    private const val PASSWORD =
        "password"

    private const val IS_LOGGED_IN =
        "isLoggedIn"


    fun register(

        context: Context,

        username: String,

        password: String

    ) {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        preferences.edit()

            .putString(
                USERNAME,
                username
            )

            .putString(
                PASSWORD,
                password
            )

            .putBoolean(
                IS_LOGGED_IN,
                false
            )

            .apply()
    }


    fun accountExists(

        context: Context,

        username: String

    ): Boolean {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        return preferences.getString(
            USERNAME,
            null
        ) == username
    }


    fun getPassword(

        context: Context,

        username: String

    ): String? {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        val savedUsername =
            preferences.getString(
                USERNAME,
                null
            )


        if (
            savedUsername == username
        ) {

            return preferences.getString(
                PASSWORD,
                null
            )
        }


        return null
    }


    fun getUsername(

        context: Context

    ): String {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        return preferences.getString(
            USERNAME,
            ""
        ) ?: ""
    }


    fun saveLogin(

        context: Context,

        username: String

    ) {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        preferences.edit()

            .putString(
                USERNAME,
                username
            )

            .putBoolean(
                IS_LOGGED_IN,
                true
            )

            .apply()
    }


    fun getCompletedDays(
        context: Context
    ): Set<String> {
        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        return preferences
            .getStringSet(
                "completedDays",
                emptySet()
            )
            ?.toSet()
            ?: emptySet()
    }

    fun saveCompletedDays(
        context: Context,
        days: Set<String>
    ) {
        val preferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        preferences.edit()
            .putStringSet(
                "completedDays",
                days
            )
            .apply()
    }


    // =====================================================
    // KIỂM TRA ĐÃ GHI NHỚ ĐĂNG NHẬP CHƯA
    // =====================================================
    fun isLoggedIn(
        context: Context
    ): Boolean {

        val preferences = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        return preferences.getBoolean(
            IS_LOGGED_IN,
            false
        )
    }


    // =====================================================
    // ĐỔI MẬT KHẨU
    // =====================================================
    fun resetPassword(
        context: Context,
        username: String,
        newPassword: String
    ) {

        val preferences = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        if (preferences.getString(USERNAME, null) == username) {
            preferences.edit()
                .putString(PASSWORD, newPassword)
                .putBoolean(IS_LOGGED_IN, false)
                .apply()
        }
    }


    fun logout(
        context: Context
    ) {

        val preferences =
            context.getSharedPreferences(

                PREF_NAME,

                Context.MODE_PRIVATE
            )


        preferences.edit()

            .putBoolean(
                IS_LOGGED_IN,
                false
            )

            .apply()
    }
}

