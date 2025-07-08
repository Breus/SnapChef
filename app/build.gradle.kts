import net.ltgt.gradle.errorprone.errorprone

plugins {
    java
    id("io.quarkus")
    id("net.ltgt.errorprone") version "4.3.0"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jackson")
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")

    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("org.assertj:assertj-core:3.27.3")

    implementation("org.jspecify:jspecify:1.0.0")
    errorprone("com.uber.nullaway:nullaway:0.12.7")
    errorprone("com.google.errorprone:error_prone_core:2.39.0")
}

group = "dev.blaauwendraad"
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