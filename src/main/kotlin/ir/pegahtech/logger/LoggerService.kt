package ir.pegahtech.logger

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

@Service
class LoggerService(
    private val adapters: List<LoggerAdapter>,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    @Autowired
    private lateinit var metricsRegistry: MetricsRegistry

    private fun getMeter(name: String, tags: Map<String, String>, type: MetricType): Meter =
        adapters.find { it.supported(MetricType.Counter) }?.let { adapter ->
            val (identifierName, identifierType) = adapter.getIdentifier(name, tags, type)
            metricsRegistry.get(identifierName, identifierType) ?: adapter.createNewMeter(
                identifierName, meterRegistry, tags
            ).also {
                metricsRegistry.add(name, it)
            }
        } ?: throw IllegalStateException("No adapter found for type $type")

    fun counter(name: String, tags: Map<String, String> = emptyMap(), value: Double = 1.0) {
        (getMeter(name, tags, MetricType.Counter) as Counter).increment(value)
    }

    fun histogram(name: String, tags: Map<String, String> = emptyMap(), value: Double) {
        (getMeter(name, tags, MetricType.Counter) as DistributionSummary).record(value)
    }

    fun timer(name: String, tags: Map<String, String> = emptyMap(), value: Duration) {
        (getMeter(name, tags, MetricType.Timer) as Timer).record(value)
    }

    @OptIn(ExperimentalTime::class)
    fun <T> withTimeLogging(
        name: String,
        tags: Map<String, String> = emptyMap(),
        action: () -> T
    ): T = measureTimedValue {
        action()
    }.let {
        timer(name, tags, it.duration.toJavaDuration())
        it.value
    }
}