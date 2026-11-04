package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmokeFree
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import com.example.ui.theme.AccentGold
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal

@Composable
fun HouseRulesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

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
                        modifier = Modifier.testTag("rules_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "قواعد المنزل والأحكام القانونية",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "وثيقة السلوك الفندقي والتعايش المشترك",
                            color = Color(0xFFB0BEC5),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("house_rules_lazy_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "قواعد التعايش السلوكي (House Rules)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "قواعد صارمة لضمان راحة وسلامة جميع الشركاء والمغتربين",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.PersonOff,
                    title = "سياسة منع الزوار الأغراب:",
                    description = "يُمنع منعاً باتاً استضافة الزوار أو الأقارب أو الأصدقاء داخل الغرف المشتركة أو صالة الشقة، وذلك للحفاظ على خصوصية وأمن المساكنين الآخرين."
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.SmokeFree,
                    title = "حظر التدخين والممنوعات قطعيًا:",
                    description = "التدخين بجميع أشكاله (سجائر، فيب، شيشة) محظور تماماً داخل الغرف أو المساحات المشتركة (المطبخ، الصالة، الحمام)، كما يُمنع حيازة أو تعاطي أي مواد مخدرة أو كحولية، ويعرض مرتكبها للإخلاء الفوري وفسخ العقد."
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.NotificationsOff,
                    title = "ساعات الهدوء التام (11 م - 8 ص):",
                    description = "يجب الحفاظ على الهدوء التام ومنع الأصوات المرتفعة أو تشغيل السماعات بدون سماعات أذن خلال الفترة من 11:00 مساءً وحتى 8:00 صباحاً لتوفير مناخ مثالي للدراسة والنوم."
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.CleaningServices,
                    title = "نظافة المرافق المشتركة:",
                    description = "يلتزم الساكن بترك المطبخ نظيفاً ومغسول الأواني فور الانتهاء من إعداد طعامه، وتنظيف الحمام بعد استخدامه، مع الاستفادة من جدول النظافة الفندقية الأسبوعية المجدولة من سكن شير."
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.ElectricBolt,
                    title = "تقسيم استهلاك العدادات الذكية بالتساوي:",
                    description = "يتم شحن كروت العدادات مسبقة الدفع (الكهرباء، الغاز، المياه) وتقسيم الفاتورة آلياً بالتساوي بين جميع المساكنين عبر المحفظة الإلكترونية للتطبيق."
                )
            }

            item {
                RuleDetailCard(
                    icon = Icons.Default.Security,
                    title = "الإخلاء السريع ومخالفة النظام العام:",
                    description = "للمنصة الحق المطلق في إلغاء الحجز الفوري وإخلاء الوحدة عند ثبوت أي مخالفة جسيمة للنظام العام أو الآداب العامة، أو في حال التأخر عن سداد المستحقات لمدة تتجاوز 48 ساعة."
                )
            }
        }
    }
}

@Composable
private fun RuleDetailCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryTeal,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = PrimaryDarkTeal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
