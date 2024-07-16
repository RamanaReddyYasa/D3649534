package uk.ac.tees.mad.d3649534.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.d3649534.data.database.MedicineAppDatabase
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepository
import uk.ac.tees.mad.d3649534.data.repository.MedicationRepositoryImpl
import javax.inject.Singleton

//Dagger _ Hilt

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext app: Context) =
        Room
            .databaseBuilder(
                app,
                MedicineAppDatabase::class.java,
                "medicine_reminder_db"
            )
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideRepo(db: MedicineAppDatabase): MedicationRepository =
        MedicationRepositoryImpl(dao = db.getMedicineDao())

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

}