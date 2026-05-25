package com.example.model

data class AcademicScores(
    val math: Int,
    val science: Int,
    val english: Int,
    val history: Int,
    val art: Int
) {
    fun getAverage(): Float {
        return (math + science + english + history + art) / 5.0f
    }
}

data class SkillRatings(
    val creativity: Int, // 1-5
    val leadership: Int, // 1-5
    val communication: Int, // 1-5
    val discipline: Int, // 1-5
    val activity: Int // 1-5
) {
    fun getAverageRating(): Float {
        return (creativity + leadership + communication + discipline + activity) / 5.0f
    }
}

data class Student(
    val id: String,
    val name: String,
    val className: String,
    val skillTag: String, // e.g. "Creative", "Leader", "Communicator"
    val academicScores: AcademicScores,
    val skillRatings: SkillRatings,
    val remarks: String
)
