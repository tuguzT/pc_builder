package com.mirea.tuguzt.pcbuilder.domain.model.memory

/**
 * Data class represents timing of memory.
 *
 * @see Memory
 */
data class MemoryTiming(val casLatency: UInt, val tRCD: UInt, val tRP: UInt, val tRAS: UInt) {
    override fun toString() = "$casLatency-$tRCD-$tRP-$tRAS"
}