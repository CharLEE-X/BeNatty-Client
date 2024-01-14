package core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

private const val FLOW_FREQUENCY = 16L

fun <T> infiniteFlow(item: suspend () -> T): Flow<T> {
    return flow {
        while (true) {
            val result = item()
            emit(result)
            delay(FLOW_FREQUENCY)
        }
    }.distinctUntilChanged()
}
