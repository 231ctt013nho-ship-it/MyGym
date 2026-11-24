package com.example.mygym.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mygym.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserAccount(
    val username: String,
    val email: String = "",
    val password: String,
    val role: String // "ADMIN" hoặc "USER"
)

data class DbExercise(
    val id: Int,
    val name: String,
    val muscle: String,
    val sets: String,
    val reps: String,
    val description: String,
    val imageRes: Int
)

data class WorkoutLogEntry(
    val id: Int,
    val username: String,
    val dayKey: String,
    val dayTitle: String,
    val targetMuscle: String,
    val completedCount: Int,
    val totalExercises: Int,
    val totalVolumeKg: Float,
    val durationMinutes: Int,
    val startTime: String,
    val endTime: String,
    val completedList: String = "",
    val uncompletedList: String = "",
    val notes: String = "",
    val dateStr: String = ""
) {
    val progressPercent: Int
        get() = if (totalExercises > 0) ((completedCount.toFloat() / totalExercises) * 100).toInt() else 0
}

data class WeeklyEvaluationEntry(
    val id: Int,
    val username: String,
    val monthStr: String,
    val weekNum: Int,
    val difficultyRating: String,
    val assessmentNote: String
)

data class PasswordResetLogEntry(
    val id: Int,
    val username: String,
    val email: String,
    val otpCode: String,
    val status: String,
    val createdAt: String
)

class GymDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "mygym_app.db", null, 7) {

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Bảng Users (Bổ sung cột email/gmail)
        db.execSQL(
            """
            CREATE TABLE users (
                username TEXT PRIMARY KEY,
                email TEXT,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            )
            """.trimIndent()
        )

        // 2. Bảng Exercises
        db.execSQL(
            """
            CREATE TABLE exercises (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                muscle TEXT NOT NULL,
                sets TEXT NOT NULL,
                reps TEXT NOT NULL,
                description TEXT NOT NULL,
                imageRes INTEGER NOT NULL
            )
            """.trimIndent()
        )

        // 3. Bảng Workout Logs
        db.execSQL(
            """
            CREATE TABLE workout_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                day_key TEXT NOT NULL,
                day_title TEXT NOT NULL,
                target_muscle TEXT NOT NULL,
                completed_count INTEGER NOT NULL,
                total_exercises INTEGER NOT NULL,
                total_volume_kg REAL NOT NULL,
                duration_minutes INTEGER NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT NOT NULL,
                completed_list TEXT,
                uncompleted_list TEXT,
                notes TEXT,
                date_str TEXT NOT NULL
            )
            """.trimIndent()
        )

        // 4. Bảng Weekly Evaluations
        db.execSQL(
            """
            CREATE TABLE weekly_evaluations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                month_str TEXT NOT NULL,
                week_num INTEGER NOT NULL,
                difficulty_rating TEXT NOT NULL,
                assessment_note TEXT
            )
            """.trimIndent()
        )

        // 5. Bảng Password Reset Logs (Quản lý cấp mã OTP & Nhật ký đổi mật khẩu cho Admin)
        db.execSQL(
            """
            CREATE TABLE password_reset_logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                email TEXT NOT NULL,
                otp_code TEXT NOT NULL,
                status TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """.trimIndent()
        )

        // Tài khoản ADMIN mặc định (admin / 123456)
        val adminValues = ContentValues().apply {
            put("username", "admin")
            put("email", "admin@mygym.com")
            put("password", "123456")
            put("role", "ADMIN")
        }
        db.insert("users", null, adminValues)

        // Tài khoản USER mặc định (user / 123456)
        val userValues = ContentValues().apply {
            put("username", "user")
            put("email", "user@gmail.com")
            put("password", "123456")
            put("role", "USER")
        }
        db.insert("users", null, userValues)

