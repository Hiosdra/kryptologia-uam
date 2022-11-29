rootProject.name = "kryptografia"
include("kdc-kdc")
include("kdc-alice")
include("kdc-bob")

pluginManagement {
    repositories {
        maven { url = uri("https://repo.spring.io/milestone") }
        gradlePluginPortal()
    }
}
include("kdc-common")
