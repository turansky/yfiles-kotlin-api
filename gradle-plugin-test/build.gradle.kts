plugins {
    kotlin("js")
    id("com.github.turansky.yfiles")
    id("com.github.turansky.kfc.webpack")
}

kotlin.js {
    browser()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(project(":libraries:yfiles-kotlin"))

    testImplementation(kotlin("test-js"))
}