        seedDefaultExercises(db)
        seedInitialResetLogs(db)
    }

    private fun seedDefaultExercises(db: SQLiteDatabase) {
        val list = listOf(
            DbExercise(0, "Đẩy ngực ngang (Bench Press)", "Ngực", "4 hiệp", "8-10 lần", "Nằm trên ghế phẳng, hạ tạ chạm nhẹ giữa ngực rồi đẩy thẳng lên.", R.drawable.bench_press),
            DbExercise(0, "Đẩy ngực trên (Incline Press)", "Ngực", "4 hiệp", "10 lần", "Chỉnh ghế dốc 30-45 độ, đẩy tạ tập trung vào phần ngực trên.", R.drawable.incline_bench_press),
            DbExercise(0, "Ép ngực tạ đơn (Dumbbell Fly)", "Ngực", "3 hiệp", "12 lần", "Mở rộng hai tay sang ngang giữ khuỷu tay hơi cong, ép ngực lại.", R.drawable.dumbbell_fly),
            DbExercise(0, "Kéo xô máy (Lat Pulldown)", "Lưng", "4 hiệp", "10-12 lần", "Kéo thanh đòn xuống ngang cằm, ép chặt cơ xô lưng.", R.drawable.lat_pulldown),
            DbExercise(0, "Gập người kéo tạ (Barbell Row)", "Lưng", "4 hiệp", "8-10 lần", "Giữ lưng thẳng, gập người 45 độ kéo thanh đòn về phía bụng dưới.", R.drawable.barbell_row),
            DbExercise(0, "Gánh đùi (Squat)", "Chân", "4 hiệp", "8-10 lần", "Đặt thanh đòn lên cầu vai, hạ hông xuống sâu đến khi đùi song song sàn.", R.drawable.squat),
            DbExercise(0, "Đạp đùi máy (Leg Press)", "Chân", "4 hiệp", "12 lần", "Đặt chân rộng bằng vai trên bàn đạp, đẩy mạnh và hạ có kiểm soát.", R.drawable.leg_press),
            DbExercise(0, "Cuốn tạ tay trước (Biceps Curl)", "Tay trước", "3 hiệp", "12 lần", "Cố định khuỷu tay sát thân người, cuốn tạ lên siết chặt bắp tay trước.", R.drawable.biceps_curl),
            DbExercise(0, "Đẩy cáp tay sau (Triceps Pushdown)", "Tay sau", "3 hiệp", "12-15 lần", "Kép sát khuỷu tay, đẩy dây cáp thẳng xuống dưới siết cơ tay sau.", R.drawable.triceps_pushdown)
        )

        for (item in list) {
            val cv = ContentValues().apply {
                put("name", item.name)
                put("muscle", item.muscle)
                put("sets", item.sets)
                put("reps", item.reps)
                put("description", item.description)
                put("imageRes", item.imageRes)
            }
            db.insert("exercises", null, cv)
        }
    }

    private fun seedInitialResetLogs(db: SQLiteDatabase) {
        val cv = ContentValues().apply {
            put("username", "user")
            put("email", "user@gmail.com")
            put("otp_code", "849201")
            put("status", "ĐÃ ĐỔI MẬT KHẨU")
            put("created_at", SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()))
        }
        db.insert("password_reset_logs", null, cv)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS exercises")
        db.execSQL("DROP TABLE IF EXISTS workout_logs")
        db.execSQL("DROP TABLE IF EXISTS weekly_evaluations")
        db.execSQL("DROP TABLE IF EXISTS password_reset_logs")
        onCreate(db)
    }

    // ==================== USERS & AUTHENTICATION ====================

    fun getUser(usernameOrEmail: String): UserAccount? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT username, email, password, role FROM users WHERE username = ? OR email = ?", arrayOf(usernameOrEmail, usernameOrEmail))
        var user: UserAccount? = null
        if (cursor.moveToFirst()) {
            user = UserAccount(
                username = cursor.getString(0),
                email = cursor.getString(1) ?: "",
                password = cursor.getString(2),
                role = cursor.getString(3)
            )
        }
        cursor.close()
        return user
    }

    fun getUserByEmail(email: String): UserAccount? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT username, email, password, role FROM users WHERE email = ?", arrayOf(email))
        var user: UserAccount? = null
        if (cursor.moveToFirst()) {
            user = UserAccount(
                username = cursor.getString(0),
                email = cursor.getString(1) ?: "",
                password = cursor.getString(2),
                role = cursor.getString(3)
            )
        }
        cursor.close()
        return user
    }

    fun getAllUsers(): List<UserAccount> {
        val list = mutableListOf<UserAccount>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT username, email, password, role FROM users", null)
        while (cursor.moveToNext()) {
            list.add(
                UserAccount(
                    username = cursor.getString(0),
                    email = cursor.getString(1) ?: "",
                    password = cursor.getString(2),
                    role = cursor.getString(3)
                )
            )
        }
        cursor.close()
        return list
    }

    fun registerUserWithEmail(username: String, email: String, password: String, role: String = "USER"): Boolean {
        if (getUser(username) != null || (email.isNotBlank() && getUserByEmail(email) != null)) return false
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("password", password)
            put("role", role)
        }
        return db.insert("users", null, cv) != -1L
    }

    fun updatePasswordByEmail(email: String, newPassword: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply { put("password", newPassword) }
        val rows = db.update("users", cv, "email = ? OR username = ?", arrayOf(email, email))
        if (rows > 0) {
            val acc = getUserByEmail(email) ?: getUser(email)
            logPasswordReset(
                username = acc?.username ?: email,
                email = acc?.email ?: email,
                otpCode = "XÁC THỰC SUCCESS",
                status = "ĐÃ ĐỔI MẬT KHẨU TỰ ĐỘNG"
            )
        }
        return rows > 0
    }

    fun getUserCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM users", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        return count
    }

    // ==================== ADMIN PASSWORD RESET & OTP MANAGEMENT ====================

    fun logPasswordReset(username: String, email: String, otpCode: String, status: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("username", username)
            put("email", email)
            put("otp_code", otpCode)
            put("status", status)
            put("created_at", SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()))
        }
        return db.insert("password_reset_logs", null, cv) != -1L
    }

    fun getAllPasswordResets(): List<PasswordResetLogEntry> {
        val list = mutableListOf<PasswordResetLogEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, username, email, otp_code, status, created_at FROM password_reset_logs ORDER BY id DESC", null)
        while (cursor.moveToNext()) {
            list.add(
                PasswordResetLogEntry(
                    id = cursor.getInt(0),
                    username = cursor.getString(1),
                    email = cursor.getString(2),
                    otpCode = cursor.getString(3),
                    status = cursor.getString(4),
                    createdAt = cursor.getString(5)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getLatestOtpForEmail(email: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT otp_code FROM password_reset_logs WHERE email = ? OR username = ? ORDER BY id DESC LIMIT 1", arrayOf(email, email))
        var otp: String? = null
        if (cursor.moveToFirst()) {
            otp = cursor.getString(0)
        }
        cursor.close()
        return otp
    }

    // ==================== EXERCISES ====================

    fun getAllExercises(): List<DbExercise> {
        val list = mutableListOf<DbExercise>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, name, muscle, sets, reps, description, imageRes FROM exercises ORDER BY id DESC", null)
        while (cursor.moveToNext()) {
            list.add(
                DbExercise(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    muscle = cursor.getString(2),
                    sets = cursor.getString(3),
                    reps = cursor.getString(4),
                    description = cursor.getString(5),
                    imageRes = cursor.getInt(6)
                )
            )
        }
        cursor.close()
        return list
    }

    fun insertExercise(name: String, muscle: String, sets: String, reps: String, description: String, imageRes: Int) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("name", name)
            put("muscle", muscle)
            put("sets", sets)
            put("reps", reps)
            put("description", description)
            put("imageRes", imageRes)
        }
        db.insert("exercises", null, cv)
    }

    fun deleteExercise(id: Int) {
        val db = writableDatabase
        db.delete("exercises", "id = ?", arrayOf(id.toString()))
    }

    // ==================== WORKOUT LOGS ====================

    fun saveWorkoutLog(
        username: String,
        dayKey: String,
        dayTitle: String,
        targetMuscle: String,
        completedCount: Int,
        totalExercises: Int,
        totalVolumeKg: Float,
        durationMinutes: Int,
        startTime: String,
        endTime: String,
        completedList: String,
        uncompletedList: String,
        notes: String
    ): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("username", username)
            put("day_key", dayKey)
            put("day_title", dayTitle)
            put("target_muscle", targetMuscle)
            put("completed_count", completedCount)
            put("total_exercises", totalExercises)
            put("total_volume_kg", totalVolumeKg)
            put("duration_minutes", durationMinutes)
            put("start_time", startTime)
            put("end_time", endTime)
            put("completed_list", completedList)
            put("uncompleted_list", uncompletedList)
            put("notes", notes)
            put("date_str", SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
        }
        return db.insert("workout_logs", null, cv) != -1L
    }

    fun deleteWorkoutLog(id: Int): Boolean {
        val db = writableDatabase
        val rows = db.delete("workout_logs", "id = ?", arrayOf(id.toString()))
        return rows > 0
    }

    fun updateWorkoutLogNotes(id: Int, newNotes: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("notes", newNotes)
        }
        return db.update("workout_logs", cv, "id = ?", arrayOf(id.toString())) > 0
    }

    fun getAllWorkoutLogs(): List<WorkoutLogEntry> {
        val list = mutableListOf<WorkoutLogEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, username, day_key, day_title, target_muscle, completed_count, total_exercises, total_volume_kg, duration_minutes, start_time, end_time, completed_list, uncompleted_list, notes, date_str FROM workout_logs ORDER BY id DESC", null)
        while (cursor.moveToNext()) {
            list.add(
                WorkoutLogEntry(
                    id = cursor.getInt(0),
                    username = cursor.getString(1),
                    dayKey = cursor.getString(2),
                    dayTitle = cursor.getString(3),
                    targetMuscle = cursor.getString(4),
                    completedCount = cursor.getInt(5),
                    totalExercises = cursor.getInt(6),
                    totalVolumeKg = cursor.getFloat(7),
                    durationMinutes = cursor.getInt(8),
                    startTime = cursor.getString(9),
                    endTime = cursor.getString(10),
                    completedList = cursor.getString(11) ?: "",
                    uncompletedList = cursor.getString(12) ?: "",
                    notes = cursor.getString(13) ?: "",
                    dateStr = cursor.getString(14)
                )
            )
        }
        cursor.close()
        return list
    }

    fun getUserWorkoutLogs(username: String): List<WorkoutLogEntry> {
        val list = mutableListOf<WorkoutLogEntry>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, username, day_key, day_title, target_muscle, completed_count, total_exercises, total_volume_kg, duration_minutes, start_time, end_time, completed_list, uncompleted_list, notes, date_str FROM workout_logs WHERE username = ? ORDER BY id DESC", arrayOf(username))
        while (cursor.moveToNext()) {
            list.add(
                WorkoutLogEntry(
                    id = cursor.getInt(0),
                    username = cursor.getString(1),
                    dayKey = cursor.getString(2),
                    dayTitle = cursor.getString(3),
                    targetMuscle = cursor.getString(4),
                    completedCount = cursor.getInt(5),
                    totalExercises = cursor.getInt(6),
                    totalVolumeKg = cursor.getFloat(7),
                    durationMinutes = cursor.getInt(8),
                    startTime = cursor.getString(9),
                    endTime = cursor.getString(10),
                    completedList = cursor.getString(11) ?: "",
                    uncompletedList = cursor.getString(12) ?: "",
                    notes = cursor.getString(13) ?: "",
                    dateStr = cursor.getString(14)
                )
            )
        }
        cursor.close()
        return list
    }

    // ==================== WEEKLY EVALUATION ====================

    fun saveWeeklyEvaluation(username: String, monthStr: String, weekNum: Int, rating: String, note: String): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put("username", username)
            put("month_str", monthStr)
            put("week_num", weekNum)
            put("difficulty_rating", rating)
            put("assessment_note", note)
        }
        db.delete("weekly_evaluations", "username = ? AND month_str = ? AND week_num = ?", arrayOf(username, monthStr, weekNum.toString()))
        return db.insert("weekly_evaluations", null, cv) != -1L
    }

    fun getWeeklyEvaluation(username: String, monthStr: String, weekNum: Int): WeeklyEvaluationEntry? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id, username, month_str, week_num, difficulty_rating, assessment_note FROM weekly_evaluations WHERE username = ? AND month_str = ? AND week_num = ? LIMIT 1", arrayOf(username, monthStr, weekNum.toString()))
        var entry: WeeklyEvaluationEntry? = null
        if (cursor.moveToFirst()) {
            entry = WeeklyEvaluationEntry(
                id = cursor.getInt(0),
                username = cursor.getString(1),
                monthStr = cursor.getString(2),
                weekNum = cursor.getInt(3),
                difficultyRating = cursor.getString(4),
                assessmentNote = cursor.getString(5) ?: ""
            )
        }
        cursor.close()
        return entry
    }
}
