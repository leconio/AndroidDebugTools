package io.lecon.debugtools.plugin.domain

open class Database(var name: String?) {
    var path: String? = ""
    var password: String? = null

    override fun toString(): String {
        return "Database(name='$name', path='$path', password=$password)"
    }


}