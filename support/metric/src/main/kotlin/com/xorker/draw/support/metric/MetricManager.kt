package com.xorker.draw.support.metric

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.stereotype.Component

@Component
class MetricManager(
    metric: MeterRegistry,
) {
    private val connectedWebSocketCount: CopyOnWriteArraySet<String> = CopyOnWriteArraySet()
    private val playingPlayingRoomCount: AtomicInteger = AtomicInteger()

    init {
        Gauge
            .builder("connect_websocket_gauge", connectedWebSocketCount) { it.size.toDouble() }
            .register(metric)

        Gauge
            .builder("playing_game_room_gauge", playingPlayingRoomCount) { it.toDouble() }
            .register(metric)
    }

    fun increaseWebsocket(sessionId: String) {
        connectedWebSocketCount.add(sessionId)
    }

    fun decreaseWebsocket(sessionId: String) {
        connectedWebSocketCount.remove(sessionId)
    }

    fun increaseGameCount() {
        playingPlayingRoomCount.incrementAndGet()
    }

    fun decreaseGameCount() {
        playingPlayingRoomCount.decrementAndGet()
    }
}
