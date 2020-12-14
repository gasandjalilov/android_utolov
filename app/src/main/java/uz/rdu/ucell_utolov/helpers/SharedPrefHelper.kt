package uz.rdu.ucell_utolov.helpers

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import java.util.Base64.getDecoder


class SharedPrefHelper(context: Context) {

    val context = context


    fun getSelectedLang(): String? {
        val sharedPreferences = context.getSharedPreferences("app_language", Context.MODE_PRIVATE)
        return sharedPreferences.getString("lang", "ru")
    }

    fun setSelectedLang(locale: String) {
        val sharedPreferences = context.getSharedPreferences("app_language", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("lang", locale).apply()
        sharedPreferences.edit().commit()
    }

    fun getUser(): User {
        val sharedPreferences = context.getSharedPreferences("utolov_user", Context.MODE_PRIVATE)
        var user: User = User()
        user.username = sharedPreferences.getString("username", null)
        user.password = sharedPreferences.getString("password", null)
        Log.d("getUser: ", user.toString())
        return user
    }

    fun setUser(user: User) {
        val sharedPreferences = context.getSharedPreferences("utolov_user", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("username", user.username).apply()
        sharedPreferences.edit().putString("password", user.password).apply()
        sharedPreferences.edit().commit()
        Log.d("setUser: ", user.toString())

    }


    fun savePin(pin: String){
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

    fun validatePin(pin: String):Boolean{
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

    fun saveUserObject(userObject: AdvUser?){
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
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
            "user",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        var user = sharedPreferences.getString("user", "")

        return Gson().fromJson(user, AdvUser::class.java)
    }

}

