package uz.rdu.ucell_utolov.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import uz.rdu.ucell_utolov.MainActivity
import uz.rdu.ucell_utolov.R
import uz.rdu.ucell_utolov.databinding.FragmentTransferBinding
import uz.rdu.ucell_utolov.helpers.CardNumberTextWatcher
import uz.rdu.ucell_utolov.helpers.ThousandsTextWatcher
import uz.rdu.ucell_utolov.helpers.adapters.HomeCardsAdapter
import uz.rdu.ucell_utolov.helpers.adapters.PaymentCardsAdapter
import uz.rdu.ucell_utolov.modelviews.MainViewModel
import uz.rdu.ucell_utolov.modelviews.TransferViewModel


class TransferFragment : Fragment() {

    lateinit var transferViewModel: TransferViewModel
    lateinit var transferBinding: FragmentTransferBinding
    lateinit var homeCardsAdapter: HomeCardsAdapter
    lateinit var nfc: ImageView
    lateinit var scan: ImageView
    lateinit var camera: SurfaceView
    lateinit var cameraSource: CameraSource



    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        transferViewModel = TransferViewModel()
        transferViewModel.viewContext = requireContext()
        transferBinding = DataBindingUtil.inflate<FragmentTransferBinding>(
            inflater,
            R.layout.fragment_transfer,
            container,
            false
        )
        transferBinding.vmodel = transferViewModel
        nfc = transferBinding.transferNfc
        scan = transferBinding.transferScan
        camera = transferBinding.transferCamera
        transferBinding.amount.visibility = View.GONE
        model.cardListLive.observe(viewLifecycleOwner, Observer {
            transferBinding.viewPager.adapter = PaymentCardsAdapter(
                model.cardListLive.value!!,
                requireContext()
            )
            transferViewModel.cardSliderViewPager = transferBinding.viewPager
            transferBinding.progressBar3.visibility = View.GONE
        })
        transferBinding.amount.addTextChangedListener(ThousandsTextWatcher(transferBinding.amount))
        transferBinding.transferEditTextCard.addTextChangedListener(CardNumberTextWatcher())
        transferBinding.transferEditTextCard.addTextChangedListener {
            if (it?.length == 19)
                transferBinding.amount.visibility = View.VISIBLE
            else {
                transferBinding.amount.visibility = View.GONE
            }
        }
        scan.setOnClickListener {
            onCameraPressed()
        }
        nfc.setOnClickListener {
            onNfcPressed()
        }
        //nfc.visibility= View.GONE
        return transferBinding.root
    }


    fun onCameraPressed() {
        camera.visibility = View.VISIBLE
        transferBinding.transferCardView.visibility = View.GONE
        var recognizer = TextRecognizer.Builder(context).build()
        var cameraSource =
            CameraSource.Builder(context, recognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(640, 480)
                .setAutoFocusEnabled(true)
                .setRequestedFps(2.0f)
                .build()

        camera.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requireActivity().requestPermissions(
                            arrayOf(Manifest.permission.CAMERA),
                            1001
                        )
                        return
                    }
                    cameraSource.start(camera.holder)
                } catch (e: Exception) {

                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

        })

        recognizer.setProcessor(object : Detector.Processor<TextBlock> {

            override fun release() {
            }

            override fun receiveDetections(p0: Detector.Detections<TextBlock>?) {
                val items: SparseArray<TextBlock> = p0!!.detectedItems
                for (i in 0 until items.size()) {

                    var regex = "^(8600|9860|6262) (\\d{4}) (\\d{4}) (\\d{4})".toRegex()
                    val item = items.valueAt(i)
                    if (regex.matches(item.value)) {
                        Log.i("CAMERA", item.value)
                        requireActivity().runOnUiThread {
                            transferBinding.transferEditTextCard.setText(item.value)
                            cameraSource.stop()
                            camera.visibility = View.GONE
                            transferBinding.transferCardView.visibility = View.VISIBLE
                        }
                    }

                }

            }

        })
    }


    fun onNfcPressed() {
        var mainAct = activity as MainActivity?
        mainAct?.onNfcPressed()
    }


}