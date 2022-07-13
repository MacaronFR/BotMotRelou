import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	application
	kotlin("jvm") version "1.7.10"
	kotlin("plugin.serialization") version "1.7.10"
}

group = "fr.imacaron.motrelou.bot"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("dev.kord:kord-core:0.8.0-M15")
	implementation("org.slf4j:slf4j-api:1.7.36")
	implementation("org.slf4j:slf4j-simple:1.7.36")
	implementation("io.ktor:ktor-client-resources:2.0.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "17"
	incremental = true
}

application {
	mainClass.set("fr.motrelou.bot.MainKt")
}