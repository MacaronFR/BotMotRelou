import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	application
	kotlin("jvm") version "1.6.21"
}

group = "fr.imacaron.motrelou.bot"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

val kord_version: String by project
val ktor_version: String by project

dependencies {
	implementation(kotlin("stdlib"))
	implementation("dev.kord:kord-core:$kord_version")
	implementation("io.ktor:ktor-client-core:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
	implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
	implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
	implementation("org.slf4j:slf4j-api:1.7.36")
	implementation("org.slf4j:slf4j-simple:1.7.36")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "17"
	incremental = true
	sourceCompatibility = "17"
	targetCompatibility = "17"
}

application {
	mainClass.set("fr.imacaron.motrelou.bot.MainKt")
}