// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json        = Json(JsonConfiguration.Stable)
// val iSSLocation = json.parse(ISSLocation.serializer(), jsonString)

package fr.ismailkoksal.kotlin_iss.models

import kotlinx.serialization.*

@Serializable
data class IssLocation (
        @SerialName("iss_position")
        val issPosition: IssPosition,

        val message: String,
        val timestamp: Long
)

@Serializable
data class IssPosition (
        val longitude: String,
        val latitude: String
)
