package ir.pegahtech.logger

import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.springframework.stereotype.Component

@Component
class HistogramLoggerAdapter : LoggerAdapter {

    override fun supported(metricType: MetricType): Boolean =
        metricType == MetricType.Histogram

    override fun createNewMeter(
        name: String,
        registry: MeterRegistry,
        tags: Map<String, String>
    ): Meter =
        DistributionSummary.builder(LoggerAdapter.escapeValue(name)).also {
            tags.forEach { (tagName, tagValue) ->
                it.tag(LoggerAdapter.escapeValue(tagName), LoggerAdapter.escapeValue(tagValue))
            }
        }.register(registry)
}