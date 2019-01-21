package io.lecon.debugtools.plugin.domain

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

open class DebugTools(instantiate: Instantiator, project: Project) {

    /**
     * 插件信息
     */
    var databases: NamedDomainObjectContainer<Database> = project.container(Database::class.java, DatabaseFactory(instantiate))

    fun database(action: Action<NamedDomainObjectContainer<Database>>) {
        action.execute(databases)
    }
}