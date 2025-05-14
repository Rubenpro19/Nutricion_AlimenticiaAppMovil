plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") //Para el uso de Room
}

android {
    namespace = "com.example.loginfuncional2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.loginfuncional2"
        minSdk = 21
        targetSdk = 35
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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.runtime)//Definimos room para manejo de BD local
    implementation(libs.androidx.room.ktx) //Definimos para q kotlin maneje sub rutinas
    kapt(libs.androidx.room.compiler)//Genera código de BD
    implementation(libs.kotlinx.coroutines.core) //Tareas de backgraud
    implementation(libs.kotlinx.coroutines.android) //Tareas asincronas
}