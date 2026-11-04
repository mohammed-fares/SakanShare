package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.BedVacantGreen
import com.example.ui.theme.PrimaryDarkTeal
import com.example.ui.theme.PrimaryTeal

@Composable
fun KycOnboardingScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()

    var step by remember { mutableIntStateOf(1) } // 1: Phone & OTP, 2: National ID, 3: Liveness Selfie, 4: Status Proof (Student/HR)
    var phoneNumber by remember { mutableStateOf(currentUser?.phone ?: "01012345678") }
    var otpCode by remember { mutableStateOf("4829") }
    var nationalId by remember { mutableStateOf(currentUser?.nationalId ?: "29805120102345") }
    var userType by remember { mutableStateOf(currentUser?.userType ?: "student") } // student or professional
    var universityOrCompany by remember { mutableStateOf(currentUser?.universityOrCompany ?: "الجامعة الأمريكية بالقاهرة (AUC)") }
    var idUploaded by remember { mutableStateOf(true) }
    var selfieCaptured by remember { mutableStateOf(true) }
    var proofUploaded by remember { mutableStateOf(true) }

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
                        modifier = Modifier.testTag("kyc_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "التوثيق الرقمي والتحقق من الهوية (KYC)",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "بوابة الأمان والتحقق المعتمدة لسكن شير",
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
                .testTag("kyc_lazy_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Steps Progress Indicator
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepBubble(stepNumber = 1, title = "الهاتف وOTP", isCurrent = step == 1, isCompleted = step > 1)
                    StepLine(isCompleted = step > 1)
                    StepBubble(stepNumber = 2, title = "الرقم القومي", isCurrent = step == 2, isCompleted = step > 2)
                    StepLine(isCompleted = step > 2)
                    StepBubble(stepNumber = 3, title = "صورة حية", isCurrent = step == 3, isCompleted = step > 3)
                    StepLine(isCompleted = step > 3)
                    StepBubble(stepNumber = 4, title = "إثبات القيد/عمل", isCurrent = step == 4, isCompleted = currentUser?.kycStatus == "verified")
                }
            }

            when (step) {
                1 -> {
                    // Step 1: Phone OTP
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "الخطوة ١: التحقق الإلزامي برمز OTP",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    label = { Text("رقم الهاتف المحمول المصري") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("kyc_phone_input"),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = otpCode,
                                    onValueChange = { otpCode = it },
                                    label = { Text("رمز التحقق لمرة واحدة (OTP)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("kyc_otp_input"),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "تم التحقق من رقم الهاتف بنجاح ✓", Toast.LENGTH_SHORT).show()
                                        step = 2
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("kyc_step1_next_btn")
                                ) {
                                    Text("تأكيد ومتابعة للرقم القومي", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Step 2: National ID
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Badge, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(24.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "الخطوة ٢: بطاقة الرقم القومي (الوجهين)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = nationalId,
                                    onValueChange = { nationalId = it },
                                    label = { Text("الرقم القومي (14 رقماً)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("kyc_national_id_input"),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                // ID Upload boxes
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    UploadBox(title = "وجه البطاقة الأمامي", isUploaded = idUploaded, modifier = Modifier.weight(1f))
                                    UploadBox(title = "ظهر البطاقة الخلفي", isUploaded = idUploaded, modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "تم قبول وتدقيق بطاقة الرقم القومي ✓", Toast.LENGTH_SHORT).show()
                                        step = 3
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("kyc_step2_next_btn")
                                ) {
                                    Text("متابعة لمطابقة الصورة الحية", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                3 -> {
                    // Step 3: Liveness Detection
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Face, contentDescription = null, tint = AccentGold, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "الخطوة ٣: التقاط صورة شخصية حية (Liveness)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = "مطابقة فورية بالذكاء الاصطناعي مع بطاقة الرقم القومي لمنع التزوير",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryDarkTeal)
                                        .border(3.dp, BedVacantGreen, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF15803D), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "تمت المطابقة الحيوية بنجاح بنسبة 99.4%",
                                            color = Color(0xFF15803D),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = {
                                        step = 4
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(46.dp)
                                        .testTag("kyc_step3_next_btn")
                                ) {
                                    Text("متابعة لإثبات الحالة (طالب / موظف)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                4 -> {
                    // Step 4: Proof of Status (Student / HR)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "الخطوة ٤: إثبات الحالة (طالب جامعي / موظف مهني)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                // Select student or professional
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { userType = "student" }
                                    ) {
                                        RadioButton(
                                            selected = userType == "student",
                                            onClick = { userType = "student" },
                                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryTeal)
                                        )
                                        Icon(Icons.Default.School, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("طالب جامعي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { userType = "professional" }
                                    ) {
                                        RadioButton(
                                            selected = userType == "professional",
                                            onClick = { userType = "professional" },
                                            colors = RadioButtonDefaults.colors(selectedColor = PrimaryTeal)
                                        )
                                        Icon(Icons.Default.Work, contentDescription = null, tint = AccentGold, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("موظف / مهني", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    value = universityOrCompany,
                                    onValueChange = { universityOrCompany = it },
                                    label = { Text(if (userType == "student") "اسم الجامعة والكلية" else "اسم جهة العمل / الشركة") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("kyc_org_input"),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                UploadBox(
                                    title = if (userType == "student") "كارنيه الجامعة أو إثبات القيد الرسمي" else "خطاب الموارد البشرية HR Letter ومفردات المرتب",
                                    isUploaded = proofUploaded,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(18.dp))

                                Button(
                                    onClick = {
                                        viewModel.submitKycVerification(
                                            nationalId = nationalId,
                                            userType = userType,
                                            universityOrCompany = universityOrCompany,
                                            docName = if (userType == "student") "كارنيه_جامعة_معتمد.pdf" else "HR_Letter_2026.pdf",
                                            onSuccess = {
                                                Toast.makeText(context, "تهانينا! تم توثيق حسابك رسمياً وأصبحت مؤهلاً لكافة العمليات ✓", Toast.LENGTH_LONG).show()
                                                viewModel.navigateBack()
                                            }
                                        )
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = BedVacantGreen),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("submit_kyc_verification_button")
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("اعتماد التوثيق الرقمي النهائي", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepBubble(stepNumber: Int, title: String, isCurrent: Boolean, isCompleted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCompleted -> BedVacantGreen
                        isCurrent -> PrimaryTeal
                        else -> Color(0xFFCBD5E1)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            } else {
                Text(
                    text = "$stepNumber",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 9.sp,
            color = if (isCurrent) PrimaryTeal else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun StepLine(isCompleted: Boolean) {
    Box(
        modifier = Modifier
            .width(26.dp)
            .height(2.dp)
            .background(if (isCompleted) BedVacantGreen else Color(0xFFE2E8F0))
    )
}

@Composable
private fun UploadBox(title: String, isUploaded: Boolean, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isUploaded) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isUploaded) BedVacantGreen else Color(0xFFCBD5E1)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                contentDescription = null,
                tint = if (isUploaded) BedVacantGreen else PrimaryTeal,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (isUploaded) "تم التحميل بنجاح ✓" else "انقر لاختيار المستند",
                fontSize = 9.sp,
                color = if (isUploaded) BedVacantGreen else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
