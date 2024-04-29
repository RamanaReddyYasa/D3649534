package uk.ac.tees.mad.d3649534.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.d3649534.data.database.MedicineAppDao
import uk.ac.tees.mad.d3649534.data.domain.Medication
import uk.ac.tees.mad.d3649534.data.domain.toMedication
import uk.ac.tees.mad.d3649534.data.domain.toMedicationEntity
import java.util.Date

class MedicationRepositoryImpl(
    private val dao: MedicineAppDao
) : MedicationRepository {
    override suspend fun insertMedications(medications: List<Medication>) {
        medications.map { it.toMedicationEntity() }.forEach {
            dao.addMedication(it)
        }
    }

    override suspend fun deleteMedication(medId: String) {
        dao.deleteMedication(medId)
    }

    override suspend fun updateMedication(medication: Medication) {
        dao.updateMedication(medication.toMedicationEntity())
    }

    override fun getMedicationByKey(medKey: String): Flow<List<Medication>> {
        return dao.getMedicationByKey(medKey).map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override fun getAllMedications(): Flow<List<Medication>> {
        return dao.getAllMedicines().map { entities ->
            entities.map { it.toMedication() }
        }
    }

    override fun getMedicationsForDate(date: Date): Flow<List<Medication>> {
        return dao.getMedicationsForDate(
            date = date
        ).map { entities ->
            entities.map { it.toMedication() }
        }
    }
}