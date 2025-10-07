plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.androidx.navigation.safeargs)
    id("io.github.farimarwat.lokalenow") version "1.10"
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.ardrawing"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.ardrawing"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    // ✅ Use stable Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    // ✅ Lokalenow config
    lokalenow {
        languages = listOf(
            "en", // English
            "es", // Spanish
            "fr", // French
            "de", // German
            "zh", // Chinese
            "ja", // Japanese
            "ko", // Korean
            "ar", // Arabic
            "ru", // Russian
            "pt", // Portuguese
            "ur"  // Urdu

        )
        activate = true
    }
}

// ✅ Kotlin + Java toolchain (forces consistent JDK)
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)
    implementation(libs.play.services.ads)

    val camerax_version = "1.5.0"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // ssp/sdp (for responsive sizes)
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    // For Kotlin Coroutines support
    implementation("io.insert-koin:koin-android:4.1.1")

    implementation("com.airbnb.android:lottie:6.0.0")

    implementation("io.coil-kt:coil:2.7.0")

    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation(project(":sdk"))
}
