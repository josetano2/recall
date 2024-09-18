package edu.bluejack23_2.recall.ui.util.algorithm

import android.util.Log
import edu.bluejack23_2.recall.model.supermemo.SupermemoMetadata
import kotlin.math.roundToInt

class Supermemo {

    fun supermemo2(grade: Grade, repetition: Int, easinessFactor: Double, interval: Int): SupermemoMetadata {
        var intervalVar = interval
        var repetitionVar = repetition
        var easinessFactorVar = easinessFactor
        Log.d("REP-",repetitionVar.toString())
        val gradeValue = when (grade) {
            Grade.Again -> 0
            Grade.Hard -> 1
            Grade.Good -> 2
            Grade.Easy -> 3
        }

        if (gradeValue >= 2) {
            intervalVar = when (repetitionVar) {
                0 -> 1
                1 -> 6
                else -> (intervalVar * easinessFactorVar).roundToInt().toInt()
            }
            repetitionVar++
        } else {
            repetitionVar = 0
            intervalVar = 1
        }

        easinessFactorVar += 0.1 - (5 - grade.ordinal) * (0.08 + (5 - grade.ordinal) * 0.02)

        if (easinessFactorVar < 1.3) {
            easinessFactorVar = 1.3
        }
        Log.d("REP+",repetitionVar.toString())

        return SupermemoMetadata(easinessFactorVar, repetitionVar, intervalVar)
    }

    enum class Grade {
        Again, Hard, Good, Easy
    }
}
