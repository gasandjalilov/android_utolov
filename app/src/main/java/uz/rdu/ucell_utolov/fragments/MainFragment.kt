package uz.rdu.ucell_utolov.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentMainBinding
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import java.lang.ref.WeakReference


class MainFragment : Fragment() {

    //getting value as argument
    private val args: MainFragmentArgs by navArgs()
    lateinit var mainViewModel:MainViewModel
    lateinit var MenuFrame:FrameLayout
    lateinit var MenuFrameCard: CardView
    lateinit var animation: Animation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //UserObject get
        var user:AdvUser = args.userObject
        mainViewModel = MainViewModel()
        var mainBinding: FragmentMainBinding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        animation = AnimationUtils.loadAnimation(
            context,
            R.anim.image_animation
        )

        //The code bellow is navigation controller implementation
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        mainViewModel.setThisNavController(navController)

        mainBinding.vmodel = mainViewModel

        val bottom = mainBinding.root.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        setupWithNavController(bottom, navController)

        MenuFrame = mainBinding.root.findViewById(R.id.fragment_main_frame) as FrameLayout
        MenuFrameCard = mainBinding.root.findViewById(R.id.fragment_main_frame_card) as CardView
        MenuFrameCard.setBackgroundResource(R.drawable.rounded_corners_menu_button)



        return mainBinding.root
    }




        fun setupWithNavController(
            bottomNavigationView: BottomNavigationView,
            navController: NavController
        ) {
            bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                if(item.itemId == R.id.menuFragment){
                    mainViewModel.frameVisibility.set(View.VISIBLE)
                    //MenuFrame.visibility=View.VISIBLE;
                    if(!item.isChecked) MenuFrameCard.startAnimation(animation)
                }
                else{
                    NavigationUI.onNavDestinationSelected(
                        item,
                        navController
                    )
                    mainViewModel.frameVisibility.set(View.GONE)


                }
                true
            }

            val weakReference = WeakReference(bottomNavigationView)
            navController.addOnDestinationChangedListener(
                object : OnDestinationChangedListener {
                    override fun onDestinationChanged(
                        controller: NavController,
                        destination: NavDestination, arguments: Bundle?
                    ) {


                        val view = weakReference.get()
                        if (view == null) {
                            navController.removeOnDestinationChangedListener(this)
                            return
                        }
                        val menu = view.menu
                        var h = 0
                        val size = menu.size()
                        while (h < size) {
                            val item = menu.getItem(h)
                            if (matchDestination(destination, item.itemId)) {
                                item.isChecked = true
                            }
                            h++
                        }
                    }
                })
        }



    fun matchDestination(
        destination: NavDestination,
        @IdRes destId: Int
    ): Boolean {
        var currentDestination: NavDestination? = destination
        while (currentDestination!!.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent
        }
        return currentDestination.id == destId
    }



}