package uz.rdu.ucell_utolov.helpers

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.BindingAdapter
import androidx.transition.TransitionManager
import uz.rdu.ucell_utolov.modelviews.PhonePinViewModel

object AnimationHelper {

    @JvmStatic
    @BindingAdapter("animationprogress")
    fun animation_progress(@NonNull view: View, isVisible: Boolean) {
        Log.d(PhonePinViewModel::class.java.simpleName, "ANIMATION")
        TransitionManager.beginDelayedTransition(view.rootView as ViewGroup)
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}