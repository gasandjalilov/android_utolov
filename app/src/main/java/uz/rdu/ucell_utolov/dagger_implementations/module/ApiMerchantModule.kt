package uz.rdu.ucell_utolov.dagger_implementations.module

import android.content.Context
import dagger.Module
import dagger.Provides
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiTransactionPerform

@Module
class ApiMerchantModule {

    @Provides
    fun ApiMethodsService(context:Context): ApiTransactionPerform {
        return ApiTransactionPerform.create(context)
    }

}