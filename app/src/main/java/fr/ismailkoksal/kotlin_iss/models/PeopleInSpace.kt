// To parse the JSON, install kotlin's serialization plugin and do:
//
// val json          = Json(JsonConfiguration.Stable)
// val peopleInSpace = json.parse(PeopleInSpace.serializer(), jsonString)

package fr.ismailkoksal.kotlin_iss.models

import kotlinx.serialization.*

@Serializable
data class PeopleInSpace (
        val message: String,
        val number: Long,
        val people: List<Person>
)

@Serializable
data class Person (
        val craft: String,
        val name: String
)
