package ir.pegahtech.logger

import io.micrometer.core.instrument.Meter

enum class MetricType(val cls: Class<out Meter>) {
    Counter(io.micrometer.core.instrument.Counter::class.java),
    Histogram(io.micrometer.core.instrument.DistributionSummary::class.java),
    Timer(io.micrometer.core.instrument.Timer::class.java);

    companion object {
        private val map = values().associateBy { it.cls }

        fun getType(cls: Class<out Meter>): MetricType? {
            return map[cls]
        }
    }
}