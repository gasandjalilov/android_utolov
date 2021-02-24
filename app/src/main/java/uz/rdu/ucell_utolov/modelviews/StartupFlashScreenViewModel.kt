package uz.rdu.ucell_utolov.modelviews

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.fragments.PhonePinFragmentDirections
import uz.rdu.ucell_utolov.helpers.Event
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.interfaces.Startup_Flash_Screen_Interface
import java.util.*


class StartupFlashScreenViewModel(application: Application):ViewModel(),Startup_Flash_Screen_Interface  {

    var isAnimation = ObservableBoolean(true)
    var language:String = ""
    private val _navigateScreen = MutableLiveData<Event<Any>>()
    val navigateScreen: LiveData<Event<Any>> = _navigateScreen
    val progressVisible = ObservableField(false)
    val application = application




    override fun chooseLanguage() {
        SharedPrefHelper(application).setSelectedLang(language)
        Log.i("Current Language : {}", SharedPrefHelper(application).getSelectedLang())
        _navigateScreen.value = Event(R.id.action_startupFlashScreenFragment_to_registrationFragment)
    }



    override fun uz_language() {
        language="uz"
        chooseLanguage()
    }

    override fun ru_language() {
        language="ru"
        chooseLanguage()
    }

    override fun en_language() {
        language="en"
        chooseLanguage()
    }



}

@BindingAdapter("app:button_animate")
fun button_animate(view: Button, isAnimated: Boolean){
    if (isAnimated) {
        val animation: Animation =
            AnimationUtils.loadAnimation(view.context, R.anim.language_button_animation)
        animation.repeatMode = Animation.ABSOLUTE
        view.startAnimation(animation)
    }
}

@BindingAdapter("app:image_animate")
fun image_animate(view: ImageView, isAnimated: Boolean){
    if (isAnimated) {
        val animation: Animation =
            AnimationUtils.loadAnimation(view.context, R.anim.image_animation)
        animation.repeatMode = Animation.ABSOLUTE
        view.startAnimation(animation)
    }
}

@BindingAdapter("app:edittext_animate")
fun edittext_animate(view: ImageView, isAnimated: Boolean){
    if (isAnimated) {
        val animation: Animation =
            AnimationUtils.loadAnimation(view.context, R.anim.edittext_animation)
        animation.repeatMode = Animation.ABSOLUTE
        view.startAnimation(animation)
    }
}

@BindingAdapter("app:cardview_animate")
fun cardview_animate(view: CardView, isAnimated: Boolean){
    if (isAnimated) {
        val animation: Animation =
            AnimationUtils.loadAnimation(view.context, R.anim.image_animation)
        animation.repeatMode = Animation.ABSOLUTE
        view.startAnimation(animation)
    }
}

@BindingAdapter("app:layout_animate")
fun layout_animate(view: ConstraintLayout, isAnimated: Boolean){
    if (isAnimated) {
        val animation: Animation =
            AnimationUtils.loadAnimation(view.context, R.anim.image_animation)
        animation.repeatMode = Animation.ABSOLUTE
        view.startAnimation(animation)
    }
}