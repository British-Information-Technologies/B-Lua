import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
	kotlin("jvm")
	id("org.jetbrains.compose")
	kotlin("plugin.serialization") version "1.9.20"
}

group = "com.example"
version = "1.0-SNAPSHOT"


tasks.test {
	useJUnitPlatform()
}

repositories {
	mavenCentral()
	maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
	google()
}

dependencies {
	// Note, if you develop a library, you should use compose.desktop.common.
	// compose.desktop.currentOs should be used in launcher-sourceSet
	// (in a separate module for demo project and in testMain).
	// With compose.desktop.common you will also lose @Preview functionality
	implementation(compose.desktop.currentOs)


	// ktor
	val ktor_version: String by project
	implementation("io.ktor:ktor-client-core:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")

	// serialisation
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

	implementation("cafe.adriel.bonsai:bonsai-core:1.2.0")

	// testing
	testImplementation(kotlin("test"))
}

compose.desktop {
	application {
		mainClass = "MainKt"

		nativeDistributions {
			targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
			packageName = "demo"
			packageVersion = "1.0.0"
		}
	}
}

