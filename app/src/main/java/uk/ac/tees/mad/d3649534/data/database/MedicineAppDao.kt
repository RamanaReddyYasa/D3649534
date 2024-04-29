package uk.ac.tees.mad.d3649534.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface MedicineAppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMedication(medicationEntity: MedicationEntity)

    @Query("select * from MedicationEntity")
    fun getAllMedicines(): Flow<List<MedicationEntity>>

    @Query("""
        SELECT * 
    FROM MedicationEntity 
    WHERE medId=:medKey
    """)
    fun getMedicationByKey(medKey: String): Flow<List<MedicationEntity>>

    @Query("DELETE from MedicationEntity WHERE medId=:medId")
    suspend fun deleteMedication(medId: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMedication(medicationEntity: MedicationEntity)

    @Query("SELECT * FROM MedicationEntity WHERE endDate > :date")
    fun getMedicationsForDate(date: Date): Flow<List<MedicationEntity>>


}