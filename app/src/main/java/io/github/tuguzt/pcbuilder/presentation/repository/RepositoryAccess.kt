package io.github.tuguzt.pcbuilder.presentation.repository

import android.app.Application
import io.github.tuguzt.pcbuilder.domain.model.component.Component
import io.github.tuguzt.pcbuilder.presentation.repository.mock.MockComponentRepository
import io.github.tuguzt.pcbuilder.presentation.repository.room.RoomRepository

/**
 * Object for access to all repository types used in the application.
 */
@Suppress("UNCHECKED_CAST")
object RepositoryAccess {
    @JvmStatic
    val localRepository: MutableRepository<Component>
        get() {
            if (pLocalRepository == null) {
                pLocalRepository = MockComponentRepository as MutableRepository<Component>
            }
            return pLocalRepository!!
        }

    @JvmStatic
    fun initRoom(application: Application): RoomRepository {
        val roomRepository = RoomRepository(application)
        pLocalRepository = roomRepository as MutableRepository<Component>
        return roomRepository
    }

    private var pLocalRepository: MutableRepository<Component>? = null
}
