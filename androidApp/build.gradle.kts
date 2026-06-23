plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
}

android {
    namespace = "net.oddware.gamepadmp.android"
    compileSdk = 37
    defaultConfig {
        applicationId = "net.oddware.gamepadmp.android"
        minSdk = 26
        targetSdk = 37
        versionCode = 7
        versionName = "0.1"
        signingConfig = signingConfigs.getByName("debug")
        versionNameSuffix = "-beta"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            ndk.debugSymbolLevel = "FULL"
        }
    }
    buildToolsVersion = "37.0.0"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    compileSdkMinor = 0

    kotlin {
        jvmToolchain(21)
    }
}

dependencies {
    implementation(libs.androidx.material.icons.core)
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.symbol.processing)
    implementation(libs.coil)
    debugImplementation(libs.compose.ui.tooling)
}