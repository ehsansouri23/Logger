package ir.pegahtech.logger

import io.micrometer.core.instrument.*
import org.springframework.stereotype.Component

@Component("apiMetricsRegistry")
class MetricsRegistry {
    private val registry: MutableMap<Pair<MetricType, String>, Meter> = HashMap()

    fun <T : Meter> add(key: String, meter: T) {
        MetricType.getType(meter.javaClass)?.let {
            registry.putIfAbsent(it to key, meter)
        } ?: throw IllegalAccessError("meter of type ${meter.javaClass.name} is not supported")
    }

    fun get(key: String, type: MetricType): Meter? =
        registry[type to key]
}