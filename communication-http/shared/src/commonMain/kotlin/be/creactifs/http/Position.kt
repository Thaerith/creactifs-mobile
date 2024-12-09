package be.creactifs.http

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val id: String,
    val date: LocalDateTime,
    val latitude: Float,
    val longitude: Float
)