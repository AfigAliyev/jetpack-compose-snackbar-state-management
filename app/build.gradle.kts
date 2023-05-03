plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.maximillianleonov.snackbarapp"

    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.maximillianleonov.snackbarapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()

    buildFeatures.compose = true

    composeOptions.kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.bundles.androidx.compose)
    implementation(libs.bundles.androidx.lifecycle)
}
