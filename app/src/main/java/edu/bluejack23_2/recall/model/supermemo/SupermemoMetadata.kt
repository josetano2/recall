package edu.bluejack23_2.recall.model.supermemo

import edu.bluejack23_2.recall.model.Card

data class SupermemoMetadata(
    val easinessFactor: Double,
    val repetition: Int,
    val interval: Int
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "easinessFactor" to easinessFactor,
            "repetition" to repetition,
            "interval" to interval
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): SupermemoMetadata {
            val easinessFactor = map["easinessFactor"] as? Double ?: 0.0
            val repetition = map["repetition"] as? Int ?: 0
            val interval = map["interval"] as? Int ?: 0

            return SupermemoMetadata(easinessFactor, repetition, interval);
        }
    }
}
