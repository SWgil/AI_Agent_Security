plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "poc.provenance.verifier"
    compileSdk = 35

    defaultConfig {
        applicationId = "poc.provenance.verifier"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "0.1"
    }

    signingConfigs {
        // Demo key committed to the repository so every build has the same
        // signer. It stands in for an app signing key and protects nothing.
        create("demo") {
            storeFile = rootProject.file("demo-signing.jks")
            storePassword = "android"
            keyAlias = "demo"
            keyPassword = "android"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("demo")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    lint {
        checkReleaseBuilds = false
    }
}

kotlin {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
}

dependencies {
    implementation(project(":provenance-core"))
}
