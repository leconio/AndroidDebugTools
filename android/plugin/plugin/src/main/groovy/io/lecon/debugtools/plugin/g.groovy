package io.lecon.debugtools.plugin

static getG(File path) {
    return {
            srcDirs = [path.getAbsolutePath()]
    }
}