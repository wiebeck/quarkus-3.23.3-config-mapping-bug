import io.quarkus.gradle.tasks.QuarkusDev

plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

// Comment in this block to make the app work as expected.
//configurations.all {
//    resolutionStrategy {
//        force(
//            "io.smallrye.config:smallrye-config-common:3.13.0",
//            "io.smallrye.config:smallrye-config-core:3.13.0",
//            "io.smallrye.config:smallrye-config:3.13.0",
//        )
//    }
//}

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")

    // Comment out this line to make the app work as expected.
    implementation("io.quarkiverse.vault:quarkus-vault")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "org.acme"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<QuarkusDev> {
    environmentVariables.put("DEMO_INNER_FOO_ID", "123")
    environmentVariables.put("DEMO_INNER_FOO_DATA", "Some data")

    environmentVariables.put("DEMO_INNER_BAR_ID", "456")
    environmentVariables.put("DEMO_INNER_BAR_DATA", "Some other data")
}
