plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.duongnd.ecommerceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.duongnd.ecommerceapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SANBOX_GOSHIP_URL", "\"http://sandbox.goship.io/api/v2/\"")
        buildConfigField("String", "SANBOX_GOSHIP_TOKEN", "\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImExY2Q4MTY2NTQ1ZmVlNzNiNThlNWU3OGIzMzY3MDFiNDMzM2I3MTJlYmVjNzQ2YzJkM2Q0MGE3MWM0OWNhODc3NmZiNzg5MTA3OGJiNWY5In0.eyJhdWQiOiIxMyIsImp0aSI6ImExY2Q4MTY2NTQ1ZmVlNzNiNThlNWU3OGIzMzY3MDFiNDMzM2I3MTJlYmVjNzQ2YzJkM2Q0MGE3MWM0OWNhODc3NmZiNzg5MTA3OGJiNWY5IiwiaWF0IjoxNzI4NjU0MzkzLCJuYmYiOjE3Mjg2NTQzOTMsImV4cCI6MjA0NDE4NzE5Mywic3ViIjoiMzMyNCIsInNjb3BlcyI6W119.B_HlHlZuaqE4HghY9b4uZoH6idQeuJjKSQHt6XqWSUXdwU5biyq1sdR8hu3sSGxa97Su6SAzrEoy5xpXLCk3md4dGKPOE_OVUZcIStiRSNy1ME6JgfAy53tt3h47kItVUW6inGRBbspSyabUXsc0sc5iovLhRutnnzTW5wKSo298RhLgBYc8CYiRzdjAOU2_YsgKeSh9XRUutk4mYpkFp6KPTxBzSi4z5q6uo0gfP6H3B_R5TmH6aY6d67RPXEWi3GzfFIifS7bFVRt_5sZbi6YHXjVGe_DXCFGkb3YbDo-KTtG-jRpo9N8ruAN8Nfg_91bmV8KQw4m8dfO0in35q068AHKq3ScUvjCIQawfWZNuMtHgVCUfXO8VwTxBi0gBfEYs-JCo7dDrQUs7-v0y1n794xHD24xp4wkx_2cgLXVpQrt_VxFMCPFpGg22OIXrOq34xucpzLrjEfByrSwmOMd2Dq8amH3VwKAVrMl8-D3-kAKoiaRyTKhvOAlAPk89LHpsaFXnY-SC0GmvLAhh2WndMCPGleEwkKjLN5N6qVlCsCHbl-vbPmGjqLiQpY-uDeCw65QgXB1b8juzLnf8JIzxVL1c__DVGc-GZjTkyw4LHL2qQEg8UjR21ojTEtZ3LzasK1_WlnxI0hZon47Zma0DCKrG2VFJhEwJmHup8Mw\"")
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
        viewBinding = true
        dataBinding = true
        buildConfig = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Library
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.tbuonomo:dotsindicator:5.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("com.github.erkutaras:StateLayout:1.5.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.2")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.2")
    implementation ("androidx.navigation:navigation-dynamic-features-fragment:2.8.2")

    // ViewModel - LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    //noinspection LifecycleAnnotationProcessorWithJava8
    kapt ("androidx.lifecycle:lifecycle-compiler:2.8.3")

    // retrofit - okhttp
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation ("com.squareup.okhttp3:okhttp:5.x0.0-alpha.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    // room database
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")

    // Socket.io
    implementation ("io.socket:socket.io-client:2.0.0") {
        // excluding org.json which is provided by Android
        exclude(group = "org.json", module = "json")
    }
}