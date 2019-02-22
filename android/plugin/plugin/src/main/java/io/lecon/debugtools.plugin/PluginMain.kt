package io.lecon.debugtools.plugin

import com.android.build.gradle.*
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.LibraryVariant
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import groovy.util.XmlSlurper
import io.lecon.debugtools.plugin.domain.DebugTools
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle
import java.util.*
import javax.lang.model.element.Modifier
import kotlin.reflect.KClass


/**
 * Created by spawn on 2018/5/21.
 */
class PluginMain : Plugin<Project> {

    private operator fun <T : Any> ExtensionContainer.get(type: KClass<T>): T? {
        try {
            return getByType(type.java)
        } catch (_ : UnknownDomainObjectException) {
            return null
        }
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
        val dbBean: ClassName = ClassName.get("io.lecon.debugtools.db", "DbBean")
        val file: ClassName = ClassName.get("java.io", "File")

        val getExtraDatabases = MethodSpec.methodBuilder("getExtraDatabases")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(HashMap::class.java)

        getExtraDatabases.addStatement("HashMap<String, \$T> map = new HashMap()", dbBean)
        debugTools.databases.all { database ->
            if (database.path != null && database.name != null) {
                getExtraDatabases.addStatement("map.put(\"${database.name}\",new \$T(\"${database.name}\",${database.password},new \$T(\"${database.path}\")))", dbBean, file)
            }
        }
        getExtraDatabases.addStatement("return map")

        val helloWorld = TypeSpec.classBuilder("ExtraDatabaseBuilder")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(getExtraDatabases.build())
                .build()

        val javaFile = JavaFile.builder("io.lecon.debug_tools", helloWorld)
                .build()

        project.extensions[AppExtension::class].let {
            it?.let { extension ->
                extension.applicationVariants.all { variant->
                  getConfig(variant,javaFile)
              }
            }
        }

        project.extensions[LibraryExtension::class].let {
            it?.let { extension ->
                extension.libraryVariants.all { variant ->
                    getConfig(variant,javaFile)
                }
            }
        }
    }

    private fun getConfig(variant: BaseVariant,javaFile: JavaFile) {
        val outputDir = project.buildDir.resolve(
                "generated/source/debug_tools/${variant.dirName}")
        javaFile.writeTo(outputDir)
        variant.outputs.all {
            project.tasks.create("generate${variant.name.capitalize()}ExtraDatabaseBuilder") {
                variant.registerJavaGeneratingTask(it, outputDir)
            }
        }
    }

    private fun getPackageName(variant: BaseVariant): String {
        val slurper = XmlSlurper(false, false)
        val list = variant.sourceSets.map { it.manifestFile }

        // According to the documentation, the earlier files in the list are meant to be overridden by the later ones.
        // So the first file in the sourceSets list should be main.
        val result = slurper.parse(list[0])
        return result.getProperty("@package").toString()
    }


    companion object {
        const val CONFIG_NAME = "debug_tools"
    }
}