import org.gradle.api.provider.Property
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaLauncher
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

tasks.named<org.gradle.jvm.tasks.Jar>("jar") {
    manifest.attributes["MixinConfigs"] = "mixins.forgiving_item.json"
}


// RFG's 1.7.10 helper tasks expose a java17Launcher. This machine has
// compatible Java 21 and Java 25 installations, so use Java 21 for those
// helpers while Gradle itself runs on Java 25.
val javaToolchains = extensions.getByType<JavaToolchainService>()
val java21Launcher = javaToolchains.launcherFor {
    languageVersion.set(JavaLanguageVersion.of(21))
}
val java21Compiler = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(21))
}
val java8Compiler = javaToolchains.compilerFor {
    languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.configureEach {
    val launcherGetter = javaClass.methods.firstOrNull {
        it.name == "getJava17Launcher" && it.parameterCount == 0
    }
    if (launcherGetter != null) {
        val launcherProperty = launcherGetter.invoke(this)
        if (launcherProperty is Property<*>) {
            @Suppress("UNCHECKED_CAST")
            (launcherProperty as Property<JavaLauncher>).set(java21Launcher)
        }
    }
}

// RFG asks the patched Minecraft compile task for Java 8. Use the locally
// available Java 21 compiler instead of attempting toolchain auto-download.
tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    javaCompiler.set(if (name == "compilePatchedMcJava") java8Compiler else java21Compiler)
}
