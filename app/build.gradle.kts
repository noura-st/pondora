import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    val openAiKey = localProperties.getProperty("OPENAI_API_KEY") ?: ""
    val anthropicKey = localProperties.getProperty("ANTHROPIC_API_KEY") ?: ""
    val geminiKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""

    namespace = "com.example.pondora"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pondora"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "OPENAI_API_KEY", "\"$openAiKey\"")
        buildConfigField("String", "ANTHROPIC_API_KEY", "\"$anthropicKey\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

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
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.airbnb.android:lottie:6.4.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
}