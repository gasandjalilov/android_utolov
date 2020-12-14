package uz.rdu.ucell_utolov.dagger_implementations.module

import dagger.Module
import dagger.Provides
import uz.rdu.ucell_utolov.interfaces.api.ApiPinInterface

@Module
class ApiPinModule {

    @Provides
    fun ApiPinMehtods(): ApiPinInterface {
        return  ApiPinInterface.create()
    }
}