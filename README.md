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

```