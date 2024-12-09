plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "be.creactifs.http"
version = "1.0.0"
application {
    mainClass.set("be.creactifs.http.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.communicationHttp.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.negotiation)
    implementation(libs.ktor.server.serialization)
    implementation(libs.ktor.server.cors)
    testImplementation(libs.kotlin.test.junit)
}