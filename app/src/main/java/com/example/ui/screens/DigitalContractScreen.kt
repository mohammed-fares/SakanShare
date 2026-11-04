package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import com.example.R
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal

@Composable
fun DigitalContractScreen(
    bookingId: Long,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(Screen.MyBookings)
    }

    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()

    var agreedToLegalTerms by remember { mutableStateOf(false) }
    var agreedToHouseRules by remember { mutableStateOf(false) }
    var signerName by remember { mutableStateOf(currentUser?.name ?: "أحمد محمود العوضي") }
    var isSignedSuccessfully by remember { mutableStateOf(false) }

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
                        onClick = { viewModel.navigateTo(Screen.MyBookings) },
                        modifier = Modifier.testTag("contract_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "العقد الإلكتروني الموحد لسكن شير",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "طبقا لقوانين الإيجار وإثبات الهوية الرقمية المصرية",
                            color = Color(0xFFB0BEC5),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (!agreedToLegalTerms || !agreedToHouseRules) {
                                Toast.makeText(context, "يرجى الموافقة على الشروط القانونية وقواعد المنزل أولاً", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            viewModel.signDigitalContract(bookingId) {
                                isSignedSuccessfully = true
                                Toast.makeText(context, "تم توقيع العقد بنجاح وتفعيله رقمياً!", Toast.LENGTH_LONG).show()
                            }
                        },
                        enabled = agreedToLegalTerms && agreedToHouseRules && !isSignedSuccessfully,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryTeal,
                            disabledContainerColor = Color(0xFFCBD5E1)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_contract_signature_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isSignedSuccessfully) Icons.Default.CheckCircle else Icons.Default.Draw,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isSignedSuccessfully) "تم التوقيع الإلكتروني وتفعيل الحجز ✓" else "التوقيع الإلكتروني الفوري وتفعيل الحجز",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    if (isSignedSuccessfully) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.navigateTo(Screen.MyBookings) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BedVacantGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("goto_my_bookings_button")
                        ) {
                            Text("الانتقال إلى شاشة حجوزاتي", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("digital_contract_lazy_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Certificate Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BedVacantGreen)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.sakanshare_brand_icon),
                            contentDescription = "SakanShare",
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .padding(2.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "عقد إيجار وحدة سكنية مشتركة بنظام سكن شير",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = "رقم المعاملة: #SKN-2026-${bookingId} • يخضع للقانون المدني المصري",
                                fontSize = 11.sp,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }
            }

            // Legal Clauses
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "أولاً: الشروط والأحكام القانونية الملزمة",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        ContractClause(
                            num = "١",
                            title = "طبيعة التعاقد:",
                            body = "يقر المستأجر بأن هذا العقد هو عقد إيجار محدد المدة لوحدة سكنية مشتركة (سرير/غرفة) عبر منصة سكن شير الإلكترونية، ولا يمنحه هذا الحجز أي حق قانوني في امتداد الإيجار تلقائياً دون موافقة المنصة وسداد القيمة إلكترونياً مسبقاً."
                        )

                        ContractClause(
                            num = "٢",
                            title = "صحة البيانات الشخصية:",
                            body = "يشهد المستأجر بأن جميع المستندات المرفوعة (بطاقة الرقم القومي المصرية / إثبات القيد الجامعي / خطاب الموارد البشرية HR Letter ومفردات المرتب) صحيحة وسارية، ويتحمل المسؤولية القانونية الكاملة أمام الجهات الرسمية في حال تقديم أي مستند غير صحيح."
                        )

                        ContractClause(
                            num = "٣",
                            title = "سياسة التأمين المسترد:",
                            body = "يتم احتجاز مبلغ التأمين إلكترونياً كأمانة رقمية مشفرة، ويتم رده للمحفظة الإلكترونية خلال 48 ساعة بعد مغادرة السكن وفحص الوحدة والتأكد من سلامتها من التلفيات الناتجة عن سوء الاستخدام."
                        )

                        ContractClause(
                            num = "٤",
                            title = "سياسة الإخلاء السريع:",
                            body = "للمنصة الحق المطلق في إلغاء الحجز وإخلاء المستأجر فوراً دون الحاجة لإنذار قضائي في حال ارتكاب أي مخالفة جسيمة للنظام العام أو الآداب العامة، أو في حال التأخر عن سداد الإيجار لمدة تتجاوز 48 ساعة."
                        )
                    }
                }
            }

            // House Rules
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "ثانياً: قواعد المنزل السلوكية (House Rules)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PrimaryDarkTeal
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        RuleBullet(
                            title = "سياسة الزوار:",
                            text = "يمنع منعاً باتاً استضافة الزوار أو الأقارب داخل الغرف المشتركة أو الشقة للحفاظ على خصوصية وأمن المساكنين الآخرين."
                        )

                        RuleBullet(
                            title = "حظر التدخين والممنوعات:",
                            text = "التدخين بجميع أنواعه محظور تماماً داخل الغرف أو المساحات المشتركة (المطبخ والصالة والحمام)، كما يُمنع منعاً باتاً حيازة أو تعاطي أي مواد كحولية أو مخدرة داخل الشقة، ويعرض صاحبها للطرد الفوري."
                        )

                        RuleBullet(
                            title = "الهدوء ومراعاة الآخرين:",
                            text = "يجب الحفاظ على الهدوء التام ومنع الأصوات المرتفعة في الفترة من الساعة 11 مساءً وحتى الساعة 8 صباحاً."
                        )

                        RuleBullet(
                            title = "النظافة والمرافق المشتركة:",
                            text = "يلتزم الساكن بنظافة سريره وغرفته وترك المساحات المشتركة (المطبخ بعد إعداد الطعام، الحمام بعد الاستخدام) نظيفة تماماً."
                        )

                        RuleBullet(
                            title = "استهلاك الطاقة والعدادات الذكية:",
                            text = "يتم تقسيم تكلفة شحن كروت العدادات الذكية (الكهرباء، الغاز، المياه) بالتساوي بين قاطني الشقة عبر المحفظة الإلكترونية للتطبيق."
                        )
                    }
                }
            }

            // Agreement Checkboxes
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { agreedToLegalTerms = !agreedToLegalTerms },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToLegalTerms,
                            onCheckedChange = { agreedToLegalTerms = it },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryTeal)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "أوافق وأقر بجميع الشروط والأحكام القانونية لعقد الإيجار الموحد",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { agreedToHouseRules = !agreedToHouseRules },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToHouseRules,
                            onCheckedChange = { agreedToHouseRules = it },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryTeal)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "أتعهد بالالتزام التام بقواعد المنزل السلوكية ونظام تقسيم العدادات",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Electronic Signature Canvas Placeholder / Name
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryTeal.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "التوقيع الرقمي المعتمد:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = signerName,
                            onValueChange = { signerName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("contract_signer_name"),
                            label = { Text("الاسم الثلاثي للمستأجر الموقع") },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFF1F5F9))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "✍️ توقيع رقمي معتمد: $signerName (IP: 197.38.12.92)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryTeal
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContractClause(num: String, title: String, body: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = PrimaryTeal.copy(alpha = 0.12f)
            ) {
                Text(
                    text = num,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = PrimaryTeal,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = body,
            fontSize = 11.sp,
            lineHeight = 17.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RuleBullet(title: String, text: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text("•", fontWeight = FontWeight.Bold, color = AccentGold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text, fontSize = 11.sp, lineHeight = 17.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
