package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.entity.BedEntity
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.HousekeepingEntity
import com.example.data.local.entity.PropertyEntity
import com.example.data.local.entity.RoomEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UtilitySplitEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SakanRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val propertyDao = database.propertyDao()
    private val roomDao = database.roomDao()
    private val bedDao = database.bedDao()
    private val bookingDao = database.bookingDao()
    private val utilityDao = database.utilitySplitDao()
    private val housekeepingDao = database.housekeepingDao()

    suspend fun initializeSampleDataIfEmpty() {
        val existing = propertyDao.getAllProperties().first()
        if (existing.isEmpty()) {
            seedInitialData()
        }
    }

    private suspend fun seedInitialData() {
        // 1. Users
        val tenantId = userDao.insertUser(
            UserEntity(
                name = "أحمد محمود العوضي",
                phone = "01012345678",
                email = "ahmed.awadi@std.auc.edu",
                role = "tenant",
                kycStatus = "verified",
                nationalId = "29805120102345",
                userType = "student",
                universityOrCompany = "الجامعة الأمريكية بالقاهرة (AUC)",
                proofDocumentName = "كارنيه الجامعة 2026_AUC.pdf",
                walletBalance = 4200.0
            )
        )

        val ownerId = userDao.insertUser(
            UserEntity(
                name = "م. كريم الشناوي",
                phone = "01234567890",
                email = "kareem.properties@sakanshare.eg",
                role = "owner",
                kycStatus = "verified",
                nationalId = "28503140109876",
                userType = "professional",
                universityOrCompany = "مجموعة الشناوي للاستثمار العقاري",
                proofDocumentName = "عقد ملكية مسجل وسجل تجاري.pdf",
                walletBalance = 48500.0
            )
        )

        // 2. Properties
        // Property 1: New Cairo (التجمع الخامس) - شباب
        val p1Id = propertyDao.insertProperty(
            PropertyEntity(
                ownerId = ownerId,
                title = "سكن شير - كامبوس ووك (AUC Walk)",
                address = "التجمع الخامس - حي النرجس، على بعد 5 دقائق من الجامعة الأمريكية",
                city = "New Cairo",
                cityAr = "القاهرة الجديدة",
                latitude = 30.0131,
                longitude = 31.4989,
                genderPolicy = "male",
                description = "شقة فندقية فاخرة بنظام المشاركة السكنية مصممة خصيصاً لطلاب الجامعة الأمريكية والمهندسين المغتربين. صالة معيشة فسيحة مع شاشة ذكية ومطبخ متكامل مجهز بالكامل.",
                nearMetro = "محطة مونوريل التسعين الجنوبي (300 متر)",
                nearUniversity = "الجامعة الأمريكية (AUC) - 5 دقائق سيراً",
                amenities = "واي فاي ألياف ضوئية 100 ميجا,نظافة دورية أسبوعية,تكييف بكل غرفة,أمن وحراسة 24/7,غسالة ومجفف أوتوماتيك,مكتب دراسة مستقل لكل سرير,شاشة ذكية بصالة المعيشة",
                rating = 4.9f,
                reviewsCount = 38,
                imageUrl = "new_cairo_apartment"
            )
        )

        // Property 2: New Administrative Capital (العاصمة الإدارية) - بنات
        val p2Id = propertyDao.insertProperty(
            PropertyEntity(
                ownerId = ownerId,
                title = "سكن شير - جرين جيت ريزيدنس (النهر الأخضر)",
                address = "العاصمة الإدارية الجديدة - الحي السكني R7 بالقرب من المدينة الطبية",
                city = "New Administrative Capital",
                cityAr = "العاصمة الإدارية",
                latitude = 30.0055,
                longitude = 31.7250,
                genderPolicy = "female",
                description = "شقة راقية مؤثثة فندقياً بأعلى المعايير لطالبات الجامعات الدولية والموظفات بالحي الحكومي. بيئة هادئة وآمنة تماماً مع نظام دخول ذكي بدون مفاتيح تقليدية.",
                nearMetro = "محطة قطار LRT العاصمة الإدارية المركزية",
                nearUniversity = "الجامعة الألمانية الدولية (GIU) وجامعة كوفنتري",
                amenities = "نظام دخول رقمي ذكي,أمن نسائي خاص,تنظيف دوري معقم,مطبخ رخامي فندقي,صالة استذكار هادئة,إنترنت فائق السرعة,عدادات مسبقة الدفع ذكية",
                rating = 4.95f,
                reviewsCount = 29,
                imageUrl = "capital_apartment"
            )
        )

        // Property 3: 6th of October (6 أكتوبر) - شباب
        val p3Id = propertyDao.insertProperty(
            PropertyEntity(
                ownerId = ownerId,
                title = "سكن شير - ستيودنت هاب (MUST Campus)",
                address = "6 أكتوبر - الحي المتميز أمام جامعة مصر للعلوم والتكنولوجيا",
                city = "6th of October",
                cityAr = "6 أكتوبر",
                latitude = 29.9737,
                longitude = 30.9529,
                genderPolicy = "male",
                description = "سكن شبابي حديث ومتميز يجمع بين الراحة الفندقية وتكلفة الإيجار التشاركي المدروسة. مكاتب دراسة منفصلة، دواليب أمانات رقمية خاصة لكل سرير، وشرفة مطلة على الحدائق.",
                nearMetro = "محطة مترو الخط الرابع / مول مصر ومول العرب",
                nearUniversity = "جامعة MUST وجامعة MSA وجامعة 6 أكتوبر",
                amenities = "خزنة رقمية لكل سرير,واي فاي سريع,تكييف سبليت,خدمة Housekeeping مجدولة,شحن كروت كهرباء ومياه تلقائي,ثلاجة وديب فريزر للمشروبات",
                rating = 4.7f,
                reviewsCount = 42,
                imageUrl = "october_apartment"
            )
        )

        // Property 4: Sheikh Zayed (الشيخ زايد) - بنات
        val p4Id = propertyDao.insertProperty(
            PropertyEntity(
                ownerId = ownerId,
                title = "سكن شير - زايد إليت (أركان بلازا)",
                address = "الشيخ زايد - بالقرب من مول أركان ومحور 26 يوليو",
                city = "Sheikh Zayed",
                cityAr = "الشيخ زايد",
                latitude = 30.0211,
                longitude = 31.0022,
                genderPolicy = "female",
                description = "سكن فندقي فاخر مصمم بأناقة عصرية للبنات، مع أثاث مودرن ومراتب طبية مريحة جداً. أجهزة كهربائية موفرة للطاقة ومناخ معيشي يعزز الإنتاجية والراحة التامة.",
                nearMetro = "محطة حافلات النقل السريع وأركان بلازا",
                nearUniversity = "جامعة النيل الأهلية وجامعة القاهرة فرع زايد",
                amenities = "أمن مجمع سكني خاص,خدمة تنظيف وتطهير أسبوعية,تكييف كامل,مطبخ مجهز بفرن وميكروويف,غسالة أوتوماتيك,مساحة عمل مشتركة",
                rating = 4.85f,
                reviewsCount = 31,
                imageUrl = "zayed_apartment"
            )
        )

        // Property 5: New Mansoura (المنصورة الجديدة) - شباب
        val p5Id = propertyDao.insertProperty(
            PropertyEntity(
                ownerId = ownerId,
                title = "سكن شير - دلتا كوست ريزيدنس",
                address = "المنصورة الجديدة - المنطقة المركزية أمام جامعة المنصورة الجديدة",
                city = "New Mansoura",
                cityAr = "المنصورة الجديدة",
                latitude = 31.4500,
                longitude = 31.5500,
                genderPolicy = "male",
                description = "شقة ساحلية بنظام المشاركة السكنية الفندقية بإطلالة بحرية نقية، مجهزة بكافة سبل الراحة ومخصصة لطلاب كليات الطب والهندسة بجامعة المنصورة الجديدة.",
                nearMetro = "محطة الحافلات المركزية الساحلية",
                nearUniversity = "جامعة المنصورة الجديدة (3 دقائق)",
                amenities = "إطلالة بحرية,إنترنت هوائي سريع,نظافة فندقية مجدولة,تكييفات حديثة,مساحات مذاكرة هادئة,تقسيم فواتير رقمي",
                rating = 4.8f,
                reviewsCount = 19,
                imageUrl = "mansoura_apartment"
            )
        )

        // 3. Rooms and Beds for Property 1 (New Cairo)
        // Room 1: Master Single (غرفة ماستر خاصة)
        val r1Id = roomDao.insertRooms(
            listOf(
                RoomEntity(
                    propertyId = p1Id,
                    roomNumber = "غرفة 101 (ماستر)",
                    roomType = "single",
                    roomTypeAr = "غرفة ماستر فردية بحمام خاص",
                    genderPolicy = "male",
                    hasPrivateBathroom = true,
                    floor = 2
                ),
                RoomEntity(
                    propertyId = p1Id,
                    roomNumber = "غرفة 102 (ثنائية)",
                    roomType = "double",
                    roomTypeAr = "غرفة ثنائية بسريرين منفصلين",
                    genderPolicy = "male",
                    hasPrivateBathroom = false,
                    floor = 2
                ),
                RoomEntity(
                    propertyId = p1Id,
                    roomNumber = "غرفة 103 (ثلاثية)",
                    roomType = "triple",
                    roomTypeAr = "غرفة ثلاثية بثلاثة أسرة فردية",
                    genderPolicy = "male",
                    hasPrivateBathroom = false,
                    floor = 2
                )
            )
        )

        // We can query rooms for p1
        val p1Rooms = roomDao.getRoomsByProperty(p1Id).first()
        val masterRoom = p1Rooms.firstOrNull { it.roomType == "single" }
        val doubleRoom = p1Rooms.firstOrNull { it.roomType == "double" }
        val tripleRoom = p1Rooms.firstOrNull { it.roomType == "triple" }

        val bedsList = mutableListOf<BedEntity>()

        masterRoom?.let {
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير ماستر كينج (حمام خاص)",
                    pricePerMonth = 4500.0,
                    pricePerWeek = 1350.0,
                    pricePerDay = 250.0,
                    depositAmount = 4500.0,
                    currentStatus = "vacant"
                )
            )
        }

        doubleRoom?.let {
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير A (بجوار الشباك والمكتب)",
                    pricePerMonth = 2800.0,
                    pricePerWeek = 850.0,
                    pricePerDay = 160.0,
                    depositAmount = 2800.0,
                    currentStatus = "booked" // already booked by another tenant
                )
            )
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير B (بجوار الخزانة الرقمية)",
                    pricePerMonth = 2800.0,
                    pricePerWeek = 850.0,
                    pricePerDay = 160.0,
                    depositAmount = 2800.0,
                    currentStatus = "vacant"
                )
            )
        }

        tripleRoom?.let {
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير A1 (إطلالة حديقة)",
                    pricePerMonth = 2200.0,
                    pricePerWeek = 700.0,
                    pricePerDay = 130.0,
                    depositAmount = 2200.0,
                    currentStatus = "vacant"
                )
            )
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير B1 (الجهة الوسطى)",
                    pricePerMonth = 2100.0,
                    pricePerWeek = 680.0,
                    pricePerDay = 125.0,
                    depositAmount = 2100.0,
                    currentStatus = "booked"
                )
            )
            bedsList.add(
                BedEntity(
                    roomId = it.id,
                    propertyId = p1Id,
                    bedLabel = "سرير C1 (الجهة الداخلية الهادئة)",
                    pricePerMonth = 2100.0,
                    pricePerWeek = 680.0,
                    pricePerDay = 125.0,
                    depositAmount = 2100.0,
                    currentStatus = "vacant"
                )
            )
        }

        bedDao.insertBeds(bedsList)

        // Seed beds for Property 2 (New Administrative Capital - بنات)
        roomDao.insertRooms(
            listOf(
                RoomEntity(
                    propertyId = p2Id,
                    roomNumber = "سويت 201 (ماستر مزدوج)",
                    roomType = "double",
                    roomTypeAr = "غرفة ماستر مزدوجة راقية",
                    genderPolicy = "female",
                    hasPrivateBathroom = true,
                    floor = 3
                ),
                RoomEntity(
                    propertyId = p2Id,
                    roomNumber = "سويت 202 (فردية هادئة)",
                    roomType = "single",
                    roomTypeAr = "غرفة فردية برايفيت للمذاكرة",
                    genderPolicy = "female",
                    hasPrivateBathroom = false,
                    floor = 3
                )
            )
        )
        val p2Rooms = roomDao.getRoomsByProperty(p2Id).first()
        val p2Beds = mutableListOf<BedEntity>()
        p2Rooms.forEach { room ->
            if (room.roomType == "double") {
                p2Beds.add(
                    BedEntity(
                        roomId = room.id,
                        propertyId = p2Id,
                        bedLabel = "سرير ملكي A (نافذة بحري)",
                        pricePerMonth = 3400.0,
                        pricePerWeek = 1050.0,
                        pricePerDay = 200.0,
                        depositAmount = 3400.0,
                        currentStatus = "vacant"
                    )
                )
                p2Beds.add(
                    BedEntity(
                        roomId = room.id,
                        propertyId = p2Id,
                        bedLabel = "سرير ملكي B (مكتب خاص)",
                        pricePerMonth = 3400.0,
                        pricePerWeek = 1050.0,
                        pricePerDay = 200.0,
                        depositAmount = 3400.0,
                        currentStatus = "booked"
                    )
                )
            } else {
                p2Beds.add(
                    BedEntity(
                        roomId = room.id,
                        propertyId = p2Id,
                        bedLabel = "سرير فردي VIP (غرفة كاملة مستقلة)",
                        pricePerMonth = 5200.0,
                        pricePerWeek = 1500.0,
                        pricePerDay = 290.0,
                        depositAmount = 5200.0,
                        currentStatus = "vacant"
                    )
                )
            }
        }
        bedDao.insertBeds(p2Beds)

        // Seed initial Booking for the sample tenant to show in "حجوزاتي"
        val sampleBed = bedDao.getBedsByProperty(p1Id).first().firstOrNull { it.currentStatus == "booked" }
        if (sampleBed != null) {
            val now = System.currentTimeMillis()
            val thirtyDays = 30L * 24 * 60 * 60 * 1000
            val bookingId = bookingDao.insertBooking(
                BookingEntity(
                    tenantId = tenantId,
                    bedId = sampleBed.id,
                    propertyId = p1Id,
                    startDate = now - 20L * 24 * 60 * 60 * 1000,
                    endDate = now + 10L * 24 * 60 * 60 * 1000, // 10 days left -> near 7-day auto-renewal threshold!
                    status = "confirmed",
                    depositAmount = sampleBed.depositAmount,
                    totalAmount = sampleBed.pricePerMonth + sampleBed.depositAmount,
                    paidAt = now - 20L * 24 * 60 * 60 * 1000,
                    paymentMethod = "InstaPay",
                    rentalType = "monthly",
                    contractSigned = true,
                    contractSignatureDate = now - 20L * 24 * 60 * 60 * 1000,
                    depositRefundStatus = "محتجز كأمانة رقمية (مسترد بالكامل خلال 48 س عند المغادرة)"
                )
            )
        }

        // Seed Utility Splits (شحن كروت العدادات الذكية المشتركة)
        utilityDao.insertUtilities(
            listOf(
                UtilitySplitEntity(
                    propertyId = p1Id,
                    utilityType = "شحن كارت الكهرباء الذكي (عداد مسبق الدفع)",
                    totalAmount = 600.0,
                    perTenantAmount = 150.0,
                    activeTenantsCount = 4,
                    period = "نوفمبر 2026",
                    isPaidByCurrentTenant = false
                ),
                UtilitySplitEntity(
                    propertyId = p1Id,
                    utilityType = "فاتورة الغاز الطبيعي وشحن المياه",
                    totalAmount = 280.0,
                    perTenantAmount = 70.0,
                    activeTenantsCount = 4,
                    period = "نوفمبر 2026",
                    isPaidByCurrentTenant = true
                )
            )
        )

        // Seed Housekeeping visits
        housekeepingDao.insertVisits(
            listOf(
                HousekeepingEntity(
                    propertyId = p1Id,
                    scheduledDate = "الخميس القادم (10:00 ص)",
                    status = "مجدولة",
                    cleanerName = "فريق النظافة الفندقية (سكن شير - كرو 4)",
                    tasksDescription = "تطهير وتعقيم الحمامات والمطبخ المشترك، كنس ومسح الأرضيات، التخلص من النفايات، تطهير مقابض الأبواب."
                ),
                HousekeepingEntity(
                    propertyId = p1Id,
                    scheduledDate = "الخميس الماضي",
                    status = "مكتملة",
                    cleanerName = "فريق النظافة الفندقية (سكن شير - كرو 2)",
                    tasksDescription = "تم تعقيم المطبخ والحمام بالكامل وتغيير مناديل ومعطرات الجو.",
                    completedAt = System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000
                )
            )
        )
    }

    // Property queries
    fun getAllProperties(): Flow<List<PropertyEntity>> = propertyDao.getAllProperties()
    fun getPropertyById(id: Long): Flow<PropertyEntity?> = propertyDao.getPropertyById(id)
    fun getPropertiesByCity(city: String): Flow<List<PropertyEntity>> = propertyDao.getPropertiesByCity(city)
    fun getPropertiesByGender(gender: String): Flow<List<PropertyEntity>> = propertyDao.getPropertiesByGender(gender)
    fun getPropertiesByOwner(ownerId: Long): Flow<List<PropertyEntity>> = propertyDao.getPropertiesByOwner(ownerId)

    // Rooms & Beds
    fun getRoomsByProperty(propertyId: Long): Flow<List<RoomEntity>> = roomDao.getRoomsByProperty(propertyId)
    fun getBedsByProperty(propertyId: Long): Flow<List<BedEntity>> = bedDao.getBedsByProperty(propertyId)
    fun getBedsByRoom(roomId: Long): Flow<List<BedEntity>> = bedDao.getBedsByRoom(roomId)
    suspend fun getBedById(bedId: Long): BedEntity? = bedDao.getBedById(bedId)
    fun getBedFlow(bedId: Long): Flow<BedEntity?> = bedDao.getBedFlow(bedId)

    // Bookings & Double Booking Lock
    fun getBookingsByTenant(tenantId: Long): Flow<List<BookingEntity>> = bookingDao.getBookingsByTenant(tenantId)
    fun getBookingsByProperty(propertyId: Long): Flow<List<BookingEntity>> = bookingDao.getBookingsByProperty(propertyId)

    suspend fun checkBookingConflict(bedId: Long, startDate: Long, endDate: Long): Boolean {
        val overlapCount = bookingDao.countOverlappingBookings(bedId, startDate, endDate)
        return overlapCount > 0
    }

    suspend fun lockBedTemporarily(bedId: Long, holdDurationMs: Long = 10 * 60 * 1000L): Boolean {
        val bed = bedDao.getBedById(bedId) ?: return false
        if (bed.currentStatus == "booked") return false
        val now = System.currentTimeMillis()
        if (bed.currentStatus == "pending" && bed.pendingUntil > now) {
            // Already locked by someone else
            return false
        }
        bedDao.lockBedTemporarily(bedId, "pending", now + holdDurationMs)
        return true
    }

    suspend fun createBooking(booking: BookingEntity): Long {
        val id = bookingDao.insertBooking(booking)
        bedDao.updateBedStatus(booking.bedId, "booked")
        return id
    }

    suspend fun confirmBookingPayment(bookingId: Long, bedId: Long, paymentMethod: String) {
        val now = System.currentTimeMillis()
        bookingDao.markBookingConfirmed(bookingId, "confirmed", now)
        bedDao.updateBedStatus(bedId, "booked")
    }

    suspend fun signContract(bookingId: Long) {
        bookingDao.signContract(bookingId, System.currentTimeMillis())
    }

    suspend fun updateDepositRefundStatus(bookingId: Long, newStatus: String) {
        bookingDao.updateDepositRefundStatus(bookingId, newStatus)
    }

    // Users & KYC
    fun getUserById(userId: Long): Flow<UserEntity?> = userDao.getUserById(userId)
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun updateUser(user: UserEntity) = userDao.updateUser(user)
    suspend fun updateKycStatus(userId: Long, status: String) = userDao.updateKycStatus(userId, status)
    suspend fun addToWallet(userId: Long, amount: Double) = userDao.addToWallet(userId, amount)
    suspend fun deductFromWallet(userId: Long, amount: Double) = userDao.deductFromWallet(userId, amount)

    // Utilities & Housekeeping
    fun getUtilitiesByProperty(propertyId: Long): Flow<List<UtilitySplitEntity>> = utilityDao.getUtilitiesByProperty(propertyId)
    suspend fun payUtilityBill(utilityId: Long, userId: Long, amount: Double) {
        utilityDao.payUtilityBill(utilityId)
        userDao.deductFromWallet(userId, amount)
    }

    fun getHousekeepingVisits(propertyId: Long): Flow<List<HousekeepingEntity>> = housekeepingDao.getVisitsByProperty(propertyId)
    suspend fun requestHousekeeping(propertyId: Long, scheduledDate: String, description: String) {
        housekeepingDao.insertVisit(
            HousekeepingEntity(
                propertyId = propertyId,
                scheduledDate = scheduledDate,
                status = "مجدولة",
                tasksDescription = description
            )
        )
    }
}
