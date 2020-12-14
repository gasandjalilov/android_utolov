package uz.rdu.ucell_utolov.interfaces.db

import androidx.room.*
import uz.rdu.ucell_utolov.models.AdvUser

@Dao
interface AdvUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg user: AdvUser)

    @Update
    fun updateUser(vararg user: AdvUser)

    @Query("SELECT * FROM AdvUser WHERE username LIKE :username")
    fun findByUsername(username: String): AdvUser

    @Query("SELECT * FROM AdvUser")
    fun allUsers():Array<AdvUser>
}