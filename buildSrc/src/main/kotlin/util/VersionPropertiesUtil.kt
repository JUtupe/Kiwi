package util

import java.io.File
import java.util.*

val versionFile = File("versions.properties")
val versionProps = Properties().apply {
    load(versionFile.inputStream())
}