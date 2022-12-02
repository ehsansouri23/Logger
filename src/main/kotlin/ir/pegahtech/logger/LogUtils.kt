package ir.pegahtech.logger

import java.time.Duration
import java.util.function.LongBinaryOperator
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

data class Log(
    val name: String? = null,
    val tags: Map<String, String>? = null,
    val type: MetricType? = null,
    val value: Double? = null,
    val duration: Duration? = null,
    val publishPercentileHistogram: Boolean = true,
    val minimumExpectedValue: Double? = null,
    val maximumExpectedValue: Double? = null,
)

val log: Log
    get() = Log()

fun counterLog(log: () -> Log) {
    LoggerService(log().copy(type = MetricType.Counter))
}

fun histogramLog(log: () -> Log) {
    LoggerService(log().copy(type = MetricType.Counter))
}

fun timerLog(log: () -> Log) {
    LoggerService(log().copy(type = MetricType.Timer))
}

infix fun Log.withName(name: String): Log =
    copy(name = name)

infix fun Log.withTags(tags: Map<String, String>): Log =
    copy(tags = tags)

infix fun Log.withTag(tag: () -> Pair<String, String>): Log =
    copy(tags = tags?.plus(tag()) ?: mapOf(tag()))

infix fun Log.withValue(value: Double): Log =
    copy(value = value)

@OptIn(ExperimentalTime::class)
infix fun Log.withBlock(action: () -> Unit): Log =
    copy(duration = measureTimedValue(action).duration.toJavaDuration())

infix fun Log.withDuration(duration: Duration): Log =
    copy(duration = duration)

infix fun Log.publishPercentileHistogram(publish: Boolean): Log =
    copy(publishPercentileHistogram = publish)

infix fun Log.minimumExpectedValue(value: Double): Log =
    copy(minimumExpectedValue = value)

infix fun Log.maximumExpectedValue(value: Double): Log =
    copy(maximumExpectedValue = value)
