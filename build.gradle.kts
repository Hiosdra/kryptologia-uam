import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

allprojects {
    group = "org.example"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.bouncycastle:bcprov-jdk15on:1.69")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.69")
    implementation("org.bouncycastle:bcprov-jdk15:1.69")
    implementation("org.bouncycastle:bc-fips:1.0.2.3")
    implementation("org.bouncycastle:bouncycastle-fips-jdk15on:1.67")
    testImplementation(kotlin("test"))
}
