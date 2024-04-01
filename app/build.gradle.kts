import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
}

val localProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

android {
    namespace = "github.heinrichbarth.meccgevents"
    compileSdk = 34

    defaultConfig {
        applicationId = "github.heinrichbarth.meccgevents"
        minSdk = 26
        targetSdk = 34
        versionCode = 20250101
        versionName = "v20250101"

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
            buildConfigField("String", "URL_MELLON_GAMES", "\"${localProperties.getProperty("meccg.url.mellon.games", "")}\"")
            buildConfigField("String", "URL_EVENTS_ALL", "\"${localProperties.getProperty("meccg.data.events", "")}\"")
            buildConfigField("String", "URL_NEWS_ALL", "\"${localProperties.getProperty("meccg.data.key", "")}\"")
            buildConfigField("String", "URL_AUTH_KEY", "\"${localProperties.getProperty("meccg.data.key", "")}\"")
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            buildConfigField("String", "URL_MELLON_GAMES", "\"${localProperties.getProperty("meccg.url.mellon.games", "")}\"")
            buildConfigField("String", "URL_EVENTS_ALL", "\"${localProperties.getProperty("meccg.data.events", "")}\"")
            buildConfigField("String", "URL_NEWS_ALL", "\"${localProperties.getProperty("meccg.data.news", "")}\"")
            buildConfigField("String", "URL_AUTH_KEY", "\"${localProperties.getProperty("meccg.data.key", "")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.play.services.maps)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}