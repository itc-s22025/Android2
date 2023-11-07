plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //↓追加
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    kotlin("plugin.serialization")
}

android {
    namespace = "jp.ac.it_college.std.s22025.pokeapipre"
    compileSdk = 34

    defaultConfig {
        applicationId = "jp.ac.it_college.std.s22025.pokeapipre"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    //JetPack Compose関連
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
        //↑追加  デフォルト↓
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Material3
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")

    //Preview Support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    //Default
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Ktor
    //↓Android用のHTTPクライアントエンジン
    implementation("io.ktor:ktor-client-core:2.3.5")

    implementation("io.ktor:ktor-client-cio:2.3.5")

    implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
    //↓Jsonの変換をする
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")

}