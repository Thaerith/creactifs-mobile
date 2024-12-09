package be.creactifs.http

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PositionsScreen() {
    val viewModel = remember { positionsViewModel }

    PositionsScreen(state = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositionsScreen(
    state: PositionsScreenState,
) {
    if (state.addPositionDialogVisible) {
        AddPositionDialog(state)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Positions") },
                actions = {
                    if (!state.loading) {
                        IconButton(onClick = state::onRefresh) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh positions")
                        }
                        IconButton(onClick = state::onAddRequest) {
                            Icon(Icons.Default.Add, contentDescription = "Add position")
                        }
                    } else {
                        CircularProgressIndicator(
                            strokeWidth = 1.dp,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(state.positions) { position ->
                ListItem(
                    headlineContent = { Text("${position.latitude}, ${position.longitude}") },
                )
            }
        }
    }
}

@Composable
private fun AddPositionDialog(
    state: PositionsScreenState,
) {
    Popup(
        onDismissRequest = state::onDismissRequest,
        properties = PopupProperties(focusable = true),
        alignment = Alignment.Center,
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Add position")
                Spacer(modifier = Modifier.padding(16.dp))
                TextField(
                    value = state.latitude,
                    onValueChange = state::onLatitudeChange,
                    label = { Text("Latitude") },
                )
                Spacer(modifier = Modifier.padding(8.dp))
                TextField(
                    value = state.longitude,
                    onValueChange = state::onLongitudeChange,
                    label = { Text("Longitude") },
                )
                Spacer(modifier = Modifier.padding(32.dp))
                Button(
                    onClick = state::onAdd,
                    modifier = Modifier.align(Alignment.End),
                    enabled = !state.loading && state.addEnabled,
                ) {
                    Text("Add")
                }
            }
        }
    }
}

class PositionsViewModel(
    private val positionStore: PositionStore,
    private val loadPositions: LoadPositions,
    private val addPosition: AddPosition,
) : ViewModel(), PositionsScreenState {
    override var positions by mutableStateOf(positionStore.currentState.positions)
        private set
    override var loading by mutableStateOf(false)
        private set
    override var longitude by mutableStateOf("0.0")
        private set
    override var latitude by mutableStateOf("0.0")
        private set
    override var addPositionDialogVisible by mutableStateOf(false)
        private set
    override var addEnabled: Boolean by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            try {
                loading = true
                loadPositions()
            } catch (t: Throwable) {
                println(t.message)
            } finally {
                loading = false
            }
        }
        viewModelScope.launch {
            positionStore.state.flowOn(Dispatchers.Default).collect {
                positions = it.positions
            }
        }
    }

    override fun onAddRequest() {
        addPositionDialogVisible = true
    }

    override fun onAdd() {
        viewModelScope.launch {
            loading = true
            try {
                addPosition(
                    date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    latitude = latitude.toFloat(),
                    longitude = longitude.toFloat(),
                )
                addPositionDialogVisible = false
            } catch (t: Throwable) {
                println(t.message)
            } finally {
                loading = false
            }
        }
    }

    override fun onLongitudeChange(input: String) {
        longitude = input
        checkAddEnabled()
    }

    override fun onLatitudeChange(input: String) {
        latitude = input
        checkAddEnabled()
    }

    override fun onDismissRequest() {
        addPositionDialogVisible = false
    }

    override fun onRefresh() {
        viewModelScope.launch {
            try {
                loading = true
                loadPositions()
            } catch (t: Throwable) {
                println(t.message)
            } finally {
                loading = false
            }
        }
    }

    private fun checkAddEnabled() {
        addEnabled = try {
            latitude.toFloat()
            longitude.toFloat()
            true
        } catch (t: Throwable) {
            false
        }
    }
}

interface PositionsScreenState {
    val positions: List<Position>
    val longitude: String
    val latitude: String
    val loading: Boolean
    val addPositionDialogVisible: Boolean
    val addEnabled: Boolean

    fun onAddRequest()
    fun onAdd()
    fun onLongitudeChange(input: String)
    fun onLatitudeChange(input: String)
    fun onDismissRequest()
    fun onRefresh()
}

val positionsViewModel
    get() = PositionsViewModel(
        positionStore = positionStore,
        loadPositions = loadPositions,
        addPosition = addPosition,
    )