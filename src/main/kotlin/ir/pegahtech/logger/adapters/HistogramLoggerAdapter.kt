package ir.pegahtech.logger.adapters

import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import ir.pegahtech.logger.MetricType

class HistogramLoggerAdapter : LoggerAdapter {

    override fun supported(metricType: MetricType): Boolean =
        metricType == MetricType.Histogram

    override fun createNewMeter(
        name: String,
        registry: MeterRegistry,
        tags: Map<String, String>,
        otherAttributes: LoggerAdapter.Attributes?,
    ): Meter =
        DistributionSummary.builder(LoggerAdapter.escapeValue(name)).also {
            tags.forEach { (tagName, tagValue) ->
                it.tag(LoggerAdapter.escapeValue(tagName), LoggerAdapter.escapeValue(tagValue))
            }
            otherAttributes?.let { attributes ->
                if (attributes.publishPercentileHistogram)
                    it.publishPercentileHistogram()
                attributes.minimumExpectedValue?.let { minimumExpectedValue-> it.minimumExpectedValue(minimumExpectedValue) }
                attributes.maximumExpectedValue?.let { maximumExpectedValue-> it.maximumExpectedValue(maximumExpectedValue) }
            }
        }.register(registry)
}