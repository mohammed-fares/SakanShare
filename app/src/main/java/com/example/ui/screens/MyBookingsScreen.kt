package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.BookingEntity
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyBookingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookings by viewModel.userBookings.collectAsState()
    val properties by viewModel.allProperties.collectAsState()

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("ar", "EG"))

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_bookings_lazy_column"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "سجل الحجوزات والإقامات",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "متابعة الحجوزات النشطة، العقد الموحد، وأولوية التجديد قبل 7 أيام",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 7-day auto-renewal priority notification banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD97706)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "تنبيه الأولوية الذكية للتجديد (Auto-Renewal)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF78350F)
                        )
                        Text(
                            text = "يمنحك النظام أولوية تجديد الحجز الحصري قبل 7 أيام من انتهاء الإقامة قبل طرح السرير للعامة.",
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            color = Color(0xFF92400E)
                        )
                    }
                }
            }
        }

        if (bookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bed,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "لا توجد حجوزات نشطة حالياً",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "استكشف الوحدات المتاحة واحجز سريرك الآن بنظام المشاركة الفندقية",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.navigateTo(Screen.Explore) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
                        ) {
                            Text("استكشف الأسرة المتاحة", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(bookings) { booking ->
                val prop = properties.firstOrNull { it.id == booking.propertyId }
                BookingCardItem(
                    booking = booking,
                    propertyTitle = prop?.title ?: "سكن شير ريزيدنس",
                    city = prop?.cityAr ?: "القاهرة الجديدة",
                    dateFormat = dateFormat,
                    onViewContract = {
                        viewModel.navigateTo(Screen.DigitalContract(booking.id))
                    },
                    onRenew = {
                        viewModel.renewBooking(booking, 1) {
                            Toast.makeText(context, "تم تجديد الحجز لشهر إضافي بنجاح بموجب حق الأولوية!", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BookingCardItem(
    booking: BookingEntity,
    propertyTitle: String,
    city: String,
    dateFormat: SimpleDateFormat,
    onViewContract: () -> Unit,
    onRenew: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_card_${booking.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = propertyTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "المدينة: $city • رقم الحجز: #SKN-${booking.id}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (booking.status == "confirmed") Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                ) {
                    Text(
                        text = if (booking.status == "confirmed") "حجز ساري ومؤكد ✓" else "قيد المراجعة",
                        color = if (booking.status == "confirmed") Color(0xFF15803D) else Color(0xFFB45309),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dates & Duration Row
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("تاريخ البدء", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(dateFormat.format(Date(booking.startDate)), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("تاريخ الانتهاء", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(dateFormat.format(Date(booking.endDate)), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryTeal)
                    }

                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("طريقة الدفع", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(booking.paymentMethod, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Refundable Deposit Tracker Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "حالة التأمين المسترد: ${booking.depositRefundStatus}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF166534)
                        )
                        Text(
                            text = "قيمة التأمين: %,.0f ج.م • يسترد للمحفظة فور المغادرة بعد فحص الغرفة".format(booking.depositAmount),
                            fontSize = 10.sp,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewContract,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("view_contract_button_${booking.id}")
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("عرض العقد الموحد", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onRenew,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .testTag("renew_priority_button_${booking.id}")
                ) {
                    Icon(Icons.Default.Autorenew, contentDescription = null, tint = PrimaryDarkTeal, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("أولوية التجديد", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryDarkTeal)
                }
            }
        }
    }
}
