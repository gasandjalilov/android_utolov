package uz.rdu.ucell_utolov.dagger_implementations.component

import dagger.Component
import uz.rdu.ucell_utolov.MainApplication
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiAuthModule
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiPaymentModule
import uz.rdu.ucell_utolov.dagger_implementations.module.ApiPinModule
import uz.rdu.ucell_utolov.dagger_implementations.module.ApplicationModule
import uz.rdu.ucell_utolov.fragments.*
import uz.rdu.ucell_utolov.helpers.WebsocketConnector
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.modelviews.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        ApplicationModule::class,
        ApiAuthModule::class,
        ApiPinModule::class,
        ApiPaymentModule::class
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
    fun inject(homeFragment: HomeFragment)

    fun inject(profileApi: ApiProfileInterface)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(phonePinFragment: PhonePinFragment)
    fun inject(pinViewModel: PinViewModel)
    fun inject(cardConfigurationViewModel: CardConfigurationViewModel)
    fun inject(mobileCarrierPaymentViewModel: MobileCarrierPaymentViewModel)
    fun inject(merchantFragment: MerchantFragment)
    fun inject(payByMerchantViewModel: PayByMerchantViewModel)
    fun inject(transferViewModel: TransferViewModel)
    fun inject(homeViewModel: HomeViewModel)
    fun inject(websocketConnector: WebsocketConnector)

}