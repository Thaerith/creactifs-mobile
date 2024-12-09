package be.creactifs.http.position

import be.creactifs.http.Position
import be.creactifs.http.api.PostPosition
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddPosition(private val source: PositionSource) {
    operator fun invoke(request: PostPosition): Position {
        val position = source.addPosition(
            dateTime = request.date.toLocalDateTime(TimeZone.UTC),
            latitude = request.latitude,
            longitude = request.longitude
        )

        return position
    }
}

val addPosition
    get() = AddPosition(positionSource)