package uz.rdu.ucell_utolov.interfaces.db

import androidx.room.*
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction

@Dao
interface TransactionDao {

    @Insert
    fun insertHistory(vararg dbTransaction: DBTransaction)

    @Update
    fun updateHistory(vararg dbTransaction: DBTransaction)

    @Delete
    fun deleteFromHistory(vararg dbTransaction: DBTransaction)


    @Query("SELECT * FROM DBTransaction")
    fun allSaved():Array<DBTransaction>
}