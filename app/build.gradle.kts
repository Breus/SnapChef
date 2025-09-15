import net.ltgt.gradle.errorprone.errorprone
import org.gradle.kotlin.dsl.errorprone

plugins {
    alias(libs.plugins.errorprone)
    alias(libs.plugins.quarkus)
    alias(libs.plugins.spotless)
    java
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    errorprone(libs.errorprone.core)
    errorprone(libs.nullaway)

    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))

    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.hibernate.validator)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.smallrye.jwt)
    implementation(libs.quarkus.rest)
    implementation(libs.quarkus.rest.jackson)

    testImplementation(libs.assertj.core)
    testImplementation(libs.mockito.junit)
    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.restassured)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
}

group = "dev.blaauwendraad"
version = "1.0.0-SNAPSHOT"

spotless {
    ratchetFrom("origin/master")
    java {
        palantirJavaFormat(libs.versions.palantir.java.format.get())
        target("src/**/*.java")
    }
}

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

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        error(
            "CheckedExceptionNotThrown",
            "DeadException",
            "DefaultCharset",
            "FunctionalInterfaceClash",
            "InvalidThrows",
            "InvalidThrowsLink",
            "NonFinalStaticField",
            "NullAway",
            "RedundantOverride",
            "RedundantThrows",
            "RemoveUnusedImports",
            "UnnecessarilyFullyQualified",
            "UnnecessarilyUsedValue",
            "UnnecessaryBoxedAssignment",
            "UnnecessaryBoxedVariable",
            "UnnecessaryFinal",
            "UnusedException",
            "UnusedLabel",
            "UnusedMethod",
            "UnusedNestedClass",
            "UnusedVariable",
            "WildcardImport",
        )
        disable(
            "StringCaseLocaleUsage",
            "MissingSummary",
        )
        option("NullAway:AnnotatedPackages", "dev.blaauwendraad")
        excludedPaths = ".*/build/generated/.*"
    }
    options.encoding = "UTF-8"
}