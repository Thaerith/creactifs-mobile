package be.creactifs.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadPositions(
    private val positionStore: PositionStore,
    private val positionApi: PositionApi,
) {
    suspend operator fun invoke() = withContext(Dispatchers.Default) {
        val positions = positionApi.getPositions()

        positionStore.update {
            this.positions = positions
        }
    }
}

val loadPositions
    get() = LoadPositions(
        positionStore = positionStore,
        positionApi = positionApi,
    )