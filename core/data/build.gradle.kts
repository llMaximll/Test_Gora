import java.util.Locale

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.github.llmaximll.core.data"
    compileSdk = AppVersions.COMPILE_SDK

    defaultConfig {
        minSdk = AppVersions.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        ksp {
            arg("room.schemaLocation", "${projectDir.parentFile}/app/schemas")
        }

        buildConfigField(
            "int",
            "DB_VERSION",
            "${AppVersions.VERSION_CODE}"
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = AppVersions.IS_MINIFY_ENABLED
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
        buildConfig = true
    }

    // Временно, для исправления бага ksp https://github.com/google/ksp/issues/350#issuecomment-1691842005
    androidComponents {
        onVariants(selector().all()) { variant ->
            afterEvaluate {
                val variantNameCapitalized = variant.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                val kspTaskName = "ksp${variantNameCapitalized}Kotlin"
                val buildConfigTaskName = "generate${variantNameCapitalized}BuildConfig"

                project.tasks.named(kspTaskName) {
                    val buildConfigTask = project.tasks.named(buildConfigTaskName).get() as com.android.build.gradle.tasks.GenerateBuildConfig
                    (this as org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompileTool<*>).source(buildConfigTask.sourceOutputDir)
                }
            }
        }
    }
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.room)
    annotationProcessor(libs.room.annotation)
    ksp(libs.room.annotation)
    implementation(libs.room.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}