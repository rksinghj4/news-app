import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.raj.newsapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.raj.newsapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //This prevents Gradle sync failure if the key is missing.
        //val apiKey: String = project.findProperty("API_KEY") as? String ?: "" Not working but below approach is working

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                localProperties.load(input)
            }
        }

        // Get the API key from local.properties, ensuring it's not null and adding quotes
        val apiKey = localProperties.getProperty("API_KEY") ?: "" // Provide a default empty string or throw error if not found
        val baseUrl = localProperties.getProperty("BASE_URL") ?: ""
        //Injecting API_KEY to BuildConfig file
        buildConfigField("String", "API_KEY", "\"$apiKey\"")//Java understand String literals so using double quotes.
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }

    buildTypes {
        debug {
            //resValue()
            //buildConfigField()
        }
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
    buildFeatures {
        compose = true
        buildConfig = true//To generate BuildConfig.java
    }
}

dependencies {
    //
    implementation(libs.squareup.retrofit2)
    //Gson converter
    implementation(libs.squareup.retrofit2.converter.gson)


    implementation(libs.dagger.hilt)
    //To import androidx.hilt.navigation.compose.hiltViewModel
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.dagger.hilt.android.compiler)
    //To import coil.compose.AsyncImage, coil3 has multiplatform support
    implementation(libs.coil.compose)
    //To import androidx.browser.customtabs.CustomTabsIntent
    implementation(libs.androidx.browser)

    implementation(libs.androidx.navigation)    //Navigation compose
    //In Navigation - compose used for data class serialization/deserialization
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //implementation(libs.okhttp3) //No need to add it.
    // Optional: For logging HTTP requests and responses
    //implementation(libs.okhttp3.logging.interceptor)
    //For Unit Testing
    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.androidx.arch.core.testing)
    testImplementation(libs.test.kotlinx.coroutines.test)
    testImplementation(libs.test.turbine)
    // For Android instrumented tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //For Debug builds
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}