package uz.rdu.ucell_utolov

import android.annotation.TargetApi
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.util.Log
import uz.rdu.ucell_utolov.dagger_implementations.component.ApplicationComponent
import uz.rdu.ucell_utolov.dagger_implementations.component.DaggerApplicationComponent
import uz.rdu.ucell_utolov.dagger_implementations.module.ApplicationModule
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import java.util.*
import javax.inject.Singleton


@Singleton
class MainApplication:Application() {

    override fun onCreate() {
        Log.i("Current Language",SharedPrefHelper(this).getSelectedLang())
        super.onCreate()
        instance = this
        setup()
        component = getApplicationComponent()
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }


    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    companion object {
        lateinit var instance: MainApplication private set
        lateinit var component: ApplicationComponent
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }


    private fun updateBaseContextLocale(context: Context?): Context? {

        if(context==null){
            var context = this
        }
        var language: String? =
            SharedPrefHelper(context!!).getSelectedLang()
        if(language.isNullOrEmpty()) {
            language="ru"
        }
        val locale = Locale(language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else
            updateResourcesLocaleLegacy(context, locale)
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun updateResourcesLocale(context:Context, locale:Locale) :Context{
        var configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLocaleLegacy(context:Context, locale:Locale):Context {
        var resources = context.resources
        var configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }

}