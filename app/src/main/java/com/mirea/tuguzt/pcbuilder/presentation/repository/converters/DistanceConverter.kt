package com.mirea.tuguzt.pcbuilder.presentation.repository.converters

import androidx.room.TypeConverter
import io.nacular.measured.units.Distance
import io.nacular.measured.units.Length.Companion.meters
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times

object DistanceConverter {
    @JvmStatic
    @TypeConverter
    fun fromDistance(value: Measure<Distance>?): Double? = value?.let { it `in` meters }

    @JvmStatic
    @TypeConverter
    fun toDistance(value: Double?): Measure<Distance>? = value?.let { it * meters }
}
