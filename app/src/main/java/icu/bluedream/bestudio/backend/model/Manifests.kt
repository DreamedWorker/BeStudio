package icu.bluedream.bestudio.backend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MinecraftManifest(
    @SerialName("format_version")
    val formatVersion: Long,
    val header: Header,
    val modules: List<Module>
) {
    @Serializable
    data class Header(
        val description: String,
        val name: String,
        val uuid: String,
        val version: List<Long>,
        @SerialName("min_engine_version")
        val minEngineVersion: List<Long>
    )

    @Serializable
    data class Module (
        val description: String,
        val type: String,
        val uuid: String,
        val version: List<Long>
    )
}

@Serializable
data class DreamManifest(
    val projectName: String,
    val projectDesc: String,
    val minEngineVersion: List<Long>,
    val version: List<Long>,
    val useSapi: Boolean,
    val bpUUID: String,
    val rpUUID: String
)
