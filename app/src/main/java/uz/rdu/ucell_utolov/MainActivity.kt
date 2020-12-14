package uz.rdu.ucell_utolov

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import uz.rdu.ucell_utolov.fragments.PinAuthFragmentDirections
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.helpers.appauth.BiometricAuthHelper
import java.util.*


class MainActivity : AppCompatActivity() {

    var sharedPrefHelper = SharedPrefHelper(this)

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
        var configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        var resources = context.getResources();
        var configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }


    override fun onResume() {
        var user = sharedPrefHelper.getUserObject()
        if (user != null) {
            var nav = findNavController(R.id.nav_host_fragment)
            nav.navigateUp()
            nav.navigate(R.id.pinAuthFragment)
        }
        super.onResume()
    }

    override fun onRestart() {
        Log.d("Activity on resume", "Resumed")

        super.onRestart()
    }

    override fun onStop() {
        super.onStop()
    }
}