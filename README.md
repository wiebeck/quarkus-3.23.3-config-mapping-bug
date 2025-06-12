# Quarkus 3.23.3 Config Issue Reproducer

This reproducer demonstrates an issue with Quarkus 3.23.3 configuration.
The app defines a `@ConfigMapping` annotated class `DemoConfig` that is being configured via environment variables in the Gradle build script:

```kotlin
tasks.withType<QuarkusDev> {
    environmentVariables.put("DEMO_INNER_FOO_ID", "123")
    environmentVariables.put("DEMO_INNER_FOO_DATA", "Some data")

    environmentVariables.put("DEMO_INNER_BAR_ID", "456")
    environmentVariables.put("DEMO_INNER_BAR_DATA", "Some other data")
}
```

By calling the `/cfg` endpoint that simply returns the configured values we **expect** an output like this (keys are may be out of order):

```
Key: foo
  ID: 123
  Data: Some data
Key: bar
  ID: 456
  Data: Some other data
```

But instead **the output is empty**.

## Observation 1 - Vault Dependency

By commenting out the `quarkus-vault` dependency in the `build.gradle.kts` file, the **expected output is produced**. So somehow the Vault dependency is interfering with the configuration.

## Observation 2 - Downgrade `smallrye-config`

By adding the following code to the `build.gradle.kts` file, we force the `smallrye-config` version to be 3.13.0, the version being used in Quarkus 3.23.2:

```kotlin
configurations.all {
    resolutionStrategy {
        force(
            "io.smallrye.config:smallrye-config-common:3.13.0",
            "io.smallrye.config:smallrye-config-core:3.13.0",
            "io.smallrye.config:smallrye-config:3.13.0",
        )
    }
}
```

This also produces the **expected output**.

## Observation 3 - application.properties

**Revert** this repository to the original state (empty output instead of expected output) and add the following property to the `application.properties` file:

```properties
demo.inner.bar.data=This text won't show!
```

This produces the following output:

```
Key: bar
  ID: 456
  Data: Some other data
```

It is clear that environment variables supersede the properties file which explains the `Data: Some other data` line, but it is not clear why this triggers the correct parsing of the `DEMO_INNER_BAR_*` environment variables but still ignore the `DEMO_INNER_FOO_*` environment variables.
