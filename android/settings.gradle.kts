pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven { url=uri("https://maven.aliyun.com/nexus/content/groups/public/") }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url=uri("https://maven.aliyun.com/nexus/content/groups/public/") }
        mavenCentral()
    }
}

rootProject.name = "TransDroid"
include(":app")
include(":Base")
include(":ble")
include(":Net")
