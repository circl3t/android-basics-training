import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.ncorti.ktfmt.gradle") version "0.17.0"
}

android {
    namespace = "ch.proliferate.globule"
    compileSdk = 35

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }
    val mapsApiKey: String = localProperties.getProperty("MAPS_API_KEY") ?: ""

    defaultConfig {
        applicationId = "ch.proliferate.globule"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
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
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    // Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Authentication
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth-ktx")
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    // KTX for the Maps SDK for Android library
    implementation("com.google.maps.android:maps-ktx:5.1.1")
    // Android Maps Compose composables for the Maps SDK for Android
    implementation("com.google.maps.android:maps-compose:6.2.1")
    implementation("com.google.maps.android:maps-compose-utils:4.3.0")
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.4")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    // Shapes
    implementation("androidx.graphics:graphics-shapes:1.0.0-rc01")

    // Firebase UI Auth
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")

}

kapt {
    correctErrorTypes = true
}