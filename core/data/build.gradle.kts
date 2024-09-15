plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.ismaelgr.data"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 26
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Basic
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Hilt
    implementation(libs.hilt)
    kapt(libs.googleHiltCompiler)
    // Room
    implementation(libs.room.impl)
    kapt(libs.room.kapt)
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitGSON)
    implementation(libs.okhttp)
    implementation(libs.okhttpLogginInterceptor)
    // Module Dependency
    implementation(project(":core:domain"))
}