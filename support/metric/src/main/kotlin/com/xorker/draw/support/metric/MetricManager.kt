package com.xorker.draw.support.metric

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.atomic.AtomicInteger
import org.springframework.stereotype.Component

@Component
class MetricManager(
    private val metric: MeterRegistry,
) {
    private val playingPlayingRoomCount: AtomicInteger = AtomicInteger()

    init {

        Gauge
            .builder("playing_game_room_gauge", playingPlayingRoomCount) { it.toDouble() }
            .register(metric)
    }

    fun setWebSocketGauge(socketCollection: Map<*, *>) {
        Gauge
            .builder("connect_websocket_gauge", socketCollection) { it.size.toDouble() }
            .register(metric)
    }

    fun increaseGameCount() {
        playingPlayingRoomCount.incrementAndGet()
    }

    fun decreaseGameCount() {
        playingPlayingRoomCount.decrementAndGet()
    }
}
