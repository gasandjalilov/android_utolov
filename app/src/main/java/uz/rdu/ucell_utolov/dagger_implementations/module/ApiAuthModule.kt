package uz.rdu.ucell_utolov.dagger_implementations.module

import dagger.Module
import dagger.Provides
import uz.rdu.ucell_utolov.interfaces.api.ApiAuthInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface

@Module
class ApiAuthModule {

    @Provides
    fun ApiMethodsService(): ApiAuthInterface {
        return ApiAuthInterface.create()
    }

}