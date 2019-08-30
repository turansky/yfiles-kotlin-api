package com.github.turansky.yfiles.vsdx.correction

import com.github.turansky.yfiles.YCLASS
import com.github.turansky.yfiles.correction.*
import org.json.JSONObject

private val TYPE_MAP = mapOf(
    "IModelItem" to "yfiles.graph.IModelItem",
    "IEdge" to "yfiles.graph.IEdge",
    "ILabel" to "yfiles.graph.ILabel"
)

internal fun applyVsdxHacks(api: JSONObject) {
    val source = VsdxSource(api)

    fixTypes(source)
    fixOptionTypes(source)

    source.types()
        .onEach { it.remove(J_STATIC_METHODS) }
        .onEach { it.remove(J_METHODS) }
        .forEach { it.remove(J_IMPLEMENTS) }
}

private fun fixPackage(source: VsdxSource) {
    source.types()
        .forEach {
            val id = it.getString(J_ID)
            it.put(J_ID, "yfiles.$id")
        }

    source.functionSignatures.apply {
        keySet().toSet().forEach { id ->
            put("yfiles.$id", get(id))
        }
    }
}

private fun JSONObject.fixType() {
    put(J_TYPE, getFixedType(getString(J_TYPE)))
}

private fun getFixedType(type: String): String {
    TYPE_MAP.get(type)?.also {
        return it
    }

    return type
}

private fun fixTypes(source: VsdxSource) {
    source.functionSignatures
        .run {
            keySet().asSequence().map {
                getJSONObject(it)
            }
        }
        .filter { it.has(J_PARAMETERS) }
        .jsequence(J_PARAMETERS)
        .forEach { it.fixType() }
}

private fun fixOptionTypes(source: VsdxSource) {
    source.type("CachingMasterProvider")
        .jsequence(J_CONSTRUCTORS)
        .single()
        .apply {
            parameter("optionsOrNodeStyleType").apply {
                put(J_NAME, "nodeStyleType")
                put(J_TYPE, "$YCLASS<yfiles.styles.INodeStyle>")
            }

            parameter("edgeStyleType")
                .addGeneric("yfiles.styles.IEdgeStyle")
            parameter("portStyleType")
                .addGeneric("yfiles.styles.IPortStyle")
            parameter("labelStyleType")
                .addGeneric("yfiles.styles.ILabelStyle")
        }
}