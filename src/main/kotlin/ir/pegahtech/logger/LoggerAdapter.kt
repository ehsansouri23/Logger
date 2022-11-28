package ir.pegahtech.logger

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import java.util.Locale

interface LoggerAdapter {

    fun supported(metricType: MetricType): Boolean

    fun getIdentifier(
        name: String,
        tags: Map<String, String>,
        type: MetricType
    ): Pair<String, MetricType> =
        getKey(name, tags) to type

    fun createNewMeter(name: String, registry: MeterRegistry, tags: Map<String, String>): Meter

    companion object {
        fun getKey(name: String, tags: Map<String, String>): String {
            return escapeValue(name) + "___" + tags.keys.sorted()
                .map { "${escapeValue(it)}__${escapeValue(tags[it])}" }.toList()
                .fold("") { a, b -> "${a}___${b}" }
        }

        fun escapeValue(input: String?): String {
            return (input ?: "").lowercase(Locale.getDefault())
                .replace(" ", "_")
                .replace("-", "_")
                .replace("\\.", "_");
        }
    }
}

