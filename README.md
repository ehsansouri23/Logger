## Logger library

### Usage

```kotlin
(log withName "name" withTags mapOf("tag" to "value") withType MetricType.Counter withValue 20.0).let(loggerService)

```