plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "com.smartstoragear.MainKt"
    }
}

kotlin {
    jvmToolchain(17)
}
