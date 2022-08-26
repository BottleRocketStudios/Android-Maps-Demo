plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id(Config.ApplyPlugins.KSP)

}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation("androidx.annotation:annotation:1.3.0")
    // Kotlin/coroutines
    kotlinDependencies()
    coroutineDependencies()

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
    archCoreTestingDependencies()
    kotlinxCoroutineTestingDependencies()
    turbineDependencies()
    moshiDependencies()
}
