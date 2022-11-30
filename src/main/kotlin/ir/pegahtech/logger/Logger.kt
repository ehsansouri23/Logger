package ir.pegahtech.logger

import io.micrometer.core.instrument.MeterRegistry

interface Logger : (Log, MeterRegistry) -> Unit