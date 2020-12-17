package uz.rdu.ucell_utolov.interfaces.db

import androidx.room.*
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse

@Dao
interface ProfileResponseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(vararg card: ProfileResponse)

    @Update
    fun updateCard(vararg card: ProfileResponse)

    @Delete
    fun deleteCard(vararg card: ProfileResponse)

    @Query("SELECT * FROM ProfileResponse WHERE card_number LIKE :cardnumber")
    fun findByCardNumber(cardnumber: String): ProfileResponse

    @Query("SELECT * FROM ProfileResponse")
    fun allCards():Array<ProfileResponse>
}