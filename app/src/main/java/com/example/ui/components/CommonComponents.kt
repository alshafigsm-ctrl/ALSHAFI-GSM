package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CasePriority
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GreenSuccess
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.NavySecondary
import com.example.ui.theme.RedUrgent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChambersTopAppBar(
    title: String = "Rashid Mukhtiar Advocate",
    subtitle: String = "Advocate High Court & Legal Chambers",
    onCallClick: () -> Unit,
    onShareAppClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GoldBright),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = "Gavel",
                        tint = NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(10.dp))
                androidx.compose.foundation.layout.Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = GoldAccent
                        )
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onCallClick,
                modifier = Modifier.testTag("action_call_chambers")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call Chambers",
                    tint = GoldBright
                )
            }
            IconButton(
                onClick = onShareAppClick,
                modifier = Modifier.testTag("action_share_app")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NavyPrimary,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun PriorityBadge(priority: CasePriority) {
    val (bg, textColor, label) = when (priority) {
        CasePriority.URGENT -> Triple(RedUrgent.copy(alpha = 0.15f), RedUrgent, "URGENT")
        CasePriority.HIGH -> Triple(GoldBright.copy(alpha = 0.18f), GoldBright, "HIGH")
        CasePriority.NORMAL -> Triple(Color.Gray.copy(alpha = 0.15f), Color.LightGray, "REGULAR")
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun CourtBadge(courtName: String) {
    val isHighCourt = courtName.contains("High Court", ignoreCase = true)
    val bg = if (isHighCourt) NavySecondary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (isHighCourt) GoldBright else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = courtName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color = GoldBright,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
