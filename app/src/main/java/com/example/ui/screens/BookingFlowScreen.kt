package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BedPendingYellow
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal

@Composable
fun BookingFlowScreen(
    propertyId: Long,
    bedId: Long,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val property by viewModel.selectedProperty.collectAsState()
    val beds by viewModel.beds.collectAsState()
    val bed = beds.firstOrNull { it.id == bedId }
    val holdSeconds by viewModel.holdSecondsLeft.collectAsState()

    var rentalType by remember { mutableStateOf("monthly") } // daily, weekly, monthly, semesterly, yearly
    var durationUnits by remember { mutableIntStateOf(1) }
    var selectedPaymentMethod by remember { mutableStateOf("InstaPay") }
    var isDepositInstallment by remember { mutableStateOf(false) }
    var installmentProvider by remember { mutableStateOf("ValU") } // ValU, Sympl, Aman
    var installmentMonths by remember { mutableIntStateOf(6) }

    // Format hold timer: MM:SS
    val minutes = holdSeconds / 60
    val seconds = holdSeconds % 60
    val formattedTime = "%02d:%02d".format(minutes, seconds)

    val monthlyRate = bed?.pricePerMonth ?: 2500.0
    val weeklyRate = bed?.pricePerWeek ?: 800.0
    val dailyRate = bed?.pricePerDay ?: 150.0
    val baseDeposit = bed?.depositAmount ?: monthlyRate

    val calculatedRent = when (rentalType) {
        "daily" -> dailyRate * durationUnits
        "weekly" -> weeklyRate * durationUnits
        "monthly" -> monthlyRate * durationUnits
        "semesterly" -> monthlyRate * 4 * durationUnits * 0.95
        "yearly" -> monthlyRate * 12 * durationUnits * 0.90
        else -> monthlyRate
    }

    val upfrontDeposit = if (isDepositInstallment) (baseDeposit / installmentMonths) else baseDeposit
    val grandTotal = calculatedRent + upfrontDeposit

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = PrimaryDarkTeal,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("booking_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "تأكيد حجز السرير والدفع",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = property?.title ?: "سكن شير",
                            color = Color(0xFFB0BEC5),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("booking_bottom_bar"),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "المبلغ المطلوب سداده الآن:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "%,.0f ج.م".format(grandTotal),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = PrimaryTeal
                            )
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.confirmBooking(
                                propertyId = propertyId,
                                bedId = bedId,
                                rentalType = rentalType,
                                durationUnits = durationUnits,
                                paymentMethod = selectedPaymentMethod,
                                isInstallment = isDepositInstallment,
                                installmentProvider = installmentProvider,
                                installmentMonths = installmentMonths,
                                onSuccess = { bookingId ->
                                    Toast.makeText(context, "تم قفل الحجز بنجاح! انتقل للتوقيع الإلكتروني", Toast.LENGTH_SHORT).show()
                                    viewModel.navigateTo(Screen.DigitalContract(bookingId))
                                }
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("confirm_and_sign_button")
                    ) {
                        Text("متابعة والتوقيع الإلكتروني ✍️", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("booking_flow_lazy_column"),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Double Booking Prevention Lock Countdown Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BedPendingYellow)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "قفل السرير حصرياً لك لمنع الحجز المزدوج",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F)
                                )
                                Text(
                                    text = "السرير محجوز لك برمجياً ومحمي من أي طلبات متزامنة.",
                                    fontSize = 10.sp,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFB45309)
                        ) {
                            Text(
                                text = formattedTime,
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. Selected Bed Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "تفاصيل السرير المختار",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = bed?.bedLabel ?: "سرير فردي فندقي",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = PrimaryTeal
                                )
                                Text(
                                    text = property?.title ?: "",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = BedVacantGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "جاهز للتسكين الفوري",
                                    color = BedVacantGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Rental Duration Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "نظام ومدة الإيجار (Rental System)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Duration Tabs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            DurationPill(label = "شهري", isSelected = rentalType == "monthly", onClick = { rentalType = "monthly" })
                            DurationPill(label = "فصلي (4ش)", isSelected = rentalType == "semesterly", onClick = { rentalType = "semesterly" })
                            DurationPill(label = "سنوي (خصم 10%)", isSelected = rentalType == "yearly", onClick = { rentalType = "yearly" })
                            DurationPill(label = "أسبوعي", isSelected = rentalType == "weekly", onClick = { rentalType = "weekly" })
                            DurationPill(label = "يومي", isSelected = rentalType == "daily", onClick = { rentalType = "daily" })
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Duration Units Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (rentalType) {
                                    "daily" -> "عدد الأيام:"
                                    "weekly" -> "عدد الأسابيع:"
                                    "monthly" -> "عدد الأشهر:"
                                    "semesterly" -> "عدد الفصول الدراسية:"
                                    else -> "عدد السنوات:"
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { if (durationUnits > 1) durationUnits -= 1 },
                                    modifier = Modifier.size(34.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("-", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "$durationUnits",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                OutlinedButton(
                                    onClick = { durationUnits += 1 },
                                    modifier = Modifier.size(34.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                                ) {
                                    Text("+", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Flexible Deposit & Consumer Finance Installments
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = AccentGold, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "التأمين المرن المسترد (%,.0f ج.م)".format(baseDeposit),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "يسترد بالكامل خلال 48 ساعة بعد فحص المغادرة",
                                        fontSize = 10.sp,
                                        color = BedVacantGreen,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Switch for Installments (ValU, Sympl, Aman)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "تقسيط مبلغ التأمين (تخفيف العبء المالي)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = PrimaryDarkTeal
                                )
                                Text(
                                    text = "شراكة رسمية مع شركات التمويل الاستهلاكي (ValU / Sympl / أمان)",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isDepositInstallment,
                                onCheckedChange = { isDepositInstallment = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = PrimaryTeal, checkedTrackColor = Color(0xFFBAE6FD))
                            )
                        }

                        if (isDepositInstallment) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("اختر مزود التقسيط وعدد الأشهر:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("ValU", "Sympl", "أمان").forEach { provider ->
                                    val isSelected = installmentProvider == provider
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) PrimaryTeal else Color(0xFFF1F5F9),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { installmentProvider = provider }
                                    ) {
                                        Box(modifier = Modifier.padding(vertical = 8.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = provider,
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(3, 6, 12).forEach { m ->
                                    val isSelected = installmentMonths == m
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) AccentGold else Color(0xFFF1F5F9),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { installmentMonths = m }
                                    ) {
                                        Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "$m أشهر (%,.0f ج/ش)".format(baseDeposit / m),
                                                color = if (isSelected) PrimaryDarkTeal else MaterialTheme.colorScheme.onSurface,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. Payment Gateways (Fawry, InstaPay, Wallets, Cards)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "طريقة الدفع المعتمدة (Payment Gateway)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        PaymentOptionRow(
                            title = "منظومة إنستا باي (InstaPay)",
                            subtitle = "سداد لحظي مباشر عبر حساب إنستا باي أو QR Code",
                            icon = Icons.Default.QrCode,
                            isSelected = selectedPaymentMethod == "InstaPay",
                            onClick = { selectedPaymentMethod = "InstaPay" }
                        )

                        PaymentOptionRow(
                            title = "شبكة فوري (Fawry Pay)",
                            subtitle = "كود سداد فوري صالح لمدة 10 دقائق من أي منفذ",
                            icon = Icons.Default.AccountBalance,
                            isSelected = selectedPaymentMethod == "Fawry",
                            onClick = { selectedPaymentMethod = "Fawry" }
                        )

                        PaymentOptionRow(
                            title = "المحافظ الإلكترونية (Mobile Wallets)",
                            subtitle = "فودافون كاش، أورنج كاش، اتصالات، وي باي",
                            icon = Icons.Default.PhoneAndroid,
                            isSelected = selectedPaymentMethod == "Wallets",
                            onClick = { selectedPaymentMethod = "Wallets" }
                        )

                        PaymentOptionRow(
                            title = "بطاقات الائتمان وميزة (Credit/Debit / Meeza)",
                            subtitle = "فيزا، ماستركارد، كارت ميزة الوطني",
                            icon = Icons.Default.CreditCard,
                            isSelected = selectedPaymentMethod == "Cards",
                            onClick = { selectedPaymentMethod = "Cards" }
                        )
                    }
                }
            }

            // 6. Cost Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("ملخص الفاتورة والحجز:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("قيمة الإيجار للمدة المحددة:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("%,.0f ج.م".format(calculatedRent), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                if (isDepositInstallment) "القسط الأول للتأمين المسترد ($installmentProvider):" else "مبلغ التأمين المسترد بالكامل:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text("%,.0f ج.م".format(upfrontDeposit), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AccentGold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("الخدمات الفندقية والنظافة الأسبوعية:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("مشمولة مجاناً ✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BedVacantGreen)
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("الإجمالي المستحق للسداد:", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            Text("%,.0f ج.م".format(grandTotal), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryTeal)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DurationPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) PrimaryTeal else Color(0xFFF1F5F9),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun PaymentOptionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = PrimaryTeal)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) PrimaryTeal else Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
