package uz.rdu.ucell_utolov.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import uz.rdu.ucell_utolov.R.*

@SuppressLint("ResourceAsColor")
class OverlayProgress(context: Context?, attrs: AttributeSet?) : FrameLayout(context!!, attrs) {
      init {
        setBackgroundColor(color.colorPrimary)
        alpha = 0.6f
        setOnTouchListener { v, event -> true }
      }
}