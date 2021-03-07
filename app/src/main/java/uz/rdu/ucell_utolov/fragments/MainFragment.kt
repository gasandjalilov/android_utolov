package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentMainBinding
import uz.rdu.ucell_utolov.helpers.AuthHelper
import uz.rdu.ucell_utolov.interfaces.api.ApiMerchantInterface
import uz.rdu.ucell_utolov.interfaces.api.ApiProfileInterface
import uz.rdu.ucell_utolov.models.AdvUser
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import java.lang.ref.WeakReference
import java.util.EnumSet.of
import javax.inject.Inject


class MainFragment : Fragment() {

    //getting value as argument
    private val args: MainFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()


    lateinit var MenuFrame: FrameLayout
    lateinit var MenuFrameCard: CardView
    lateinit var animation: Animation
    lateinit var menuButton: FloatingActionButton
    lateinit var bottomAppBar: BottomAppBar

    var menuBoolean:Boolean = false
    @Inject
    lateinit var applicationModule: ApiProfileInterface

    @Inject
    lateinit var merchantModule: ApiMerchantInterface


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var user: AdvUser = args.userObject
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
        bottom.menu.getItem(2).isEnabled = false
        bottom.background = null
        bottom.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                mainViewModel.frameVisibility.set(View.GONE)
                menuBoolean=false

            }

        })
        NavigationUI.setupWithNavController(bottom, navController)

        MenuFrame = mainBinding.root.findViewById(R.id.fragment_main_frame) as FrameLayout
        menuButton = mainBinding.fabMenuMain

        menuButton.setOnClickListener{
            if(menuBoolean){
                mainViewModel.frameVisibility.set(View.GONE)
                menuBoolean=false
            }
            else{
                mainViewModel.frameVisibility.set(View.VISIBLE)
                menuBoolean=true
            }
        }

        val radius = resources.getDimension(R.dimen.corner_radius_bottom_navigation_view)

        bottomAppBar = mainBinding.bottomAppBar
        val bottomBarBackground: MaterialShapeDrawable =
            bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .build()


        MenuFrameCard = mainBinding.root.findViewById(R.id.fragment_main_frame_card) as CardView
        MenuFrameCard.setBackgroundResource(R.drawable.rounded_corners_menu_button)

        var newData = AuthHelper.getToken(requireContext())
        var token = newData?.token
        //WebsocketConnector(requireContext()).connectStomp(token!!,user.username!!)


        return mainBinding.root
    }


    fun setupWithNavController(
        bottomNavigationView: BottomNavigationView,
        navController: NavController
    ) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.menuFragment) {
                mainViewModel.frameVisibility.set(View.VISIBLE)
                //MenuFrame.visibility=View.VISIBLE;
                if (!item.isChecked) MenuFrameCard.startAnimation(animation)
            } else {
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

