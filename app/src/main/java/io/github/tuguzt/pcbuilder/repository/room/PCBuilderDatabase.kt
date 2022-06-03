package io.github.tuguzt.pcbuilder.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.tuguzt.pcbuilder.repository.room.dao.ComponentDao
import io.github.tuguzt.pcbuilder.repository.room.dao.ManufacturerDao
import io.github.tuguzt.pcbuilder.repository.room.dto.component.ComponentEntity
import io.github.tuguzt.pcbuilder.repository.room.dto.component.ManufacturerEntity

/**
 * Room database which contains all the data saved by user on the device.
 */
@Database(
    entities = [ComponentEntity::class, ManufacturerEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class PCBuilderDatabase : RoomDatabase() {
    abstract val componentDao: ComponentDao
    abstract val manufacturerDao: ManufacturerDao
}
