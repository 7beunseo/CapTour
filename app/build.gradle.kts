plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.captour"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.captour"
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

    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // 외부 값 가져오기
    // retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.0.0")

// gson
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// tikxml
    implementation("com.tickaroo.tikxml:annotation:0.8.13")
    implementation("com.tickaroo.tikxml:core:0.8.13")
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt ("com.tickaroo.tikxml:processor:0.8.13")
    // 지도
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    // 이미지
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // 영상 재생
    // implementation ("com.google.android.youtube:youtube-android-player-api:1.2.2")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:chromecast-sender:0.28")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")

    // login
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation("androidx.multidex:multidex:2.0.1")

    // setting
    implementation ("androidx.preference:preference:1.1.1")

    // 구글 로그인
    // 1034749220111-1okipg4eplrakhev6mv5s9e6b82l9v93.apps.googleusercontent.com
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")


    // 스토리지
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
}