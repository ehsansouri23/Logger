package ir.pegahtech.logger.di

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

object DependencyProvider {

    val internalMeterRegistry: MeterRegistry

    init {
        internalMeterRegistry = SimpleMeterRegistry()
    }
}

val meterRegistry: MeterRegistry
    get() = DependencyProvider.internalMeterRegistry