package io.github.tuguzt.pcbuilder.presentation.repository.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.tuguzt.pcbuilder.domain.model.component.Component
import io.github.tuguzt.pcbuilder.presentation.repository.room.dto.ComponentDto

/**
 * Data Access Object for [Component].
 *
 * @see Component
 */
@Dao
interface ComponentDao : IDao<ComponentDto> {
    @Query("SELECT * FROM component WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<ComponentDto>>

    @Query("SELECT * FROM component")
    fun getAll(): LiveData<List<ComponentDto>>

    @Query("DELETE FROM component")
    suspend fun deleteAll()
}
