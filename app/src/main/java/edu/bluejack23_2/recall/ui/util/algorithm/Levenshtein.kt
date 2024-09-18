package edu.bluejack23_2.recall.ui.util.algorithm

class Levenshtein {
    fun computeLevenshteinDistance(str1: String, str2: String): Int {
        if (str1.isEmpty()) {
            return str2.length
        }

        if (str2.isEmpty()) {
            return str1.length
        }

        val replace = computeLevenshteinDistance(str1.substring(1), str2.substring(1)) +
                numOfReplacement(str1[0], str2[0])

        val insert = computeLevenshteinDistance(str1, str2.substring(1)) + 1

        val delete = computeLevenshteinDistance(str1.substring(1), str2) + 1

        return minOf(replace, insert, delete)
    }

    fun numOfReplacement(c1: Char, c2: Char): Int {
        return if (c1 == c2) 0 else 1
    }
}