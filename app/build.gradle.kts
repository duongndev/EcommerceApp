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
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation ("com.github.erkutaras:StateLayout:1.5.0")
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    // navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")

    // ViewModel - LiveData
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
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
    implementation ("com.google.dagger:hilt-android:2.51.1")
    kapt ("com.google.dagger:hilt-compiler:2.51.1")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")
}