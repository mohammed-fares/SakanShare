package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String,
    val role: String, // "tenant", "owner", "admin"
    val kycStatus: String, // "pending", "verified", "rejected"
    val nationalId: String,
    val userType: String = "student", // "student", "professional"
    val universityOrCompany: String = "",
    val proofDocumentName: String = "",
    val walletBalance: Double = 3500.0,
    val avatarUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "properties",
    indices = [Index(value = ["city"])]
)
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ownerId: Long,
    val title: String,
    val address: String,
    val city: String,
    val cityAr: String,
    val latitude: Double,
    val longitude: Double,
    val genderPolicy: String, // "male", "female"
    val description: String,
    val nearMetro: String,
    val nearUniversity: String,
    val amenities: String, // comma separated
    val rating: Float = 4.8f,
    val reviewsCount: Int = 24,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = PropertyEntity::class,
            parentColumns = ["id"],
            childColumns = ["propertyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["propertyId"])]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val roomNumber: String,
    val roomType: String, // "single", "double", "triple"
    val roomTypeAr: String,
    val genderPolicy: String, // "male", "female"
    val hasPrivateBathroom: Boolean,
    val floor: Int = 2
)

@Entity(
    tableName = "beds",
    foreignKeys = [
        ForeignKey(
            entity = RoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["roomId"]), Index(value = ["propertyId"])]
)
data class BedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val roomId: Long,
    val propertyId: Long,
    val bedLabel: String,
    val pricePerMonth: Double,
    val pricePerWeek: Double,
    val pricePerDay: Double,
    val depositAmount: Double,
    val isActive: Boolean = true,
    val currentStatus: String = "vacant", // "vacant", "booked", "pending"
    val pendingUntil: Long = 0L // 10-minute hold lock timestamp
)

@Entity(
    tableName = "bookings",
    foreignKeys = [
        ForeignKey(
            entity = BedEntity::class,
            parentColumns = ["id"],
            childColumns = ["bedId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["bedId", "startDate", "endDate"]),
        Index(value = ["tenantId"])
    ]
)
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tenantId: Long,
    val bedId: Long,
    val propertyId: Long,
    val startDate: Long,
    val endDate: Long,
    val status: String = "pending", // "pending", "confirmed", "cancelled", "completed"
    val depositAmount: Double,
    val totalAmount: Double,
    val paidAt: Long? = null,
    val paymentMethod: String = "InstaPay",
    val rentalType: String = "monthly", // "daily", "weekly", "monthly", "semesterly", "yearly"
    val isInstallment: Boolean = false,
    val installmentProvider: String = "", // "ValU", "Sympl", "Aman"
    val installmentMonths: Int = 0,
    val contractSigned: Boolean = false,
    val contractSignatureDate: Long? = null,
    val depositRefundStatus: String = "محتجز كأمانة رقمية", // "محتجز كأمانة رقمية", "مفحوص وموافق عليه", "تم الاسترداد للمحفظة"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "utility_splits")
data class UtilitySplitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val utilityType: String, // "شحن كارت الكهرباء الذكي", "الغاز الطبيعي", "المياه والإنترنت"
    val totalAmount: Double,
    val perTenantAmount: Double,
    val activeTenantsCount: Int,
    val period: String,
    val isPaidByCurrentTenant: Boolean = false,
    val dueDate: Long = System.currentTimeMillis() + 86400000L * 5
)

@Entity(tableName = "housekeeping_visits")
data class HousekeepingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val propertyId: Long,
    val scheduledDate: String,
    val status: String = "مجدولة", // "مجدولة", "جارية", "مكتملة"
    val cleanerName: String = "فريق النظافة الفندقية (سكن شير)",
    val tasksDescription: String = "تنظيف وتطهير المطبخ، تعقيم الحمامات، مسح الصالة العامة، تغيير مفروشات الصالون",
    val completedAt: Long? = null
)
