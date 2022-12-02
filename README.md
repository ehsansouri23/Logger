## Logger library

### Usage

```kotlin
 counterLog {
    log withName "request_count" withValue 2.0 withTag {
        "is_api" to "true"
    } withTag {
        "has_user" to "false"
    }
}

// time logging
timerLog {
    log withName "function_call" withTag {
        "is_api" to "true"
    } withTag {
        "has_user" to "false"
    } withBlock {
        myFunctionToMeasureItsTime()
    }
}

// time logging with return value
val value = LoggerService.withTimeLogging(
    "function_call",
    mapOf("is_api" to "true", "has_user" to "false")
) {
    myFunctionToMeasureItsTimeWithReturnvalue()
}

```