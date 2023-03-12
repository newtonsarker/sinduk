plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("info.picocli:picocli:4.7.1")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.9.1")
        }
    }
}

application {
    mainClass.set("io.ns.sinduk.App")
}
