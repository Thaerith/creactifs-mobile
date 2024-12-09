package be.creactifs.http

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PositionStore {
    private val _state = MutableStateFlow(PositionState(emptyList()))
    val state: Flow<PositionState> get() = _state
    val currentState get() = _state.value

    fun update(update: PositionStateBuilder.() -> Unit) {
        val builder = PositionStateBuilder(currentState)
        update(builder)
        _state.value = builder.build()
    }
}

data class PositionState(val positions: List<Position>)

data class PositionStateBuilder(private val state: PositionState) {
    var positions = state.positions

    fun build() = PositionState(positions)
}

val positionStore = PositionStore()