package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.BedEntity
import com.example.data.local.entity.RoomEntity
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BedBookedRed
import com.example.ui.theme.BedPendingYellow
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryTeal

@Composable
fun BedMatrixComponent(
    rooms: List<RoomEntity>,
    beds: List<BedEntity>,
    selectedBedId: Long?,
    onBedSelected: (BedEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "روزنامة الأسرة اللحظية (Bed Matrix)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = PrimaryTeal.copy(alpha = 0.1f)
            ) {
                Text(
                    text = "حجز مباشر بدون وسيط",
                    fontSize = 11.sp,
                    color = PrimaryTeal,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        BedStatusLegend()

        if (rooms.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("جاري تحميل مصفوفة الأسرة...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            rooms.forEach { room ->
                val roomBeds = beds.filter { it.roomId == room.id }
                RoomBedGroupCard(
                    room = room,
                    beds = roomBeds,
                    selectedBedId = selectedBedId,
                    onBedSelected = onBedSelected
                )
            }
        }
    }
}

@Composable
fun RoomBedGroupCard(
    room: RoomEntity,
    beds: List<BedEntity>,
    selectedBedId: Long?,
    onBedSelected: (BedEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("room_card_${room.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Room Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PrimaryTeal.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MeetingRoom,
                            contentDescription = null,
                            tint = PrimaryTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = room.roomNumber,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = room.roomTypeAr,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (room.hasPrivateBathroom) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bathtub,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "حمام ماستر خاص",
                                fontSize = 10.sp,
                                color = Color(0xFF2563EB),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Beds List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                beds.forEach { bed ->
                    val isSelected = selectedBedId == bed.id
                    val isVacant = bed.currentStatus == "vacant"
                    val isPending = bed.currentStatus == "pending"
                    val isBooked = bed.currentStatus == "booked"

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) PrimaryTeal else if (isVacant) BedVacantGreen.copy(alpha = 0.4f) else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .background(
                                if (isSelected) PrimaryTeal.copy(alpha = 0.08f)
                                else if (isBooked) Color(0xFFF8FAFC)
                                else Color.White
                            )
                            .clickable(enabled = isVacant) {
                                onBedSelected(bed)
                            }
                            .testTag("bed_item_${bed.id}"),
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Status Circle
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isSelected -> PrimaryTeal
                                                isVacant -> BedVacantGreen.copy(alpha = 0.2f)
                                                isPending -> BedPendingYellow.copy(alpha = 0.2f)
                                                else -> BedBookedRed.copy(alpha = 0.2f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else if (isBooked) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = BedBookedRed,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(if (isVacant) BedVacantGreen else BedPendingYellow)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = bed.bedLabel,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isBooked) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = when {
                                            isVacant -> "شاغر ومتاح للحجز الفوري"
                                            isPending -> "معلق (قفل السرير لمدة 10 د لمنع التكرار)"
                                            else -> "محجوز حالياً بواسطة ساكن آخر"
                                        },
                                        fontSize = 10.sp,
                                        color = when {
                                            isVacant -> BedVacantGreen
                                            isPending -> BedPendingYellow
                                            else -> BedBookedRed
                                        },
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Price column
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "%,.0f ج.م".format(bed.pricePerMonth),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = if (isBooked) Color(0xFF94A3B8) else PrimaryTeal
                                )
                                Text(
                                    text = "شهرياً (أو %,.0f ج/يوم)".format(bed.pricePerDay),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
