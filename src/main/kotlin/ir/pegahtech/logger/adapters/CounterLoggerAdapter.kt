package ir.pegahtech.logger.adapters

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import ir.pegahtech.logger.MetricType

class CounterLoggerAdapter : LoggerAdapter {

    override fun supported(metricType: MetricType): Boolean =
        metricType == MetricType.Counter

    override fun createNewMeter(
        name: String,
        registry: MeterRegistry,
        tags: Map<String, String>,
        otherAttributes: LoggerAdapter.Attributes?
    ): Meter =
        Counter.builder(LoggerAdapter.escapeValue(name)).also {
            tags.forEach { (tagName, tagValue) ->
                it.tag(LoggerAdapter.escapeValue(tagName), LoggerAdapter.escapeValue(tagValue))
            }
        }.register(registry)
}