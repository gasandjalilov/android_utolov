package uz.rdu.ucell_utolov.dagger_implementations.component

import dagger.Component
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiAuthModule
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiPinModule
import uz.rdu.ucell_utolov.dagger_implementations.module.ApplicationModule
import uz.rdu.ucell_utolov.fragments.MainFragment
import uz.rdu.ucell_utolov.fragments.PhonePinFragment
import uz.rdu.ucell_utolov.fragments.PinFragment
import uz.rdu.ucell_utolov.fragments.RegistrationFragment
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.modelviews.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        ApiAuthModule::class,
        ApiPinModule::class
    )
)
interface ApplicationComponent {

    fun inject(application: MainApplication)

    //ViewModels
    fun inject(phonePinView: PhonePinViewModel)
    fun inject(registrationView: RegistrationViewModel)
    fun inject(splashView: StartupFlashScreenViewModel)
    fun inject(pinFragment: PinFragment)
    fun inject(mainViewModel: MainViewModel)
    fun inject(mainFragment: MainFragment)
    fun inject(profileApi: ApiProfileInterface)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(phonePinFragment: PhonePinFragment)
    fun inject(pinViewModel: PinViewModel)
    fun inject(cardConfigurationViewModel: CardConfigurationViewModel)

}