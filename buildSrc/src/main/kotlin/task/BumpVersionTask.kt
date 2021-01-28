package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import util.versionProps
import util.versionFile

open class BumpVersionTask : DefaultTask() {

    private var newVersionName: String? = null

    @Option(option = "newVersionName", description = "Configures version, that will be set to application versionName after bump")
    fun setNewVersionName(name: String) {
        newVersionName = name
    }

    @TaskAction
    fun bump() {
        if (newVersionName == null) {
            throw NullPointerException("newVersionName is not specified")
        }

        val newVersionCode = (versionProps["kiwiVersionCode"] as String).toInt() + 1

        versionProps.setProperty("kiwiVersionName", newVersionName)
        versionProps.setProperty("kiwiVersionCode", newVersionCode.toString())

        versionProps.store(versionFile.outputStream(), null)
    }
}