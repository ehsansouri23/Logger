package ir.pegahtech.logger.adapters

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import ir.pegahtech.logger.MetricType

class TimerLoggerAdapter : LoggerAdapter {

    override fun supported(metricType: MetricType): Boolean =
        metricType == MetricType.Timer

    override fun createNewMeter(
        name: String,
        registry: MeterRegistry,
        tags: Map<String, String>
    ): Meter =
        Timer.builder(LoggerAdapter.escapeValue(name)).also {
            tags.forEach { (tagName, tagValue) ->
                it.tag(LoggerAdapter.escapeValue(tagName), LoggerAdapter.escapeValue(tagValue))
            }
        }.register(registry)
}