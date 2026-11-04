package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
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
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal

@Composable
fun OwnerDashboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.switchRole("tenant")
        viewModel.navigateTo(Screen.Home)
    }

    val context = LocalContext.current
    val properties by viewModel.allProperties.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("owner_dashboard_lazy_column"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Owner Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryDarkTeal)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "لوحة تحكم المالك والمؤجر",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                            }
                            Text(
                                text = "م. كريم الشناوي • مالك موثق بسجل تجاري وترخيص تأجير",
                                color = Color(0xFFB0BEC5),
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.switchRole("tenant")
                                viewModel.navigateTo(Screen.Home)
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentGold),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("وضع المستأجر 👤", color = PrimaryDarkTeal, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OwnerStatCard(title = "نسبة الإشغال", value = "87%", subtitle = "14 سرير مشغول من 16", modifier = Modifier.weight(1f))
                        OwnerStatCard(title = "الدخل الشهري", value = "48,500 ج", subtitle = "تم تحصيله إلكترونياً", modifier = Modifier.weight(1f))
                        OwnerStatCard(title = "العقارات النشطة", value = "${properties.size}", subtitle = "مدارة فندقياً", modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Subletting and Title Deed Status Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BedVacantGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "إثبات الملكية وترخيص التأجير من الباطن (Subletting)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFF166534)
                        )
                        Text(
                            text = "تم التحقق من صك الملكية وعقود الإدارة الفندقية بنجاح لدى الشؤون القانونية للمنصة.",
                            fontSize = 10.sp,
                            color = Color(0xFF15803D)
                        )
                    }
                }
            }
        }

        // Section: Departure Inspection & Deposit Release
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "طلبات مغادرة الساكنين وفحص التأمين المسترد",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "اعتماد تقرير سلامة الغرفة لرد التأمين إلى محفظة المستأجر خلال 48 ساعة",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                        Column {
                            Text(
                                text = "طلب مغادرة الساكن: أحمد محمود العوضي",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "شقة التجمع الخامس - غرفة 102 (سرير B)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFFEF3C7)) {
                            Text(
                                text = "فحص مكتمل ✓",
                                color = Color(0xFFB45309),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "تقرير مندوب المعاينة: تم فحص السرير والدولاب والتكييف والمفاتيح، الوحدة سليمة 100% ولا توجد أي تلفيات.",
                        fontSize = 11.sp,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "مبلغ التأمين المسترد: 2,800 ج.م",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp,
                            color = PrimaryTeal
                        )

                        Button(
                            onClick = {
                                viewModel.approveDepositRefund(1L, 1L, 2800.0) {
                                    Toast.makeText(context, "تمت الموافقة الفورية ورد مبلغ 2,800 ج.م لمحفظة المستأجر!", Toast.LENGTH_LONG).show()
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BedVacantGreen),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("approve_deposit_btn")
                        ) {
                            Text("اعتماد الإفراج عن التأمين", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Section: Managed Properties List
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "العقارات المدارة (${properties.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "إدراج شقة جديدة متاح عبر بوابة العقارات الفندقية", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إضافة وحدة", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(properties) { prop ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { viewModel.navigateTo(Screen.PropertyDetail(prop.id)) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryTeal.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Apartment, contentDescription = null, tint = PrimaryTeal)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = prop.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "${prop.cityAr} • نظام فندقي متكامل", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Surface(shape = RoundedCornerShape(6.dp), color = BedVacantGreen.copy(alpha = 0.15f)) {
                            Text("مشغول بنسبة 85%", color = BedVacantGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerStatCard(title: String, value: String, subtitle: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF132B36),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, color = Color(0xFF90A4AE), fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = AccentGold, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = Color(0xFFCFD8DC), fontSize = 8.sp, maxLines = 1)
        }
    }
}
