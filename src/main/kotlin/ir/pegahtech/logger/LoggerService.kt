package ir.pegahtech.logger

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import ir.pegahtech.logger.adapters.LoggerAdapter
import ir.pegahtech.logger.di.meterRegistry
import java.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

object LoggerService : Logger {

    private val adapters = LoggerAdapter.getAvailableAdapters()

    private lateinit var metricsRegistry: MetricsRegistry

    private fun getMeter(meterRegistry: MeterRegistry, name: String, tags: Map<String, String>, type: MetricType): Meter =
        adapters.find { it.supported(MetricType.Counter) }?.let { adapter ->
            val (identifierName, identifierType) = adapter.getIdentifier(name, tags, type)
            metricsRegistry.get(identifierName, identifierType) ?: adapter.createNewMeter(
                identifierName, meterRegistry, tags
            ).also {
                metricsRegistry.add(name, it)
            }
        } ?: throw IllegalStateException("No adapter found for type $type")

    fun counter(meterRegistry: MeterRegistry, name: String, tags: Map<String, String> = emptyMap(), value: Double = 1.0) {
        (getMeter(meterRegistry, name, tags, MetricType.Counter) as Counter).increment(value)
    }

    fun histogram(meterRegistry: MeterRegistry, name: String, tags: Map<String, String> = emptyMap(), value: Double) {
        (getMeter(meterRegistry, name, tags, MetricType.Counter) as DistributionSummary).record(value)
    }

    fun timer(meterRegistry: MeterRegistry, name: String, tags: Map<String, String> = emptyMap(), value: Duration) {
        (getMeter(meterRegistry, name, tags, MetricType.Timer) as Timer).record(value)
    }

    @OptIn(ExperimentalTime::class)
    fun <T> withTimeLogging(
        name: String,
        tags: Map<String, String> = emptyMap(),
        action: () -> T,
    ): T = measureTimedValue {
        action()
    }.let {
        timer(meterRegistry, name, tags, it.duration.toJavaDuration())
        it.value
    }

    override fun invoke(log: Log, meterRegistry: MeterRegistry) {
        with(log) {
            when (type) {
                MetricType.Counter -> counter(meterRegistry, name!!, tags!!, value!!)
                MetricType.Histogram -> histogram(meterRegistry, name!!, tags!!, value!!)
                MetricType.Timer -> timer(meterRegistry, name!!, tags!!, duration!!)
                else -> {}
            }
        }
    }
}
