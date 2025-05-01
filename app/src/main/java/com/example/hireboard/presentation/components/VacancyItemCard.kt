package com.example.hireboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hireboard.ui.theme.HireBoardTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Vacancy(
    val id: Long,
    val title: String,
    val companyName: String,
    val description: String,
    val postedAt: Long,
    val isActive: Boolean,
    val publisherId: Long,
    val logoUrl: String? = null
)

@Composable
fun VacancyItemCard(
    vacancy: Vacancy,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vacancy.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = vacancy.companyName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Опубликовано: ${formatDate(vacancy.postedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            if (!vacancy.logoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(vacancy.logoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${vacancy.companyName} logo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getInitials(vacancy.companyName),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

private fun getInitials(name: String): String {
    return name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString()?.uppercase() }
        .take(2)
        .joinToString("")
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun VacancyItemCardPreviewLight() {
    val vacancyWithLogo = Vacancy(
        id = 1L,
        title = "Android Developer",
        companyName = "JetBrains",
        description = "Требуется разработчик Android",
        postedAt = System.currentTimeMillis(),
        isActive = true,
        publisherId = 1L,
        logoUrl = null
    )

    HireBoardTheme(darkTheme = false) {
        VacancyItemCard(
            vacancy = vacancyWithLogo,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Theme")
@Composable
fun VacancyItemCardPreviewDark() {
    val vacancyWithLogo = Vacancy(
        id = 1L,
        title = "Android Developer",
        companyName = "JetBrains",
        description = "Требуется разработчик Android",
        postedAt = System.currentTimeMillis(),
        isActive = true,
        publisherId = 1L,
        logoUrl = null
    )

    HireBoardTheme(darkTheme = true) {
        VacancyItemCard(
            vacancy = vacancyWithLogo,
            onClick = {}
        )
    }
}