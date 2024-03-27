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
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "URL_MELLON_GAMES", "\"${localProperties.getProperty("meccg.url.mellon.games", "")}\"")
            buildConfigField("String", "URL_EVENTS_ALL", "\"${localProperties.getProperty("meccg.url.events.all", "")}\"")
            buildConfigField("String", "URL_NEWS_ALL", "\"${localProperties.getProperty("meccg.url.news.all", "")}\"")
        }

        debug {
            buildConfigField("String", "URL_MELLON_GAMES", "\"${localProperties.getProperty("meccg.url.mellon.games", "")}\"")
            buildConfigField("String", "URL_EVENTS_ALL", "\"${localProperties.getProperty("meccg.url.events.all", "")}\"")
            buildConfigField("String", "URL_NEWS_ALL", "\"${localProperties.getProperty("meccg.url.news.all", "")}\"")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}