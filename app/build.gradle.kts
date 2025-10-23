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
    namespace = "com.sketchbox.drawingapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sketchbox.drawingapp.anime.flower.pencil.sketch.art"
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
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
    }
    // Lokalenow config
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

//Kotlin + Java toolchain (forces consistent JDK)
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

    val camerax_version = "1.5.1"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    // ssp/sdp (for responsive sizes)
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.intuit.sdp:sdp-android:1.1.1")

    // For Kotlin Coroutines support
    implementation("io.insert-koin:koin-android:4.1.1")


    implementation("io.coil-kt:coil:2.7.0") // latest stable Coil v2.x (2.7.0) ya 3.x (jese available ho)
    implementation("io.coil-kt:coil-video:2.7.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")

    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.tbuonomo:dotsindicator:5.1.0")
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")


    implementation("androidx.room:room-runtime:2.8.2")
    ksp("androidx.room:room-compiler:2.8.2")

    implementation("com.google.android.play:review:2.0.2")

    implementation("com.android.billingclient:billing-ktx:7.1.1")

    implementation(project(":sdk"))
}
