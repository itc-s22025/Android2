// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    //↓Ktor
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    //↓io.ktor:ktor-serialization-kotlinx-jsonが正しく動作するための設定
    kotlin("plugin.serialization") version "1.8.10" apply false
}