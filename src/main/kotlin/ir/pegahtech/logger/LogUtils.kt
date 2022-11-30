package ir.pegahtech.logger

import io.micrometer.core.instrument.MeterRegistry
import ir.pegahtech.logger.di.meterRegistry
import java.time.Duration

data class Log(
    val name: String? = null,
    val tags: Map<String, String>? = null,
    val type: MetricType? = null,
    val value: Double? = null,
    val duration: Duration? = null,
)

val log: Log
    get() = Log()

val counterLog: Log
    get() = Log(type = MetricType.Counter)

val timerLog: Log
    get() = Log(type = MetricType.Timer)

val histogramLog: Log
    get() = Log(type = MetricType.Histogram)

infix fun Log.withType(type: MetricType): Log =
    copy(type = type)

infix fun Log.withName(name: String): Log =
    copy(name = name)

infix fun Log.withTags(tags: Map<String, String>): Log =
    copy(tags = tags)

infix fun Log.withTag(tag: () -> Pair<String, String>):Log =
    copy(tags = tags?.plus(tag()) ?: mapOf(tag()))

infix fun Log.withValue(value: Double): Log =
    copy(value = value)

infix fun Log.withDuration(duration: Duration): Log =
    copy(duration = duration)

infix fun Log.on(meterRegistry: MeterRegistry) =
    LoggerService.invoke(this, meterRegistry)

fun main() {
    counterLog withTag {"tag1" to "value1"} withTag {"tag2" to "value2"} withName "counter" withValue 1.0 withDuration Duration.ofSeconds(20) on meterRegistry
}