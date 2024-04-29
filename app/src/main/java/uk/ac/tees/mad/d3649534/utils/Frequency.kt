package uk.ac.tees.mad.d3649534.utils

enum class Frequency {
    Daily,
    Weekly,
    Monthly
}

fun getRecurrenceList(): List<Frequency> {
    val recurrenceList = mutableListOf<Frequency>()
    recurrenceList.add(Frequency.Daily)
    recurrenceList.add(Frequency.Weekly)
    recurrenceList.add(Frequency.Monthly)

    return recurrenceList
}