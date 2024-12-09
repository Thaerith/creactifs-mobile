package be.creactifs.http.api

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class PostPosition(
    val date: Instant,
    val latitude: Float,
    val longitude: Float
)