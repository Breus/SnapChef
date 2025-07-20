import net.ltgt.gradle.errorprone.errorprone
import org.gradle.kotlin.dsl.errorprone

plugins {
    java
    alias(libs.plugins.quarkus)
    alias(libs.plugins.errorprone)
    alias(libs.plugins.spotless)
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-elytron-security-jdbc")
    implementation("io.quarkus:quarkus-elytron-security")
    implementation(libs.quarkus.rest)
    implementation(libs.quarkus.rest.jackson)
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.jdbc.postgresql)

    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.assertj.core)
    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.postgresql)

    implementation(libs.jspecify)
    errorprone(libs.nullaway)
    errorprone(libs.errorprone.core)
}

group = "dev.blaauwendraad"
version = "1.0.0-SNAPSHOT"

spotless {
    java {
        palantirJavaFormat("2.72.0")
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
        option("NullAway:JSpecifyMode")
        option("NullAway:AnnotatedPackages", "dev.blaauwendraad.masker")
        excludedPaths = ".*/build/generated/.*"
    }
    options.encoding = "UTF-8"
}