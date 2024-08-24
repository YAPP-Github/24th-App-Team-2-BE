package com.xorker.draw.support.metric

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.stereotype.Component

@Component
class MetricManager(
    private val metric: MeterRegistry,
) {
    private val connectedWebSocketCount: AtomicInteger = AtomicInteger()
    private val playingPlayingRoomCount: AtomicInteger = AtomicInteger()

    init {
        Gauge
            .builder("connect_websocket_gauge", connectedWebSocketCount) { it.toDouble() }
            .register(metric)

        Gauge
            .builder("playing_game_room_gauge", playingPlayingRoomCount) { it.toDouble() }
            .register(metric)

    }

    fun increaseWebsocket() {
        connectedWebSocketCount.incrementAndGet()
    }

    fun decreaseWebsocket() {
        connectedWebSocketCount.decrementAndGet()
    }

    fun increaseGameCount(){
        playingPlayingRoomCount.incrementAndGet()
    }

    fun decreaseGameCount(){
        playingPlayingRoomCount.decrementAndGet()
    }
}
