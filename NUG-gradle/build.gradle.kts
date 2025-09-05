plugins {
    id("base")
}

val dockerImage = "docker.unidata.ucar.edu/unidata-jekyll-docs:0.0.6"

val buildDoc = tasks.register<Exec>(name = "buildJekyllSite") {
    group = "documentation"
    description = "Build the NUG documentation."
    val siteBuildDir = layout.buildDirectory.dir("site")
    val buildDocInputs = fileTree(".")
    buildDocInputs.exclude("build/", ".gradle", ".jekyll-cache")
    inputs.files(buildDocInputs)
    outputs.dir(siteBuildDir)
    commandLine("docker", "run", "--rm",
        "-e", "SRC_DIR=/NUG",
        "-v", ".:/NUG",
        "-v", "./${relativePath(siteBuildDir)}:/site",
        dockerImage, "build")
}

tasks.named("assemble") {
    dependsOn(buildDoc)
}

tasks.register<Exec>(name = "serveJekyllSite") {
    group = "documentation"
    description = "Start a local server to live edit the NUG documentation."
    commandLine("docker", "run", "--rm", "-d",
        "--name", "nug-docs-server",
        "-e", "SRC_DIR=/NUG",
        "-v", ".:/NUG",
        "-p", "4000:4000",
        dockerImage, "serve", "--livereload")
    standardOutput = java.io.OutputStream.nullOutputStream()
    doLast {
        val msg = "NUG documentation available at http://localhost:4000"
        val bannerBorder = "#".repeat(msg.length + 4)
        println()
        println(bannerBorder)
        println("# $msg #")
        println(bannerBorder)
        println()
    }
}

tasks.register<Exec>(name = "stopServe") {
    group = "documentation"
    description = "Stop the local server used while live editing the NUG documentation."
    commandLine("docker", "stop", "nug-docs-server")
    delete("$projectDir/Gemfile")
    delete("$projectDir/Gemfile.lock")
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "9.0.0"
}
