package dev.jay.gossip.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface UserDAO {
    @Insert( onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Delete()
    suspend fun addRemove(user: User)
}