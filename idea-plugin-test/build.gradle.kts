plugins {
    kotlin("js") version "1.3.61"
    id("com.github.turansky.yfiles") version "0.5.0"
}

repositories {
    gradlePluginPortal()
    jcenter()
    mavenLocal()
}

kotlin {
    target {
        nodejs()
    }

    sourceSets {
        main {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("com.yworks.yfiles:yfiles-kotlin:22.0.3-SNAPSHOT")
            }
        }
    }
}

tasks {
    compileKotlinJs {
        kotlinOptions {
            moduleKind = "commonjs"
            allWarningsAsErrors = true
        }
    }

    wrapper {
        gradleVersion = "6.0.1"
        distributionType = Wrapper.DistributionType.ALL
    }
}
