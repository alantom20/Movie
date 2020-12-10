package com.home.movies.data

import androidx.room.*

@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: Record)
    @Query("select * from Record")
    fun getAll() : List<Record>
    @Query("select * from Record WHERE id = :movieId")
    suspend fun findByMovieId(movieId: String?): Record
    @Delete
    suspend fun delete(record: Record?)

}