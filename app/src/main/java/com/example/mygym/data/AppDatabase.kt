package com.example.mygym.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. BẢNG NGƯỜI DÙNG (Có phân quyền role: "ADMIN" hoặc "USER")
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String,
    val role: String = "USER" // "ADMIN" hoặc "USER"
)

// 2. BẢNG BÀI TẬP (Lưu động trong DB để Admin có thể Thêm/Xóa)
@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val muscle: String,
    val sets: String,
    val reps: String,
    val description: String,
    val imageRes: Int
)

// 3. DAO TRUY VẤN DỮ LIỆU
@Dao
interface GymDao {
    // --- Quản lý User ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun login(username: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("UPDATE users SET password = :newPassword WHERE username = :username")
    suspend fun updatePassword(username: String, newPassword: String)

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    // --- Quản lý Bài tập ---
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exercise: ExerciseEntity)

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int
}

// 4. KHỞI TẠO DATABASE & TÀI KHOẢN ADMIN MẶC ĐỊNH
@Database(entities = [UserEntity::class, ExerciseEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gymDao(): GymDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mygym_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}