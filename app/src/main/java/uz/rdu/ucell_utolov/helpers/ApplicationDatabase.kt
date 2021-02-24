package uz.rdu.ucell_utolov.helpers

import android.content.Context
import androidx.room.*
import uz.rdu.ucell_utolov.helpers.typeconverters.DateConverter
import uz.rdu.ucell_utolov.interfaces.db.AdvUserDao
import uz.rdu.ucell_utolov.interfaces.db.ProfileResponseDao
import uz.rdu.ucell_utolov.interfaces.db.TransactionDao
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.profilemodels.ProfileResponse
import uz.rdu.ucell_utolov.models.transaction.innertransaction.DBTransaction

@Database(entities = arrayOf(AdvUser::class,ProfileResponse::class,DBTransaction::class), version = 2)
@TypeConverters(DateConverter::class)
abstract class ApplicationDatabase:RoomDatabase() {

    abstract fun advUserDao(): AdvUserDao

    abstract fun profileResponseDao():ProfileResponseDao

    abstract fun transactionDao():TransactionDao


    companion object {
        var INSTANCE: ApplicationDatabase? = null

        fun getAppDataBase(context: Context?): ApplicationDatabase? {
            if (INSTANCE == null){
                synchronized(ApplicationDatabase::class){
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext, ApplicationDatabase::class.java, "utolovDB").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }

    }
}