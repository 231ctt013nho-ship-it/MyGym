package com.example.mygym.data

import android.content.Context
import com.example.mygym.R

object DatabaseSeeder {
    suspend fun seedInitialData(context: Context) {
        val dao = AppDatabase.getDatabase(context).gymDao()

        // 1. Tạo tài khoản ADMIN mặc định nếu chưa có
        if (dao.getUserByUsername("admin") == null) {
            dao.insertUser(
                UserEntity(
                    username = "admin",
                    password = "123456",
                    role = "ADMIN"
                )
            )
        }

        // 2. Nạp danh sách bài tập mẫu vào DB nếu DB đang trống
        if (dao.getExerciseCount() == 0) {
            val defaultExercises = listOf(
                ExerciseEntity(name = "Đẩy ngực ngang (Bench Press)", muscle = "CHEST", sets = "4 hiệp", reps = "8-10 lần", description = "Nằm trên ghế phẳng, hạ tạ chạm nhẹ giữa ngực rồi đẩy thẳng lên.", imageRes = R.drawable.bench_press),
                ExerciseEntity(name = "Đẩy ngực trên (Incline Press)", muscle = "CHEST", sets = "4 hiệp", reps = "10 lần", description = "Chỉnh ghế dốc 30-45 độ, đẩy tạ tập trung vào phần ngực trên.", imageRes = R.drawable.incline_bench_press),
                ExerciseEntity(name = "Ép ngực tạ đơn (Dumbbell Fly)", muscle = "CHEST", sets = "3 hiệp", reps = "12 lần", description = "Mở rộng hai tay sang ngang giữ khuỷu tay hơi cong, ép ngực lại.", imageRes = R.drawable.dumbbell_fly),
                ExerciseEntity(name = "Kéo xô máy (Lat Pulldown)", muscle = "BACK", sets = "4 hiệp", reps = "10-12 lần", description = "Kéo thanh đòn xuống ngang cằm, ép chặt cơ xô lưng.", imageRes = R.drawable.lat_pulldown),
                ExerciseEntity(name = "Gập người kéo tạ (Barbell Row)", muscle = "BACK", sets = "4 hiệp", reps = "8-10 lần", description = "Giữ lưng thẳng, gập người 45 độ kéo thanh đòn về phía bụng dưới.", imageRes = R.drawable.barbell_row),
                ExerciseEntity(name = "Gánh đùi (Squat)", muscle = "LEGS", sets = "4 hiệp", reps = "8-10 lần", description = "Đặt thanh đòn lên cầu vai, hạ hông xuống sâu đến khi đùi song song sàn.", imageRes = R.drawable.squat),
                ExerciseEntity(name = "Đạp đùi máy (Leg Press)", muscle = "LEGS", sets = "4 hiệp", reps = "12 lần", description = "Đặt chân rộng bằng vai trên bàn đạp, đẩy mạnh và hạ có kiểm soát.", imageRes = R.drawable.leg_press),
                ExerciseEntity(name = "Cuốn tạ tay trước (Biceps Curl)", muscle = "BICEPS", sets = "3 hiệp", reps = "12 lần", description = "Cố định khuỷu tay sát thân người, cuốn tạ lên siết chặt bắp tay trước.", imageRes = R.drawable.biceps_curl),
                ExerciseEntity(name = "Đẩy cáp tay sau (Triceps Pushdown)", muscle = "TRICEPS", sets = "3 hiệp", reps = "12-15 lần", description = "Kép sát khuỷu tay, đẩy dây cáp thẳng xuống dưới siết cơ tay sau.", imageRes = R.drawable.triceps_pushdown)
            )
            defaultExercises.forEach { dao.insertExercise(it) }
        }
    }
}