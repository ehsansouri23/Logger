package ir.pegahtech.logger

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class CounterLoggerAdapter : LoggerAdapter {

    override fun supported(metricType: MetricType): Boolean =
        metricType == MetricType.Counter

    override fun createNewMeter(
        name: String,
        registry: MeterRegistry,
        tags: Map<String, String>
    ): Meter =
        Counter.builder(LoggerAdapter.escapeValue(name)).also {
            tags.forEach { (tagName, tagValue) ->
                it.tag(LoggerAdapter.escapeValue(tagName), LoggerAdapter.escapeValue(tagValue))
            }
        }.register(registry)
}