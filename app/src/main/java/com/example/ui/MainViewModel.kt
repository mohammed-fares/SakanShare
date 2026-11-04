package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.BedEntity
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.HousekeepingEntity
import com.example.data.local.entity.PropertyEntity
import com.example.data.local.entity.RoomEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UtilitySplitEntity
import com.example.data.repository.SakanRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Home : Screen()
    object Explore : Screen()
    data class PropertyDetail(val propertyId: Long) : Screen()
    data class BookingFlow(val propertyId: Long, val bedId: Long) : Screen()
    data class DigitalContract(val bookingId: Long) : Screen()
    object MyBookings : Screen()
    object ServicesAndUtilities : Screen()
    object KycOnboarding : Screen()
    object OwnerDashboard : Screen()
    object HouseRules : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SakanRepository(AppDatabase.getInstance(application))

    // Navigation back stack
    private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Home))
    val currentScreen: StateFlow<Screen> = MutableStateFlow(Screen.Home)

    private val _screen = MutableStateFlow<Screen>(Screen.Home)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    // Current active user
    private val _currentUserId = MutableStateFlow(1L)
    val currentUser: StateFlow<UserEntity?> = _currentUserId.combine(repository.getAllUsers()) { id, users ->
        users.firstOrNull { it.id == id } ?: users.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Current User Role ("tenant" or "owner")
    private val _activeRole = MutableStateFlow("tenant")
    val activeRole: StateFlow<String> = _activeRole.asStateFlow()

    // Properties list
    val allProperties: StateFlow<List<PropertyEntity>> = repository.getAllProperties()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Explore Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCity = MutableStateFlow("الكل")
    val selectedCity = _selectedCity.asStateFlow()

    private val _selectedGender = MutableStateFlow("الكل") // "الكل", "شباب", "بنات"
    val selectedGender = _selectedGender.asStateFlow()

    // Filtered properties
    val filteredProperties = combine(
        allProperties,
        _searchQuery,
        _selectedCity,
        _selectedGender
    ) { properties, query, city, gender ->
        properties.filter { property ->
            val matchesCity = if (city == "الكل") true else (property.cityAr.contains(city) || property.city.contains(city, ignoreCase = true))
            val matchesGender = when (gender) {
                "شباب" -> property.genderPolicy == "male"
                "بنات" -> property.genderPolicy == "female"
                else -> true
            }
            val matchesQuery = if (query.isBlank()) true else {
                property.title.contains(query, ignoreCase = true) ||
                        property.address.contains(query, ignoreCase = true) ||
                        property.nearUniversity.contains(query, ignoreCase = true) ||
                        property.nearMetro.contains(query, ignoreCase = true)
            }
            matchesCity && matchesGender && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Property Details
    private val _selectedPropertyId = MutableStateFlow<Long?>(null)
    val selectedProperty: StateFlow<PropertyEntity?> = _selectedPropertyId.combine(allProperties) { id, list ->
        list.firstOrNull { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _rooms = MutableStateFlow<List<RoomEntity>>(emptyList())
    val rooms = _rooms.asStateFlow()

    private val _beds = MutableStateFlow<List<BedEntity>>(emptyList())
    val beds = _beds.asStateFlow()

    // Active Bookings for current user
    val userBookings: StateFlow<List<BookingEntity>> = _currentUserId.combine(
        repository.getBookingsByTenant(1L)
    ) { _, bookings -> bookings }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Utilities for current active property
    val currentUtilities: StateFlow<List<UtilitySplitEntity>> = repository.getUtilitiesByProperty(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Housekeeping visits
    val housekeepingVisits: StateFlow<List<HousekeepingEntity>> = repository.getHousekeepingVisits(1L)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Booking flow state
    private val _holdSecondsLeft = MutableStateFlow(600) // 10 minutes hold lock
    val holdSecondsLeft = _holdSecondsLeft.asStateFlow()
    private var holdTimerJob: Job? = null

    private val _bookingErrorMessage = MutableStateFlow<String?>(null)
    val bookingErrorMessage = _bookingErrorMessage.asStateFlow()

    private val _bookingSuccessId = MutableStateFlow<Long?>(null)
    val bookingSuccessId = _bookingSuccessId.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeSampleDataIfEmpty()
        }
    }

    // Navigation
    fun navigateTo(destination: Screen) {
        val stack = _screenStack.value.toMutableList()
        stack.add(destination)
        _screenStack.value = stack
        _screen.value = destination

        if (destination is Screen.PropertyDetail) {
            loadPropertyDetails(destination.propertyId)
        }
    }

    fun navigateBack(): Boolean {
        val stack = _screenStack.value.toMutableList()
        if (stack.size > 1) {
            stack.removeAt(stack.lastIndex)
            _screenStack.value = stack
            val prev = stack.last()
            _screen.value = prev
            if (prev is Screen.PropertyDetail) {
                loadPropertyDetails(prev.propertyId)
            }
            return true
        }
        return false
    }

    fun switchRole(role: String) {
        _activeRole.value = role
        if (role == "owner") {
            _currentUserId.value = 2L
        } else {
            _currentUserId.value = 1L
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCity(city: String) {
        _selectedCity.value = city
    }

    fun setSelectedGender(gender: String) {
        _selectedGender.value = gender
    }

    fun loadPropertyDetails(propertyId: Long) {
        _selectedPropertyId.value = propertyId
        viewModelScope.launch {
            repository.getRoomsByProperty(propertyId).collect {
                _rooms.value = it
            }
        }
        viewModelScope.launch {
            repository.getBedsByProperty(propertyId).collect {
                _beds.value = it
            }
        }
    }

    // Double Booking Prevention & Temporary Hold Timer
    fun startBookingHold(bedId: Long, propertyId: Long, onHoldSuccess: () -> Unit, onConflict: (String) -> Unit) {
        viewModelScope.launch {
            val locked = repository.lockBedTemporarily(bedId)
            if (locked) {
                _bookingErrorMessage.value = null
                _holdSecondsLeft.value = 600
                startCountdownTimer()
                onHoldSuccess()
            } else {
                val msg = "عذراً! هذا السرير محجوز بالفعل أو معلق لدفع مستخدم آخر في هذه الفترة."
                _bookingErrorMessage.value = msg
                onConflict(msg)
            }
        }
    }

    private fun startCountdownTimer() {
        holdTimerJob?.cancel()
        holdTimerJob = viewModelScope.launch {
            while (_holdSecondsLeft.value > 0) {
                delay(1000)
                _holdSecondsLeft.value -= 1
            }
        }
    }

    fun confirmBooking(
        propertyId: Long,
        bedId: Long,
        rentalType: String,
        durationUnits: Int,
        paymentMethod: String,
        isInstallment: Boolean,
        installmentProvider: String,
        installmentMonths: Int,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val bed = repository.getBedById(bedId) ?: return@launch
            val now = System.currentTimeMillis()
            val durationMs = when (rentalType) {
                "daily" -> durationUnits * 24L * 60 * 60 * 1000
                "weekly" -> durationUnits * 7L * 24 * 60 * 60 * 1000
                "monthly" -> durationUnits * 30L * 24 * 60 * 60 * 1000
                "semesterly" -> durationUnits * 120L * 24 * 60 * 60 * 1000
                "yearly" -> durationUnits * 365L * 24 * 60 * 60 * 1000
                else -> 30L * 24 * 60 * 60 * 1000
            }
            val startDate = now
            val endDate = now + durationMs

            // Double Booking Conflict Validation
            val hasConflict = repository.checkBookingConflict(bedId, startDate, endDate)
            if (hasConflict) {
                _bookingErrorMessage.value = "تعذر إتمام الحجز: يوجد تعارض تاريخ حجز مسجل لهذا السرير."
                return@launch
            }

            val rentRate = when (rentalType) {
                "daily" -> bed.pricePerDay * durationUnits
                "weekly" -> bed.pricePerWeek * durationUnits
                "monthly" -> bed.pricePerMonth * durationUnits
                "semesterly" -> bed.pricePerMonth * 4 * durationUnits * 0.95 // 5% discount
                "yearly" -> bed.pricePerMonth * 12 * durationUnits * 0.90 // 10% discount
                else -> bed.pricePerMonth
            }

            val totalAmount = rentRate + bed.depositAmount

            val booking = BookingEntity(
                tenantId = _currentUserId.value,
                bedId = bedId,
                propertyId = propertyId,
                startDate = startDate,
                endDate = endDate,
                status = "confirmed",
                depositAmount = bed.depositAmount,
                totalAmount = totalAmount,
                paidAt = now,
                paymentMethod = paymentMethod,
                rentalType = rentalType,
                isInstallment = isInstallment,
                installmentProvider = installmentProvider,
                installmentMonths = installmentMonths,
                contractSigned = true,
                contractSignatureDate = now,
                depositRefundStatus = "محتجز كأمانة رقمية (مسترد بالكامل خلال 48 س عند المغادرة)"
            )

            val bookingId = repository.createBooking(booking)
            _bookingSuccessId.value = bookingId
            holdTimerJob?.cancel()
            onSuccess(bookingId)
        }
    }

    fun signDigitalContract(bookingId: Long, onSigned: () -> Unit) {
        viewModelScope.launch {
            repository.signContract(bookingId)
            onSigned()
        }
    }

    // KYC Verification submission
    fun submitKycVerification(
        nationalId: String,
        userType: String,
        universityOrCompany: String,
        docName: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val updated = user.copy(
                nationalId = nationalId,
                userType = userType,
                universityOrCompany = universityOrCompany,
                proofDocumentName = docName,
                kycStatus = "verified"
            )
            repository.updateUser(updated)
            onSuccess()
        }
    }

    // Utility Payment
    fun payUtilityBill(utility: UtilitySplitEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            if (user.walletBalance >= utility.perTenantAmount) {
                repository.payUtilityBill(utility.id, user.id, utility.perTenantAmount)
                onSuccess()
            }
        }
    }

    // Schedule Housekeeping
    fun scheduleHousekeeping(propertyId: Long, dateString: String, details: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.requestHousekeeping(propertyId, dateString, details)
            onSuccess()
        }
    }

    // Auto Renewal
    fun renewBooking(booking: BookingEntity, additionalMonths: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val additionalMs = additionalMonths * 30L * 24 * 60 * 60 * 1000
            val updated = booking.copy(
                endDate = booking.endDate + additionalMs
            )
            // Just updating booking with extended period
            val bookingDao = AppDatabase.getInstance(getApplication()).bookingDao()
            bookingDao.updateBooking(updated)
            onSuccess()
        }
    }

    // Owner approves refund after checkout inspection
    fun approveDepositRefund(bookingId: Long, tenantId: Long, amount: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateDepositRefundStatus(bookingId, "تم الاسترداد للمحفظة")
            repository.addToWallet(tenantId, amount)
            onSuccess()
        }
    }

    fun addToWallet(userId: Long, amount: Double) {
        viewModelScope.launch {
            repository.addToWallet(userId, amount)
        }
    }
}
