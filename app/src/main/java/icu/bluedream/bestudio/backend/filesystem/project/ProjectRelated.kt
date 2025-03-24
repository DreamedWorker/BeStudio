package icu.bluedream.bestudio.backend.filesystem.project

import icu.bluedream.bestudio.backend.filesystem.CommonFileApiHelper
import icu.bluedream.bestudio.backend.model.DreamManifest
import kotlinx.serialization.json.Json
import java.io.File

fun CommonFileApiHelper.makeProjectPath(path: String): Result<Boolean> {
    val file = File(path)
    if (file.exists()) {
        return Result.failure(FileAlreadyExistsException(file))
    }
    return Result.success(file.mkdirs())
}

fun CommonFileApiHelper.writeDreamManifest(path: String, context: DreamManifest) {
    File(path).writeText(Json.encodeToString(context))
}