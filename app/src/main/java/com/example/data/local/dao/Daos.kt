package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.local.entity.BedEntity
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.HousekeepingEntity
import com.example.data.local.entity.PropertyEntity
import com.example.data.local.entity.RoomEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UtilitySplitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: Long): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE role = :role LIMIT 1")
    suspend fun getFirstUserByRole(role: String): UserEntity?

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET walletBalance = walletBalance + :amount WHERE id = :userId")
    suspend fun addToWallet(userId: Long, amount: Double)

    @Query("UPDATE users SET walletBalance = walletBalance - :amount WHERE id = :userId")
    suspend fun deductFromWallet(userId: Long, amount: Double)

    @Query("UPDATE users SET kycStatus = :status WHERE id = :userId")
    suspend fun updateKycStatus(userId: Long, status: String)
}

@Dao
interface PropertyDao {
    @Query("SELECT * FROM properties")
    fun getAllProperties(): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE id = :id")
    fun getPropertyById(id: Long): Flow<PropertyEntity?>

    @Query("SELECT * FROM properties WHERE city = :city")
    fun getPropertiesByCity(city: String): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE genderPolicy = :gender")
    fun getPropertiesByGender(gender: String): Flow<List<PropertyEntity>>

    @Query("SELECT * FROM properties WHERE ownerId = :ownerId")
    fun getPropertiesByOwner(ownerId: Long): Flow<List<PropertyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperties(properties: List<PropertyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: PropertyEntity): Long
}

@Dao
interface RoomDao {
    @Query("SELECT * FROM rooms WHERE propertyId = :propertyId")
    fun getRoomsByProperty(propertyId: Long): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id")
    suspend fun getRoomById(id: Long): RoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRooms(rooms: List<RoomEntity>)
}

@Dao
interface BedDao {
    @Query("SELECT * FROM beds WHERE propertyId = :propertyId")
    fun getBedsByProperty(propertyId: Long): Flow<List<BedEntity>>

    @Query("SELECT * FROM beds WHERE roomId = :roomId")
    fun getBedsByRoom(roomId: Long): Flow<List<BedEntity>>

    @Query("SELECT * FROM beds WHERE id = :id")
    suspend fun getBedById(id: Long): BedEntity?

    @Query("SELECT * FROM beds WHERE id = :id")
    fun getBedFlow(id: Long): Flow<BedEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeds(beds: List<BedEntity>)

    @Update
    suspend fun updateBed(bed: BedEntity)

    @Query("UPDATE beds SET currentStatus = :status WHERE id = :bedId")
    suspend fun updateBedStatus(bedId: Long, status: String)

    @Query("UPDATE beds SET currentStatus = :status, pendingUntil = :lockUntil WHERE id = :bedId")
    suspend fun lockBedTemporarily(bedId: Long, status: String, lockUntil: Long)
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE tenantId = :tenantId ORDER BY createdAt DESC")
    fun getBookingsByTenant(tenantId: Long): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE propertyId = :propertyId ORDER BY createdAt DESC")
    fun getBookingsByProperty(propertyId: Long): Flow<List<BookingEntity>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Long): BookingEntity?

    // Double-booking check preventing overlapping reservations
    @Query("""
        SELECT COUNT(*) FROM bookings 
        WHERE bedId = :bedId 
        AND status IN ('pending', 'confirmed') 
        AND startDate < :endDate 
        AND endDate > :startDate
    """)
    suspend fun countOverlappingBookings(bedId: Long, startDate: Long, endDate: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity): Long

    @Update
    suspend fun updateBooking(booking: BookingEntity)

    @Query("UPDATE bookings SET status = :status, paidAt = :paidAt WHERE id = :bookingId")
    suspend fun markBookingConfirmed(bookingId: Long, status: String, paidAt: Long)

    @Query("UPDATE bookings SET contractSigned = 1, contractSignatureDate = :date WHERE id = :bookingId")
    suspend fun signContract(bookingId: Long, date: Long)

    @Query("UPDATE bookings SET depositRefundStatus = :status WHERE id = :bookingId")
    suspend fun updateDepositRefundStatus(bookingId: Long, status: String)
}

@Dao
interface UtilitySplitDao {
    @Query("SELECT * FROM utility_splits WHERE propertyId = :propertyId")
    fun getUtilitiesByProperty(propertyId: Long): Flow<List<UtilitySplitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtilities(utilities: List<UtilitySplitEntity>)

    @Query("UPDATE utility_splits SET isPaidByCurrentTenant = 1 WHERE id = :id")
    suspend fun payUtilityBill(id: Long)
}

@Dao
interface HousekeepingDao {
    @Query("SELECT * FROM housekeeping_visits WHERE propertyId = :propertyId ORDER BY id DESC")
    fun getVisitsByProperty(propertyId: Long): Flow<List<HousekeepingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisit(visit: HousekeepingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisits(visits: List<HousekeepingEntity>)

    @Query("UPDATE housekeeping_visits SET status = :status, completedAt = :time WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String, time: Long)
}
