package com.example.ardrawing.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.imageLoader
import coil.request.ImageRequest
import com.example.ardrawing.MainActivity
import com.example.ardrawing.R
import com.example.ardrawing.adapters.ParentAdapter
import com.example.ardrawing.data.ArDrawingData
import com.example.ardrawing.data.CategoryModel
import com.example.ardrawing.databinding.FragmentHomeBinding
import com.example.ardrawing.utils.ArDrawingSharePreference
import com.example.ardrawing.utils.CommonUtils
import com.example.ardrawing.utils.ImageHolder
import com.example.ardrawing.utils.ImageUrlList.allUrlList
import com.example.ardrawing.utils.PermissionHandler
import kotlinx.coroutines.launch
import kotlin.collections.List
import kotlin.random.Random


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var galleryLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private lateinit var permissionHandler: PermissionHandler
    private lateinit var sharePreference: ArDrawingSharePreference

    private val readStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
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


    /*

        private val multiplePermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val allGranted = permissions.all { it.value }

                if (allGranted) {
                    Toast.makeText(requireContext(), "Permissions granted ", Toast.LENGTH_SHORT).show()
                } else {
                    val requestCount = sharePreference.getInt()

                    when (requestCount) {
                        0 -> {
                            sharePreference.saveInt(1)
                            permissionHandler.showRetryDialog()
                        }

                        else -> {
                            permissionHandler.showSettingsDialog()
                        }
                    }
                }
            }
    */

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

        sharePreference = ArDrawingSharePreference(requireContext())

        // Request permissions
        /*  permissionHandler.requestPermission()*/
        CommonUtils.loadNativeAd(requireView(), requireContext())

        galleryLauncher = CommonUtils.registerGalleryPicker(this) { uri ->
            uri?.let {
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                ImageHolder.pickLocation = "gallery"

                ImageHolder.bitmap = bitmap


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

                    ImageHolder.bitmap = bitmap
                    ImageHolder.pickLocation = "camera"
                    val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Capture failed", Toast.LENGTH_SHORT).show()
                }
            }

        val urlList = allUrlList()


        binding.moreDrawer.setOnClickListener {

            (activity as? MainActivity)?.openDrawer()
        }

        binding.billingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_subscriptionFragment)
        }

        binding.quickStartBtn.setOnClickListener {
            val randomImage = Random.nextInt(urlList.size)
            val randomElement = urlList[randomImage].favouritefavouriteUrl
            lifecycleScope.launch {
                val bitmp = urlToBitmap(randomElement, requireContext())
                ImageHolder.bitmap = bitmp
            }
            val action = HomeFragmentDirections.actionHomeFragmentToSelectionModeFragment()
            findNavController().navigate(action)
        }
        binding.viewNowBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment()
            findNavController().navigate(action)
        }

        binding.gallerylinearLayout.setOnClickListener {
            permissionHandler = PermissionHandler(requireContext(), readStoragePermissionLauncher)

            if (permissionHandler.isReadMediaImagesGranted()) {
                CommonUtils.pickImageFromGallery(galleryLauncher)
            } else {
                permissionHandler.requestReadMediaImagesPermission()
            }
        }

        binding.cameraBtnLayout.setOnClickListener {
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
                permissionHandler.requestCameraPermission()
            }

        }


        /*    val includedLayout = binding.catogoriesSection

            includedLayout.seeAll1.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToViewCategoryFragment("Anime")
                findNavController().navigate(action)
            }

            includedLayout.seeAll2.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToViewCategoryFragment("Anatomy")
                findNavController().navigate(action)
            }



            includedLayout.seeAll3.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToViewCategoryFragment("Object")
                findNavController().navigate(action)
            }*/
        val categoryList = listOf(
            CategoryModel(
                "Birds",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/birds/birds03.png")
                )
            ),
            CategoryModel(
                "Boats",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/boats/boats03.png")
                )
            ),
            CategoryModel(
                "Characters",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/character/character03.png")
                )
            ),
            CategoryModel(
                "Trees",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/tree/tree03.png")
                )
            ),
            CategoryModel(
                "Vegetables",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/vegetables/vegetable03.png")
                )
            ),
            CategoryModel(
                "Animals",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Animals/animals03.png")
                )
            ),
            CategoryModel(
                "Bacteria",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria1.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria2.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Bacteria's/bacteria3.png")
                )
            ),
            CategoryModel(
                "Fruits",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Fruits/fruits03.png")
                )
            ),
            CategoryModel(
                "Human Organs",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Human%20Organs/humanorgans03.png"),

                    )
            ),
            CategoryModel(
                "Pirates",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Pirats/pirats03.png"),
                )
            ),
            CategoryModel(
                "Plants",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Plants/plants03.png"),
                )
            ),
            CategoryModel(
                "Toys",
                listOf(
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy01.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy02.png"),
                    ArDrawingData("https://raw.githubusercontent.com/saeedsharifmobologics-bit/arDrawingImages/main/Toys/toy03.png"),
                )
            )
        )

        binding.parentRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ParentAdapter(categoryList) { categoryName ->
                val action= HomeFragmentDirections.actionHomeFragmentToViewCategoryFragment(categoryName)
                findNavController().navigate(action)
            }
        }

    }

    suspend fun urlToBitmap(url: String, context: Context): Bitmap? {
        return context.imageLoader.execute(
            ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // if you need to manipulate the bitmap
                .build()
        ).drawable?.toBitmap()
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}