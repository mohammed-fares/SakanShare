package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BedDao
import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.HousekeepingDao
import com.example.data.local.dao.PropertyDao
import com.example.data.local.dao.RoomDao
import com.example.data.local.dao.UserDao
import com.example.data.local.dao.UtilitySplitDao
import com.example.data.local.entity.BedEntity
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.HousekeepingEntity
import com.example.data.local.entity.PropertyEntity
import com.example.data.local.entity.RoomEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UtilitySplitEntity

@Database(
    entities = [
        UserEntity::class,
        PropertyEntity::class,
        RoomEntity::class,
        BedEntity::class,
        BookingEntity::class,
        UtilitySplitEntity::class,
        HousekeepingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun propertyDao(): PropertyDao
    abstract fun roomDao(): RoomDao
    abstract fun bedDao(): BedDao
    abstract fun bookingDao(): BookingDao
    abstract fun utilitySplitDao(): UtilitySplitDao
    abstract fun housekeepingDao(): HousekeepingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sakanshare_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
