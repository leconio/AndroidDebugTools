package io.lecon.debugtools.plugin.domain

import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator

open class DatabaseFactory(private val instantiate: Instantiator) : NamedDomainObjectFactory<Database> {
    override fun create(name: String?): Database {
        return instantiate.newInstance(Database::class.java, name)
    }
}
