plugins {
    scala
    application
}

dependencies {
    implementation("org.scala-lang:scala3-library_3:3.2.0")
    implementation(project(":sum"))
    implementation("com.monovore:decline_3:2.3.1")

    testImplementation("org.specs2:specs2-junit_3:5.0.7")
}

application {
    mainClass.set("yatsvic.numerology.app.App")
}
