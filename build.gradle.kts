buildscript {
    val agp_version by extra("8.2.0")
    val agp_version1 by extra("8.0.0")
    val agp_version2 by extra("8.2.0")
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("io.realm.kotlin") version "1.11.0" apply false
}