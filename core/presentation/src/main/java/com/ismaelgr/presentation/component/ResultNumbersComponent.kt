package com.ismaelgr.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ismaelgr.domain.getDateFormated
import com.ismaelgr.domain.getFormatedToDate
import com.ismaelgr.presentation.toUIDate
import java.util.Date

@Composable
fun ResultNumbersComponent(
    modifier: Modifier = Modifier,
    date: String,
    numbers: List<Int>,
    winners: List<Int> = emptyList(),
    showDate: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = if (showDate) date.getFormatedToDate().toUIDate() else "$date       --->")

        Spacer(
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minWidth = 10.dp)
        )

        numbers.map { number ->
            val backgroundColor =
                if (winners.contains(number)) Color.Cyan.copy(alpha = 0.7f) else Color.Transparent

            Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    modifier = Modifier
                        .border(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp),
                            width = 2.dp
                        )
                        .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                        .padding(7.dp),
                    text = processNumber(number),
                    color = Color.White,
                )
            }
        }
    }
}

private fun processNumber(number: Int): String {
    return if (number == 0) " ? " else if (number < 10) "0$number" else number.toString()
}

@Composable
@Preview
private fun Preview() {
    ResultNumbersComponent(
        numbers = listOf(10, 22, 3, 14, 5, 9, 0),
        date = Date().getDateFormated(),
        winners = listOf(3)
    )
}