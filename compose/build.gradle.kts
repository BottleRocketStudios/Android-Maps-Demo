plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from = "../jacocoModule.gradle")

android {
    namespace = "com.bottlerocketstudios.compose"
    compileSdk = Config.AndroidSdkVersions.COMPILE_SDK

    defaultConfig {
        minSdk = Config.AndroidSdkVersions.MIN_SDK
        targetSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true // Enables Jetpack Compose for this module
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Config.Compose.COMPOSE_COMPILER_VERSION
    }
    flavorDimensions += listOf("environment")
    // See BEST_PRACTICES.md for comments on purpose of each build type/flavor/variant
    productFlavors {
        create("internal") {
            buildConfigField("boolean", "INTERNAL", "true")
            buildConfigField("boolean", "PRODUCTION", "false")
            dimension = "environment"
        }
        create("production") {
            buildConfigField("boolean", "INTERNAL", "false")
            buildConfigField("boolean", "PRODUCTION", "true")
            dimension = "environment"
        }
    }

    variantFilter {
        // Gradle ignores any variants that satisfy the conditions listed below. `productionDebug` has no value for this project.
        if (name == "productionDebug" || name == "productionDebugMini") {
            ignore = true
        }
    }
}

// Declare configurations per variant to use in the dependencies block below. More info: https://guides.gradle.org/migrating-build-logic-from-groovy-to-kotlin/#custom_configurations_and_dependencies
val internalDebugImplementation: Configuration by configurations.creating { extendsFrom(configurations["debugImplementation"]) }
val internalDebugMiniImplementation: Configuration by configurations.creating { extendsFrom(configurations["debugImplementation"]) }
val internalReleaseImplementation: Configuration by configurations.creating { extendsFrom(configurations["releaseImplementation"]) }
val productionReleaseImplementation: Configuration by configurations.creating { extendsFrom(configurations["releaseImplementation"]) }

/** List of all buildable dev configurations */
val devConfigurations: List<Configuration> = listOf(internalDebugImplementation, internalDebugMiniImplementation, internalReleaseImplementation)

dependencies {
    implementation(project(mapOf("path" to ":domain")))
    implementation("androidx.navigation:navigation-runtime-ktx:2.4.2")

    // Koin - Dependency Injection
    koinDependencies()

    // LaunchPad - Starting Assets
    launchPadDependencies()

    // Accompanist
    accompanistDependencies()

    // AndroidX
    composeDependencies()

    // Android Maps Compose
    composeMapsDependencies()

    // Coil
    coilDependencies()

    // Utility
    timberDependencies()

    // Test
    junitDependencies()
    mockitoKotlinDependencies()
    truthDependencies()
}
