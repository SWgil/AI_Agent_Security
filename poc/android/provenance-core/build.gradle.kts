plugins {
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
}

dependencies {
    // org.json ships with Android; plain JVM builds and tests need the artifact.
    compileOnly("org.json:json:20240303")
    testImplementation("org.json:json:20240303")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    // Optional: a real APK attested by poc/signer/attest.py (see poc/e2e.sh).
    systemProperty("attestedApk", providers.gradleProperty("attestedApk").orNull ?: "")
    inputs.property("attestedApk", providers.gradleProperty("attestedApk").orNull ?: "")
}
