package uk.ac.tees.mad.d3649534.data.domain

import uk.ac.tees.mad.d3649534.data.database.MedicationEntity
import java.util.Date

fun MedicationEntity.toMedication(): Medication {
    return Medication(
        id = id,
        medId = medId,
        name = name,
        dosage = dosage,
        startDate = startDate,
        frequency = frequency,
        endDate = endDate,
        medicationTime = medicationTime,
        medicationTimesList = medicationTimesList,
        medicationTaken = medicationTaken,
        image = image,
    )
}

fun Medication.toMedicationEntity(): MedicationEntity {
    return MedicationEntity(
        id = id,
        medId = medId,
        name = name,
        dosage = dosage,
        startDate = startDate,
        endDate = endDate,
        frequency = frequency,
        medicationTime = medicationTime,
        medicationTimesList = medicationTimesList,
        medicationTaken = medicationTaken,
        image = image
    )
}