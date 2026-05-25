package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Student
import com.example.ui.components.CustomButton
import com.example.ui.theme.AccentWarning
import com.example.ui.theme.TextMuted
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    student: Student?,
    onSaveClick: (
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
    ) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Hold local state for form inputs initialized from existing student values
    var mathText by remember { mutableStateOf(student?.academicScores?.math?.toString() ?: "85") }
    var scienceText by remember { mutableStateOf(student?.academicScores?.science?.toString() ?: "85") }
    var englishText by remember { mutableStateOf(student?.academicScores?.english?.toString() ?: "85") }
    var historyText by remember { mutableStateOf(student?.academicScores?.history?.toString() ?: "85") }
    var artText by remember { mutableStateOf(student?.academicScores?.art?.toString() ?: "85") }

    var creativityValue by remember { mutableFloatStateOf(student?.skillRatings?.creativity?.toFloat() ?: 3f) }
    var leadershipValue by remember { mutableFloatStateOf(student?.skillRatings?.leadership?.toFloat() ?: 3f) }
    var communicationValue by remember { mutableFloatStateOf(student?.skillRatings?.communication?.toFloat() ?: 3f) }
    var disciplineValue by remember { mutableFloatStateOf(student?.skillRatings?.discipline?.toFloat() ?: 3f) }
    var activityValue by remember { mutableFloatStateOf(student?.skillRatings?.activity?.toFloat() ?: 3f) }

    var remarksText by remember { mutableStateOf(student?.remarks ?: "") }

    // Validation checks
    val isMathValid = mathText.toIntOrNull() in 0..100
    val isScienceValid = scienceText.toIntOrNull() in 0..100
    val isEnglishValid = englishText.toIntOrNull() in 0..100
    val isHistoryValid = historyText.toIntOrNull() in 0..100
    val isArtValid = artText.toIntOrNull() in 0..100

    val isFormValid = isMathValid && isScienceValid && isEnglishValid && isHistoryValid && isArtValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Evaluation",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (student == null) {
            Text(
                text = "Load Failure: select valid profile.",
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .verticalScroll(scrollState)
                    .padding(20.dp)
            ) {
                // Header Label Info
                Text(
                    text = "Evaluating: ${student.name} (${student.className})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Column Section 1: Academic Scores Inputs
                Text(
                    text = "ACADEMIC SCORE INPUTS (0 - 100)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ScoreInputField(
                                value = mathText,
                                onValueChange = { mathText = it },
                                label = "Mathematics",
                                isValid = isMathValid,
                                modifier = Modifier.weight(1f)
                            )
                            ScoreInputField(
                                value = scienceText,
                                onValueChange = { scienceText = it },
                                label = "Science",
                                isValid = isScienceValid,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ScoreInputField(
                                value = englishText,
                                onValueChange = { englishText = it },
                                label = "English",
                                isValid = isEnglishValid,
                                modifier = Modifier.weight(1f)
                            )
                            ScoreInputField(
                                value = historyText,
                                onValueChange = { historyText = it },
                                label = "History",
                                isValid = isHistoryValid,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        ScoreInputField(
                            value = artText,
                            onValueChange = { artText = it },
                            label = "Art & Design",
                            isValid = isArtValid,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 2: Soft Skill Ratings Sliders
                Text(
                    text = "SOFT SKILL INDICATORS (1 - 5)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RatingSliderField(label = "Creativity", value = creativityValue, onValueChange = { creativityValue = it })
                        RatingSliderField(label = "Leadership", value = leadershipValue, onValueChange = { leadershipValue = it })
                        RatingSliderField(label = "Communication", value = communicationValue, onValueChange = { communicationValue = it })
                        RatingSliderField(label = "Discipline", value = disciplineValue, onValueChange = { disciplineValue = it })
                        RatingSliderField(label = "Activity", value = activityValue, onValueChange = { activityValue = it })
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Section 3: Evaluation Remarks
                Text(
                    text = "REMARKS & PROGRESSION COMMENTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    OutlinedTextField(
                        value = remarksText,
                        onValueChange = { remarksText = it },
                        placeholder = { Text("E.g., Exhibits remarkable academic concentration. Active team focus...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(110.dp),
                        singleLine = false,
                        maxLines = 5,
                        shape = RoundedCornerShape(16.dp), // modern high rounded text field
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Save button
                CustomButton(
                    text = "Save Evaluation",
                    enabled = isFormValid,
                    onClick = {
                        if (isFormValid) {
                            onSaveClick(
                                mathText.toInt(),
                                scienceText.toInt(),
                                englishText.toInt(),
                                historyText.toInt(),
                                artText.toInt(),
                                creativityValue.roundToInt(),
                                leadershipValue.roundToInt(),
                                communicationValue.roundToInt(),
                                disciplineValue.roundToInt(),
                                activityValue.roundToInt(),
                                remarksText
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ScoreInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            // Only allow numerical digits
            if (it.all { char -> char.isDigit() } || it.isEmpty()) {
                onValueChange(it)
            }
        },
        label = { Text(label) },
        isError = !isValid,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun RatingSliderField(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${value.roundToInt()}/5",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 1f..5f,
            steps = 3, // creates discrete values 1, 2, 3, 4, 5
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                activeTickColor = Color.White,
                inactiveTickColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
