package uz.rdu.ucell_utolov.dagger_implementations.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.dagger_implementations.scope.PerApplication
import uz.rdu.ucell_utolov.interfaces.api.*
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val mainApplication: MainApplication) {


    @Singleton
    @Provides //scope is not necessary for parameters stored within the module]
    fun context(): Context {
        return mainApplication.applicationContext
    }

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return mainApplication
    }


    @Provides
    @Singleton
    fun ApiProfileService(context:Context): ApiProfileInterface {
        return ApiProfileInterface.create(context)
    }

    @Provides
    fun ApiMerchantsService(context:Context): ApiMerchantInterface {
        return ApiMerchantInterface.create(context)
    }

    @Provides
    fun ApiP2PService(context: Context): ApiP2PPerformInterface{
        return ApiP2PPerformInterface.create(context)
    }

    @Provides
    fun ApiTransactionHistoryService(context: Context): ApiTransactionHistoryInteraface{
        return ApiTransactionHistoryInteraface.create(context)
    }


    @Provides
    fun ApiUcellInteraface(context: Context):ApiUcellInteraface{
        return ApiUcellInteraface.create(context)
    }


    @Provides
    fun ApiArticleInterface(context: Context):ApiArticleInterface{
        return ApiArticleInterface.create(context)
    }
}

