plugins {
    alias(libs.plugins.android.application)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    //implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("org.jsoup:jsoup:1.19.1")
    implementation ("commons-io:commons-io:2.11.0")
    //implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.firebase:firebase-database:20.0.4")
    implementation ("com.google.firebase:firebase-auth:21.0.3")
    // Retrofit for API calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp for networking
    //implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    //implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Lifecycle components for ViewModel
    //implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    //implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")

    // Coroutines for asynchronous programming
    //implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    implementation(libs.runner)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    //implementation("com.google.firebase:firebase-analytics")


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

}