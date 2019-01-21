package io.lecon.debugtools.plugin

import com.android.build.gradle.AppExtension
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import groovy.lang.Closure
import io.lecon.debugtools.plugin.domain.DebugTools
import io.lecon.debugtools.plugin.g.getG
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle
import java.io.File
import java.util.*
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass


/**
 * Created by spawn on 2018/5/21.
 */
class PluginMain : Plugin<Project> {

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T {
        return getByType(type.java)
    }

    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        val instantiator = (project.gradle as DefaultGradle).services.get(Instantiator::class.java)
        project.extensions.create(CONFIG_NAME, DebugTools::class.java, instantiator, project)

        project.afterEvaluate {
            val config = project.extensions.findByName(CONFIG_NAME)
            config?.let {
                genJavaCode(it as DebugTools)
            }
        }
    }

    private fun genJavaCode(debugTools: DebugTools) {
        val file: ClassName = ClassName.get("java.io", "File")

        val getExtraDatabases = MethodSpec.methodBuilder("getExtraDatabases")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(HashMap::class.java)

        getExtraDatabases.addStatement("HashMap<String, \$T> map = new HashMap()",file)
        debugTools.databases.all { database ->
            if (database.path != null && database.name != null) {
                getExtraDatabases.addStatement("map.put(\"${database.name}\",new \$T(\"${database.path}\"))",file)
            }
        }
        getExtraDatabases.addStatement("return map")

        val helloWorld = TypeSpec.classBuilder("ExtraDatabaseBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(getExtraDatabases.build())
                .build()

        val javaFile = JavaFile.builder("io.lecon.debug_tools", helloWorld)
                .build()

        project.extensions[AppExtension::class].apply {
            var file = File(project.buildDir, "debug_tools")
            file = File(file, "java")
            file.mkdirs()
            javaFile.writeTo(file)
            sourceSets.register("debug_tools") {
                it.java(getG(file) as Closure<*>?)
            }

            sourceSets.all {
                if (it.name == "main") {
                    it.java.srcDirs(file)
                }
            }
        }

    }

    companion object {
        const val CONFIG_NAME = "debug_tools"
    }
}