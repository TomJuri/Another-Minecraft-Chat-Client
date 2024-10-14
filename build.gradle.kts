plugins {
  id("java")
  id("maven-publish")
}

group = "de.tomjuri"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.google.code.gson:gson:2.11.0")
  implementation("dev.dewy:nbt:1.5.1")
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = group.toString()
      artifactId = project.name.lowercase()
      version = project.version.toString()
      from(components["java"])
    }
  }
}

java {
  withSourcesJar()
  withJavadocJar()
}