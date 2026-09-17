package com.example.mygym.data

import com.example.mygym.R

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

val muscleGroups = listOf(
    MuscleGroup("Tất cả", "ALL"),
    MuscleGroup("Ngực", "CHEST"),
    MuscleGroup("Lưng", "BACK"),
    MuscleGroup("Chân", "LEGS"),
    MuscleGroup("Tay trước", "BICEPS"),
    MuscleGroup("Tay sau", "TRICEPS")
)