package ir.pegahtech.logger

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

infix fun Log.withType(type: MetricType): Log =
    copy(type = type)

infix fun Log.withName(name: String): Log =
    copy(name = name)

infix fun Log.withTags(tags: Map<String, String>): Log =
    copy(tags = tags)

infix fun Log.withValue(value: Double): Log =
    copy(value = value)

infix fun Log.withDuration(duration: Duration): Log =
    copy(duration = duration)
