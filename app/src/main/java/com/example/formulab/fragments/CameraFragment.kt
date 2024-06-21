package com.example.formulab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.formulab.ImageClassifierHelper
import com.example.formulab.MainViewModel
import com.example.formulab.R
import com.example.formulab.databinding.FragmentCameraBinding
import com.google.mediapipe.tasks.vision.core.RunningMode
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraFragment : Fragment(), ImageClassifierHelper.ClassifierListener {
    //Binding
    private var _FragmentCameraBinding: FragmentCameraBinding? = null
    private val fragmentCameraBinding get() = _FragmentCameraBinding!!

    private lateinit var backgroundExecutor: ExecutorService
    private var isCameraReady = false
    private var imageCapture: ImageCapture? = null

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var imageClassifierHelper: ImageClassifierHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _FragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backgroundExecutor = Executors.newSingleThreadExecutor()
        // Start camera preview after the view has been created
        backgroundExecutor.execute {

            imageClassifierHelper = ImageClassifierHelper(
                context = requireContext(),
                runningMode = RunningMode.LIVE_STREAM,
                threshold = viewModel.currentThreshold,
                currentDelegate = viewModel.currentDelegate,
                currentModel = viewModel.currentModel,
                maxResults = viewModel.currentMaxResults,
                imageClassifierListener = this
            )

            fragmentCameraBinding.cameraPreview.post {
                // Set up the camera and its use cases
                startCamera()
            }
        }
    }

    override fun onPause() {
        // save ImageClassifier settings
        viewModel.setModel(imageClassifierHelper.currentModel)
        viewModel.setDelegate(imageClassifierHelper.currentDelegate)
        viewModel.setThreshold(imageClassifierHelper.threshold)
        viewModel.setMaxResults(imageClassifierHelper.maxResults)
        super.onPause()

        // Close the image classifier and release resources
        //backgroundExecutor.execute { imageClassifierHelper.clearImageClassifier() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(fragmentCameraBinding.cameraPreview.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview
                )
                // Set flag indicating that camera is ready
                isCameraReady = true
                updatePreviewVisibility()
            } catch (exc: Exception) {
                // Handle exceptions
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun updatePreviewVisibility() {
        if (isCameraReady) {
            // Fade in animation
            val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            fragmentCameraBinding.cameraPreview.apply {
                //TO FIX: Applying animation
                startAnimation(fadeInAnimation)
                visibility = View.VISIBLE
            }
        } else {
            fragmentCameraBinding.cameraPreview.visibility = View.INVISIBLE
        }
    }

    companion object {
        private const val TAG = "Formula B"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    override fun onDestroyView() {
        _FragmentCameraBinding = null
        super.onDestroyView()
        // Shut down our background executor
        backgroundExecutor.shutdown()
        backgroundExecutor.awaitTermination(
            Long.MAX_VALUE, TimeUnit.NANOSECONDS
        )
    }

    override fun onError(error: String, errorCode: Int) {
        //TODO("Not yet implemented")
    }

    override fun onResults(resultBundle: ImageClassifierHelper.ResultBundle) {
        //TODO("Not yet implemented")
    }
}