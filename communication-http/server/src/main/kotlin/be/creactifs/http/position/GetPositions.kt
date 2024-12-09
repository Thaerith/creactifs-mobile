package be.creactifs.http.position

import be.creactifs.http.Position

class GetPositions(private val positionSource: PositionSource) {
    operator fun invoke(): List<Position> {
        return positionSource.getPositions()
    }
}

val getPositions
    get() = GetPositions(positionSource)