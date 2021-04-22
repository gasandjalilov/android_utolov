package uz.rdu.ucell_utolov

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.Tag
import android.nfc.tech.NfcF
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.media.session.MediaButtonReceiver.handleIntent
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.*
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.models.User
import uz.rdu.ucell_utolov.models.renewTokenResponse
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@Module()
class MainActivity : AppCompatActivity() {

    lateinit var sharedPrefHelper: SharedPrefHelper
    var timeOfStop: MutableLiveData<Long> = MutableLiveData()

    private var intentFiltersArray: Array<IntentFilter>? = null
    private val techListsArray = arrayOf(arrayOf(NfcF::class.java.name))
    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }
    private var pendingIntent: PendingIntent? = null


    override fun onStop() {
        sharedPrefHelper.setTimeOfStop()
        var minutes = runBlocking {
            timeOfStop.value = sharedPrefHelper.getTimeOfStop()
        }
        super.onStop()
    }

    @Provides
    fun provideActivity(): Activity? {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        sharedPrefHelper = SharedPrefHelper(applicationContext)
        runBlocking {
            timeOfStop.value = sharedPrefHelper.getTimeOfStop()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }


    private fun updateBaseContextLocale(context: Context?): Context? {

        if (context == null) {
            var context = this
        }
        var language: String? =
            SharedPrefHelper(context!!).getSelectedLang()
        if (language.isNullOrEmpty()) {
            language = "ru"
        }
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else
            updateResourcesLocaleLegacy(context, locale)
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        var configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        var resources = context.resources
        var configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }


    override fun onResume() {
        var user:AdvUser? = null
        runBlocking {
            user = sharedPrefHelper.getUserObject()
        }

        Log.i("user", user.toString())
        if (user != null && user?.status == "ACTIVE") {
            updateUser()
            var nav = findNavController(R.id.nav_host_fragment)
            nav.navigateUp()
            var curTime = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
            var resultOfCalc = curTime - timeOfStop.value!!
            if (resultOfCalc > 10) {
                nav.navigate(R.id.pinAuthFragment)

                super.onResume()
            } else {
                super.onResume()
            }

        } else {
            super.onResume()
        }

    }

    override fun onRestart() {
        Log.d("Activity on resume", "Resumed")
        updateUser()
        super.onRestart()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i("TAG", "onNewIntent")
        val action = intent?.action
    }


    fun onNfcPressed() {
        pendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val ndef = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)

        Log.i("NFC Pressed", "Main Enabled ")
        pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(this.applicationContext, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        try {
            if (nfcAdapter == null) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("This device doesn't support NFC.")
                builder.setPositiveButton("Cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()

            } else if (!nfcAdapter!!.isEnabled) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("NFC Disabled")
                builder.setMessage("Plesae Enable NFC")

                builder.setPositiveButton("Settings") { _, _ -> startActivity(Intent(Settings.ACTION_NFC_SETTINGS)) }
                builder.setNegativeButton("Cancel", null)
                val myDialog = builder.create()
                myDialog.setCanceledOnTouchOutside(false)
                myDialog.show()
            } else {
                try {
                    ndef.addDataType("text/plain")
                } catch (e: IntentFilter.MalformedMimeTypeException) {
                    throw RuntimeException("fail", e)
                }
                intentFiltersArray = arrayOf(ndef)

            }
        } catch (e: Exception) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("ERROR")
            builder.setMessage("NFC action error.")
        }
    }


    fun updateUser() {

        runBlocking {
            var user: renewTokenResponse? = AuthHelper.getToken(applicationContext)
            var objString = SharedPrefHelper(applicationContext).getUserObject()
            objString?.token = user?.token
            SharedPrefHelper(applicationContext).saveUserObject(objString)
            Log.d("RenewToken",objString?.token)
        }

    }
}