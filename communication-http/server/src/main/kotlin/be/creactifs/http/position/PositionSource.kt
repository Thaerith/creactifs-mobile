package be.creactifs.http.position

import be.creactifs.http.Position
import kotlinx.datetime.LocalDateTime
import java.util.UUID

class PositionSource {
    private val positions = mutableListOf<Position>()

    fun addPosition(dateTime: LocalDateTime, latitude: Float, longitude: Float): Position {
        val position = Position(
            id = UUID.randomUUID().toString(),
            date = dateTime,
            latitude = latitude,
            longitude = longitude
        )
        positions.add(0, position)

        return position
    }

    fun getPositions(): List<Position> {
        return positions
    }
}

val positionSource = PositionSource()