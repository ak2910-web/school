package com.example.vico

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentSuccess
import com.example.ui.theme.AccentWarning
import com.example.ui.theme.BluePrimary

@Composable
fun DashboardChartPlaceholder(
    academicScore: Float,
    modifier: Modifier = Modifier
) {
    // Standard color codes
    val primaryColor = MaterialTheme.colorScheme.primary
    val barColors = listOf(
        primaryColor,
        AccentSuccess,
        AccentPurple,
        AccentWarning,
        AccentCyan
    )

    val labelTextSize = 28f // For text rendering inside Canvas

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val maxBarHeight = height * 0.75f
        val barCount = 5
        val gap = width / (barCount * 3)
        val barWidth = (width - (gap * (barCount + 1))) / barCount

        val samplePercentages = listOf(0.85f, academicScore / 100f, 0.78f, 0.92f, 0.72f)
        val labels = listOf("Math", "Sci", "Eng", "Art", "Act")

        // Draw horizontal grid lines
        val linePaint = android.graphics.Paint().apply {
            color = Color.LightGray.copy(alpha = 0.3f).toArgb()
            strokeWidth = 2f
        }
        for (i in 1..4) {
            val yPos = maxBarHeight * (i / 4f)
            drawContext.canvas.nativeCanvas.drawLine(0f, yPos, width, yPos, linePaint)
        }

        // Draw standard bars
        for (i in 0 until barCount) {
            val percentage = samplePercentages[i]
            val actualHeight = maxBarHeight * percentage
            val xOffset = gap + i * (barWidth + gap)
            val yOffset = maxBarHeight - actualHeight

            // Draw shadow/track
            drawRoundRect(
                color = barColors[i].copy(alpha = 0.1f),
                topLeft = androidx.compose.ui.geometry.Offset(xOffset, 0f),
                size = Size(barWidth, maxBarHeight),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Draw filled bar
            drawRoundRect(
                color = barColors[i],
                topLeft = androidx.compose.ui.geometry.Offset(xOffset, yOffset),
                size = Size(barWidth, actualHeight),
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Draw category labels
            drawContext.canvas.nativeCanvas.drawText(
                labels[i],
                xOffset + (barWidth / 2) - 18f,
                height - 10f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = labelTextSize
                    isAntiAlias = true
                }
            )
        }
    }
}
