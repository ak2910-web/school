package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.model.AcademicScores
import com.example.model.SkillRatings
import com.example.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class StudentViewModel : ViewModel() {

    // Login state
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // Role state: School side vs Student/Parent side
    private val _isSchoolSide = MutableStateFlow(true)
    val isSchoolSide: StateFlow<Boolean> = _isSchoolSide.asStateFlow()

    private val _loggedInStudentId = MutableStateFlow<String?>(null)
    val loggedInStudentId: StateFlow<String?> = _loggedInStudentId.asStateFlow()

    fun login(email: String, password: String) {
        // Simple UI authentication: allow any reasonably structured credentials to log in!
        if (email.contains("@") && password.length >= 4) {
            _isSchoolSide.value = true
            _loggedInStudentId.value = null
            _isLoggedIn.value = true
        }
    }

    fun loginAsSchool(email: String, password: String): Boolean {
        if (email.contains("@") && password.length >= 4) {
            _isSchoolSide.value = true
            _loggedInStudentId.value = null
            _isLoggedIn.value = true
            return true
        }
        return false
    }

    fun loginAsStudent(studentId: String): Boolean {
        val studentExists = _students.value.any { it.id == studentId }
        if (studentExists) {
            _isSchoolSide.value = false
            _loggedInStudentId.value = studentId
            _selectedStudentId.value = studentId // pre-select this student
            _isLoggedIn.value = true
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
        _loggedInStudentId.value = null
        _isSchoolSide.value = true
    }

    // List of students (in-memory persistence)
    private val _students = MutableStateFlow(
        listOf(
            Student(
                id = "1",
                name = "Amit Sharma",
                className = "Class 10A",
                skillTag = "Creative",
                academicScores = AcademicScores(math = 85, science = 90, english = 88, history = 82, art = 95),
                skillRatings = SkillRatings(creativity = 5, leadership = 3, communication = 4, discipline = 4, activity = 5),
                remarks = "Excellent artistic capacity and problem solver. Shows tremendous creative focus."
            ),
            Student(
                id = "2",
                name = "Rahul Verma",
                className = "Class 10B",
                skillTag = "Leader",
                academicScores = AcademicScores(math = 78, science = 80, english = 85, history = 92, art = 70),
                skillRatings = SkillRatings(creativity = 3, leadership = 5, communication = 5, discipline = 4, activity = 4),
                remarks = "Exhibits exceptional team coordination skills. Often leads classroom projects."
            ),
            Student(
                id = "3",
                name = "Priya Singh",
                className = "Class 10A",
                skillTag = "Communicator",
                academicScores = AcademicScores(math = 92, science = 88, english = 96, history = 90, art = 85),
                skillRatings = SkillRatings(creativity = 4, leadership = 4, communication = 5, discipline = 5, activity = 3),
                remarks = "Very articulate in discussions. Inspires peers with structured arguments."
            ),
            Student(
                id = "4",
                name = "Sneha Patel",
                className = "Class 9C",
                skillTag = "Leader",
                academicScores = AcademicScores(math = 81, science = 85, english = 80, history = 85, art = 82),
                skillRatings = SkillRatings(creativity = 4, leadership = 4, communication = 3, discipline = 5, activity = 4),
                remarks = "Quiet observer but highly adaptable. Consistently disciplined and reliable."
            ),
            Student(
                id = "5",
                name = "Vikram Reddy",
                className = "Class 11A",
                skillTag = "Creative",
                academicScores = AcademicScores(math = 98, science = 95, english = 82, history = 80, art = 65),
                skillRatings = SkillRatings(creativity = 5, leadership = 3, communication = 3, discipline = 4, activity = 3),
                remarks = "Outstanding mathematical aptitude. Prefers focused analytical research work."
            )
        )
    )
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    // Selected student id for detail and evaluation screeens
    private val _selectedStudentId = MutableStateFlow<String?>("1")
    val selectedStudentId: StateFlow<String?> = _selectedStudentId.asStateFlow()

    fun selectStudent(id: String) {
        _selectedStudentId.value = id
    }

    // Search query state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Filtered student list
    val filteredStudents = combine(students, searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.className.contains(query, ignoreCase = true) ||
                it.skillTag.contains(query, ignoreCase = true)
            }
        }
    }

    // Save/Update helper for evaluation screen
    fun updateEvaluation(
        studentId: String,
        math: Int,
        science: Int,
        english: Int,
        history: Int,
        art: Int,
        creativity: Int,
        leadership: Int,
        communication: Int,
        discipline: Int,
        activity: Int,
        remarks: String
    ) {
        _students.value = _students.value.map { student ->
            if (student.id == studentId) {
                // Determine skill tag based on the highest rating
                val ratings = mapOf(
                    "Creative" to creativity,
                    "Leader" to leadership,
                    "Communicator" to communication
                )
                val topSkillText = ratings.maxByOrNull { it.value }?.key ?: student.skillTag

                student.copy(
                    academicScores = AcademicScores(math, science, english, history, art),
                    skillRatings = SkillRatings(creativity, leadership, communication, discipline, activity),
                    remarks = remarks,
                    skillTag = topSkillText
                )
            } else {
                student
            }
        }
    }

    // Calculating dashboard stats dynamically!
    fun getTotalStudentsCount(): Int = _students.value.size

    fun getAverageAcademicScore(): Float {
        if (_students.value.isEmpty()) return 0f
        val sum = _students.value.sumOf { it.academicScores.getAverage().toDouble() }
        return (sum / _students.value.size).toFloat()
    }

    // Dynamic Top Skill category determination based on averages of all students
    fun getTopSkillCategory(): String {
        if (_students.value.isEmpty()) return "N/A"
        val totalCreativity = _students.value.sumOf { it.skillRatings.creativity }
        val totalLeadership = _students.value.sumOf { it.skillRatings.leadership }
        val totalCommunication = _students.value.sumOf { it.skillRatings.communication }
        val totalDiscipline = _students.value.sumOf { it.skillRatings.discipline }
        val totalActivity = _students.value.sumOf { it.skillRatings.activity }

        val skills = mapOf(
            "Creativity" to totalCreativity,
            "Leadership" to totalLeadership,
            "Communication" to totalCommunication,
            "Discipline" to totalDiscipline,
            "Activity" to totalActivity
        )
        return skills.maxByOrNull { it.value }?.key ?: "N/A"
    }

    // Dynamic Weak Area category determination based on averages of all students
    fun getWeakAreaCategory(): String {
        if (_students.value.isEmpty()) return "N/A"
        val totalCreativity = _students.value.sumOf { it.skillRatings.creativity }
        val totalLeadership = _students.value.sumOf { it.skillRatings.leadership }
        val totalCommunication = _students.value.sumOf { it.skillRatings.communication }
        val totalDiscipline = _students.value.sumOf { it.skillRatings.discipline }
        val totalActivity = _students.value.sumOf { it.skillRatings.activity }

        val skills = mapOf(
            "Creativity" to totalCreativity,
            "Leadership" to totalLeadership,
            "Communication" to totalCommunication,
            "Discipline" to totalDiscipline,
            "Activity" to totalActivity
        )
        return skills.minByOrNull { it.value }?.key ?: "N/A"
    }
}
