package com.example.pondora.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pondora.data.db.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): Flow<UserEntity?>

    @Query("UPDATE user SET xp = xp + :amount WHERE id = 1")
    suspend fun addXp(amount: Int)

    @Query("UPDATE user SET duckCount = duckCount + 1 WHERE id = 1")
    suspend fun addDuck()

    @Query("UPDATE user SET level = :level WHERE id = 1")
    suspend fun updateLevel(level: Int)
}