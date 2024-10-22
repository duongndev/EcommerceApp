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
        buildConfigField("String", "SANBOX_GOSHIP_TOKEN", "\"eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjM0ZWE5OGU0YTBjZTM2ZjFlNjhlOGE5OWYyYmExM2U4OTkxYTBkY2MwMjg2NDVmYWY4M2QzNTVhNjcwNzE5MjVjMjA3OTk5MTc3NWEwZWExIn0.eyJhdWQiOiIxMyIsImp0aSI6IjM0ZWE5OGU0YTBjZTM2ZjFlNjhlOGE5OWYyYmExM2U4OTkxYTBkY2MwMjg2NDVmYWY4M2QzNTVhNjcwNzE5MjVjMjA3OTk5MTc3NWEwZWExIiwiaWF0IjoxNzI5MjI4NTIxLCJuYmYiOjE3MjkyMjg1MjEsImV4cCI6MjA0NDc2MTMyMSwic3ViIjoiMzMyNCIsInNjb3BlcyI6W119.cy7n-tCAamLyiFGDiDu2z2KmfdsOFB0tmVHUVC5f0tN6sXhfeqwHBOidh9Blzj66Jgsy6yajjjSw8uRMrVpl9YZPfllyD8re_05Ex8iGXj7pZ-jEI_gi0IyLj23ujG2J3SuZXg7mkl5Obt8iCWFTZT5zzGB12rtPal6Y4Qt_aS_Glbt9HsmoJeOUnak4qPfkEEbi5rwOTlZXyxMr9AJCGz4pSmOpa2dI_zQn_L_gvK1nW-XFIClhStJO1npxJRmT3TLVEnvUojZp6mgysYZtjCH5uqZcfsabUTBnUKmFKz6Ux3N-qCgr3UvJFGIk5QccVQHf1hzy1GOmMMky9e9wQZpWfxU1EtlZXrA2UFxMjbdaPBCesLH0yzTJ_zcY60P7vcQ6q_X4GqDM0olvu80PssA3ANvlrvCfZklpXrlGdu-LCQaisgzQjCHqvbTURAgUqmCFKXInqUvU9fqjTYSsV-44QoS3KssjHrCYU5Y45-qT3DxTJjrg_p2X_sQzzoE71pxNAQf4nz96z5bFibdGVDceteR-86gLdRa_-1jGFBZf9LthuGugTOPTZKmXBHKzvuYywOfM5m6vJBVD27LAxIHvkjcWBvpkELaeBwMaS9vQUdRW2bZHJ9JdM5deAIe0yIZJ4OV5fveW6sAjfe0JIITArItdzfLdiz9FUmhblMY\"")
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
    implementation ("com.squareup.logcat:logcat:0.1")
    implementation("com.github.taimoorsultani:android-sweetalert2:2.0.2")

    // splash
    implementation ("androidx.core:core-splashscreen:1.0.1")

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
    kapt ("androidx.lifecycle:lifecycle-compiler:2.8.6")

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