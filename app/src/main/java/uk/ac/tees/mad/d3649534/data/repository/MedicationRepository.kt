package uk.ac.tees.mad.d3649534.data.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.d3649534.data.database.MedicationEntity
import uk.ac.tees.mad.d3649534.data.domain.Medication
import java.util.Date

interface MedicationRepository {

    suspend fun insertMedications(medications: List<Medication>)

    suspend fun deleteMedication(medId: String)

    suspend fun updateMedication(medication: Medication)


    fun getMedicationByKey(medKey: String): Flow<List<Medication>>

    fun getAllMedications(): Flow<List<Medication>>

    fun getMedicationsForDate(date: Date): Flow<List<Medication>>
}