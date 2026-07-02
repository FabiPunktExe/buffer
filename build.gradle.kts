import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.multiplatformLibrary)
    `maven-publish`
}

group = "de.fabiexe"
version = "1.2.2"

repositories {
    mavenCentral()
    google()
}

kotlin {
    applyDefaultHierarchyTemplate()

    jvmToolchain(21)
    jvm {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }

    android {
        namespace = "de.fabiexe.buffer"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    js {
        browser()
        nodejs()
        binaries.library()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }

    linuxX64()
    linuxArm64()
    mingwX64()
    macosArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    tvosArm64()
    tvosSimulatorArm64()
    watchosArm32()
    watchosArm64()
    watchosDeviceArm64()
    watchosSimulatorArm64()

    sourceSets {
        all {
            val baseDir = if (name.endsWith("Test")) "test" else "src"
            val platform = name.removeSuffix("Main").removeSuffix("Test")
            val targetFolder = if (platform == "common") baseDir else "$baseDir@$platform"
            kotlin.setSrcDirs(listOf(targetFolder))
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain {
            kotlin.srcDirs("src@jvmAndAndroid")
        }
        jvmTest {
            kotlin.srcDirs("test@jvmAndAndroid")
        }
        androidMain {
            kotlin.srcDirs("src@jvmAndAndroid")
        }
        androidUnitTest {
            kotlin.srcDirs("test@jvmAndAndroid")
        }
    }
}

publishing {
    repositories {
        maven("https://repo.diruptio.de/repository/maven-public-releases") {
            name = "DiruptioPublic"
            credentials {
                username = (System.getenv("DIRUPTIO_REPO_USERNAME") ?: project.findProperty("maven_username") ?: "").toString()
                password = (System.getenv("DIRUPTIO_REPO_PASSWORD") ?: project.findProperty("maven_password") ?: "").toString()
            }
        }
    }
}
