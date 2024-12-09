package be.creactifs.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class AddPosition(
    private val positionApi: PositionApi,
    private val positionStore: PositionStore,
) {
    suspend operator fun invoke(
        date: LocalDateTime, latitude: Float, longitude: Float
    ): Position = withContext(Dispatchers.Default) {
        val position = positionApi.postPosition(
            date = date.toInstant(TimeZone.UTC),
            latitude = latitude,
            longitude = longitude,
        )
        val updatedPositions = positionStore.currentState.positions.toMutableList()
        updatedPositions.add(0, position)
        positionStore.update {
            positions = updatedPositions
        }

        return@withContext position
    }
}

val addPosition
    get() = AddPosition(
        positionApi = positionApi,
        positionStore = positionStore,
    )