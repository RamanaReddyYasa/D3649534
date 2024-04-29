package uk.ac.tees.mad.d3649534.data.repository

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface MedicationRepositoryEntryPoint {
    fun medicationRepository(): MedicationRepository
}