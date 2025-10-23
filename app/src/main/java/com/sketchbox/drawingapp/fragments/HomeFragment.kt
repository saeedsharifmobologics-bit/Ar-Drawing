package com.sketchbox.drawingapp.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.imageLoader

import coil.request.ImageRequest
import com.sketchbox.drawingapp.MainActivity
import com.sketchbox.drawingapp.R
import com.sketchbox.drawingapp.adapters.MultiViewTypeAdapter
import com.sketchbox.drawingapp.adsManger.ScreenStatusLogs
import com.sketchbox.drawingapp.adsManger.Utils
import com.sketchbox.drawingapp.databinding.FragmentHomeBinding
import com.sketchbox.drawingapp.utils.ArDrawingSharePreference
import com.sketchbox.drawingapp.utils.CommonUtils
import com.sketchbox.drawingapp.utils.CommonUtils.getRotateAnticlockwiseAnimation
import com.sketchbox.drawingapp.utils.CommonUtils.getRotateClockwiseAnimation
import com.sketchbox.drawingapp.utils.ImageUrlList.allUrlList
import com.sketchbox.drawingapp.utils.ImageUrlList.createMainCategoryList
import com.sketchbox.drawingapp.utils.PermissionHandler
import com.sketchbox.drawingapp.adsManger.adsUtils.loadNativeAd
import com.sketchbox.drawingapp.adsManger.adsUtils.preloadInterstitialAd
import com.sketchbox.drawingapp.adsManger.adsUtils.showInterstitialAd
import com.sketchbox.drawingapp.fragments.CameraLauncher.isCameraFeatureActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

object CameraLauncher {
    var isCameraFeatureActive = false
}

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var sharePreference: ArDrawingSharePreference
    private var pendingAction: (() -> Unit)? = null


    private val readStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                pendingAction?.invoke()
                pendingAction = null

            } else {
                val requestCount = sharePreference.getReadStoragePermissionCount()
                when (requestCount) {
                    0 -> {
                        sharePreference.saveReadStoragePermissionCount(1)
                        permissionHandler.showRetryDialog("GalleryPermission")
                    }

                    else -> {
                        permissionHandler.showSettingsDialog()
                    }
                }
            }
        }


    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
                pendingAction?.invoke()
                pendingAction = null

            } else {
                val requestCount = sharePreference.getCameraPermissionCount()
                when (requestCount) {
                    0 -> {
                        sharePreference.saveCameraPermissionCount(1)
                        permissionHandler.showRetryDialog("CameraPermission")
                    }

                    else -> {
                        permissionHandler.showSettingsDialog()
                    }
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ScreenStatusLogs.logScreenView("HomeFragment", "HomeFragment")

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            Log.d("AdLoader", "Runned Start")
            delay(1000)
            if (!Utils.subscriptionState) {
                loadNativeAd(requireView(), requireContext())

            }
            Log.d("AdLoader", "Runned executed")

        }

        preloadInterstitialAd(requireActivity())

        /*
        StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .penaltyFlashScreen() // UI freeze hone pe screen flash karegi
                        .build()
                )

                StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build()
                )*/



        sharePreference = ArDrawingSharePreference(requireContext())

        // Request permissions
        /*  permissionHandler.requestPermission()*/



        galleryLauncher = CommonUtils.registerGalleryPicker(this) { uri ->
            uri?.let {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                CommonUtils.ImageHolder.pickLocation = "gallery"

                CommonUtils.ImageHolder.bitmap = bitmap

                val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
                findNavController().navigate(action)

            } ?: run {
                Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()

            }


        }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && imageUri != null) {
                    val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    CommonUtils.ImageHolder.bitmap = bitmap
                    CommonUtils.ImageHolder.pickLocation = "camera"
                    val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Capture failed", Toast.LENGTH_SHORT).show()
                }
            }


        binding.moreDrawer.setOnClickListener {

            (activity as? MainActivity)?.openDrawer()
        }

        binding.billingImage.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subscriptionFragment)
        }

        binding.quickStartBtn.setOnClickListener {
            showInterstitialAd(requireActivity(), {
                lifecycleScope.launch(Dispatchers.IO) {
                    // Step 1: List load karo background me
                    val urlList = allUrlList()

                    // Check karo list empty to nahi
                    if (urlList.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "No images found!", Toast.LENGTH_SHORT)
                                .show()
                        }
                        return@launch
                    }

                    // Step 2: Random image select karo
                    val randomIndex = Random.nextInt(urlList.size)
                    val randomElement = urlList[randomIndex].favouriteUrl

                    // Step 3: Bitmap load karo
                    val bitmap = urlToBitmap(randomElement, requireContext())
                    CommonUtils.ImageHolder.bitmap = bitmap

                    // Step 4: Navigate back to Main thread pe
                    withContext(Dispatchers.Main) {
                        CommonUtils.ImageHolder.pickLocation = null
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
                        findNavController().navigate(action)
                    }
                }

            })

        }


        binding.viewNowBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()
            findNavController().navigate(action)
        }

        binding.gallerylinearLayout.setOnClickListener {
            pendingAction = null
            permissionHandler = PermissionHandler(requireContext(), readStoragePermissionLauncher)

            if (permissionHandler.isReadMediaImagesGranted()) {
                CommonUtils.pickImageFromGallery(galleryLauncher)
            } else {
                pendingAction = {
                    CommonUtils.pickImageFromGallery(galleryLauncher)

                }

                permissionHandler.requestReadMediaImagesPermission()
            }
        }

        binding.cameraBtnLayout.setOnClickListener {
            isCameraFeatureActive = true

            pendingAction = null
            permissionHandler = PermissionHandler(requireContext(), cameraPermissionLauncher)
            imageUri = CommonUtils.createImageUri(requireContext())
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Failed to create image file", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (permissionHandler.isCameraPermissionGranted()) {
                cameraLauncher.launch(imageUri!!)
            } else {
                pendingAction = {
                    cameraLauncher.launch(imageUri!!)

                }
                permissionHandler.requestCameraPermission()
            }

        }

        val clockwiseRotation = getRotateClockwiseAnimation(requireContext())
        val anticlockwiseRotation = getRotateAnticlockwiseAnimation(requireContext())

        binding.CategerySectionRecyclerView.apply {
            binding.innerProgressBar.startAnimation(clockwiseRotation)
            binding.outerProgressBar.startAnimation(anticlockwiseRotation)
            binding.innerProgressBar.visibility = View.VISIBLE
            binding.outerProgressBar.visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

            lifecycleScope.launch(Dispatchers.IO) {
                delay(700)

                val categoryList = createMainCategoryList(requireContext())

                val adapterItems = mutableListOf<Any>().apply {
                    for (category in categoryList) {
                        add(category.categoryName)
                        add(category.images)
                    }
                }

                withContext(Dispatchers.Main) {
                    binding.innerProgressBar.visibility = View.GONE
                    binding.outerProgressBar.visibility = View.GONE

                    val adapter = MultiViewTypeAdapter(adapterItems)
                    binding.CategerySectionRecyclerView.adapter = adapter
                }
            }

        }

    }


    suspend fun urlToBitmap(url: String, context: Context): Bitmap? =
        withContext(Dispatchers.IO) {
            context.imageLoader.execute(
                ImageRequest.Builder(context)
                    .data(url)
                    .allowHardware(false)
                    .build()
            ).drawable?.toBitmap()
        }


}

