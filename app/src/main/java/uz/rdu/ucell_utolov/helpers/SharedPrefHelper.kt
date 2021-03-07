package uz.rdu.ucell_utolov.helpers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import java.util.*
import java.util.Base64.getDecoder
import java.util.concurrent.TimeUnit


class SharedPrefHelper(context: Context) {

    val context = context

    fun setTimeOfStop() = GlobalScope.async {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "user",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().putLong("time",TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())).commit()
    }

    suspend fun getTimeOfStop(): Long?{
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "user",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return sharedPreferences.getLong("time", TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()))
    }

    fun getSelectedLang(): String? {
        val sharedPreferences = context.getSharedPreferences("app_language", Context.MODE_PRIVATE)
        return sharedPreferences.getString("lang", "ru")
    }

    fun setSelectedLang(locale: String) {
        val sharedPreferences = context.getSharedPreferences("app_language", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("lang", locale).apply()
        sharedPreferences.edit().commit()
    }

    fun setToken(token:String) {
        val sharedPreferences = context.getSharedPreferences("utolov_token", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", token).apply()
        sharedPreferences.edit().commit()
    }

    fun getToken():String? {
        val sharedPreferences = context.getSharedPreferences("utolov_token", Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", "")
    }

    fun savePin(pin: String) = GlobalScope.async {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "pin",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        sharedPreferences.edit().putString("pin", pin).commit()
    }

    suspend fun validatePin(pin: String):Boolean{
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "pin",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        var coorect = sharedPreferences.getString("pin", "")
        return pin.equals(coorect)
    }

    suspend fun getPin():String?{
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "pin",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        var coorect = sharedPreferences.getString("pin", "")
        return coorect
    }

    fun saveUserObject(userObject: AdvUser?) = GlobalScope.async {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "user",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit().putString("user", Gson().toJson(userObject)).commit()
    }

    fun getUserObject(): AdvUser? {
        return kotlin.run {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
                "user",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            var user = sharedPreferences.getString("user", "")

            Gson().fromJson(user, AdvUser::class.java)
        }

    }

}

