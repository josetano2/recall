package edu.bluejack23_2.recall.model

data class Card (
    var id : String,
    var question : String,
    var answer : String,
    var additionalInfo : List<String>
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "question" to question,
            "answer" to answer,
            "additionalInfo" to additionalInfo,
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any>): Card {
            val id = map["id"] as? String ?: ""
            val question = map["question"] as? String ?: ""
            val answer = map["answer"] as? String ?: ""
            val additionalInfo = map["additionalInfo"] as? List<String> ?: emptyList()
            return Card(id, question, answer, additionalInfo)
        }
    }
}