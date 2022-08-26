# APIPracticeApp

## ライブラリ
```kotlin:build.gradle
plugins {
    id "com.android.application"
    id "kotlin-android"
    id "kotlin-kapt"
    id "kotlin-parcelize"
    id "androidx.navigation.safeargs"
    id "dagger.hilt.android.plugin"
}

android {
    compileSdkVersion 32
    defaultConfig {
        applicationId "com.dena.intern.talk"
        minSdkVersion 26
        targetSdkVersion 32
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
            minifyEnabled false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
        }
    }

    kotlinOptions {
        jvmTarget = '1.8'
    }

    dataBinding {
        enabled = true
    }
}

dependencies {
    implementation "androidx.fragment:fragment-ktx:1.5.0"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "com.google.android.material:material:1.6.1"
    implementation "androidx.preference:preference-ktx:1.2.0"

    // Room components
    def roomVersion = "2.4.2"
    implementation "androidx.room:room-runtime:$roomVersion"
    kapt "androidx.room:room-compiler:$roomVersion"
    androidTestImplementation "androidx.room:room-testing:$roomVersion"

    //  Lifecycle components
    def lifecycleVersion = "2.5.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion"

    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"

    implementation "androidx.navigation:navigation-runtime-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-ui-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-runtime-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-fragment-ktx:$navigationVersion"

    implementation "androidx.recyclerview:recyclerview:1.2.1"

    // Moshi
    implementation "com.squareup.moshi:moshi:1.13.0"
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

    def retrofitVersion = "2.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    implementation "com.squareup.retrofit2:retrofit-mock:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-scalars:2.5.0" // jwtはtext/planeで返ってくるので追加

    def okhttpVersion = "4.10.0"
    implementation "com.squareup.okhttp3:okhttp:$okhttpVersion"
    implementation "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"

    implementation "com.jakewharton.timber:timber:5.0.1"

    implementation "com.google.dagger:hilt-android:$hiltVersion"
    kapt "com.google.dagger:hilt-compiler:$hiltVersion"

    testImplementation "junit:junit:4.13.2"
    androidTestImplementation "androidx.test:runner:1.4.0"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.4.0"
}

kapt {
    correctErrorTypes true
}
```
