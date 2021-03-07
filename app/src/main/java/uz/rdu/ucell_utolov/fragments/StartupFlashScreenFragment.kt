package uz.rdu.ucell_utolov.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import kotlinx.coroutines.runBlocking
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.StartupFlashScreenBinding
import uz.rdu.ucell_utolov.helpers.EventObserver
import uz.rdu.ucell_utolov.helpers.SharedPrefHelper
import uz.rdu.ucell_utolov.modelviews.StartupFlashScreenViewModel

class StartupFlashScreenFragment : Fragment() {

    lateinit var startupViewmodel: StartupFlashScreenViewModel
    private lateinit var navController: NavController
    lateinit var startupFlashScreenBinding : StartupFlashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {

        startupViewmodel = StartupFlashScreenViewModel(activity?.application!!)
        startupFlashScreenBinding  = DataBindingUtil.inflate<StartupFlashScreenBinding>(inflater,R.layout.startup_flash_screen,container,false)
        startupFlashScreenBinding.viewmodel = startupViewmodel

        return startupFlashScreenBinding.root
        //return inflater.inflate(R.layout.startup_flash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        checkUser()
        startupViewmodel.navigateScreen.observe(requireActivity(), EventObserver{
            navController.navigate(it as Int)
        })

    }

    fun checkUser(){
        var prefuser = SharedPrefHelper(requireContext())
        runBlocking {
            var user =prefuser.getUserObject()
            if(user != null && user.status.equals("active",true)){
                Log.d("USER",prefuser.getUserObject().toString())
                startupViewmodel.isAnimation.set(false)
                startupViewmodel.progressVisible.set(true)
                navController = Navigation.findNavController(requireView())
                var fragmentDirection = StartupFlashScreenFragmentDirections.actionStartupFlashScreenFragmentToMainFragment(user)
                navController.navigate(fragmentDirection)
            }
        }
    }

}