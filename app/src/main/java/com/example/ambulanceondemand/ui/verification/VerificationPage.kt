package com.example.ambulanceondemand.ui.verification

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.ambulanceondemand.R
import com.example.ambulanceondemand.databinding.ActivityVerificationPageBinding
import com.example.ambulanceondemand.ui.CameraActivity
import com.example.ambulanceondemand.ui.landing.MapsActivity
import com.example.ambulanceondemand.ui.landing.MapsViewModel
import com.example.ambulanceondemand.util.rotateBitmap
import com.google.tflite.catvsdog.tflite.Classifier
import id.byu.salesagen.external.extension.textContent
import java.io.File

class VerificationPage : AppCompatActivity() {
    private val mInputSize = 350
    private val mModelPath = "converted_model_malam_senin.tflite"
    private val mLabelPath = "labels.txt"
    private lateinit var classifier: Classifier
    private lateinit var verificationViewModel: VerificationViewModel

    companion object {
        const val CAMERA_X_RESULT = 200
    }

    private lateinit var binding: ActivityVerificationPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initClassifier()
        initViews()
        onFieldChange()
        setupViewModel()

        binding.ivUpload.setOnClickListener { startCameraX() }
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setupViewModel() {
        verificationViewModel = ViewModelProvider(this)[VerificationViewModel::class.java]
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
    }

    private fun initViews() {
        findViewById<ImageView>(R.id.ivUpload)

//        runOnUiThread { Toast.makeText(this, result.get(0).title, Toast.LENGTH_SHORT).show() }
    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivUpload.setImageBitmap(result)

            val bitmap = ((binding.ivUpload).drawable as BitmapDrawable).bitmap

            val predict = classifier.recognizeImage(bitmap)

            if (predict.get(0).title == "No_accident"){
                binding.tvStatusAccident.text = "Tidak Tergolong Kecelakaan"
                binding.tvNext.apply {
                    setBackground(R.color.grey)
                }
            }else{
                binding.tvStatusAccident.text = "Tergolong Kecelakaan"
                binding.tvNext.apply {
                    setBackground(R.color.colorPrimary)
                }

                binding.tvNext.setOnClickListener {
                    verificationViewModel.setAmbulances("DKI Jakarta")
                    verificationViewModel.getAmbulances.observe(this){ destination ->
                        val driverName = destination.data?.get(0)?.namaDriver
                        val ambulanceNumber = destination.data?.get(0)?.platNomor
                        val contactPicAmbulance = destination.data?.get(0)?.kontakPicAmbulance
                        val hospitalName = destination.data?.get(0)?.namaInstansi
                        val driverLat = destination.data?.get(0)?.geopoint?._latitude
                        val driverLng = destination.data?.get(0)?.geopoint?._longitude

                        val intentOrderAmbulance = Intent(this@VerificationPage, MapsActivity::class.java).also {
                            it.putExtra(MapsActivity.EXTRA_DRIVER_NAME , driverName)
                            it.putExtra(MapsActivity.EXTRA_HOSPITAL_NAME , hospitalName)
                            it.putExtra(MapsActivity.EXTRA_AMBULANCE_NUMBER , ambulanceNumber)
                            it.putExtra(MapsActivity.EXTRA_CONTACT_PIC_NUMBER , contactPicAmbulance)
                            it.putExtra(MapsActivity.EXTRA_DRIVER_LAT , driverLat)
                            it.putExtra(MapsActivity.EXTRA_DRIVER_LNG , driverLng)

                        }
                        startActivity(intentOrderAmbulance)
                        finish()
                    }
                }

            }

        }
    }

    private fun View.setBackground(resDrawableId: Int) {
        background = ContextCompat.getDrawable(context, resDrawableId)
    }

    private fun onFieldChange() {
        binding.apply {
            etPhone.addTextChangedListener {
                val fieldName = etName.textContent().isNotEmpty()
                val fieldPhone = etPhone.textContent().isNotEmpty() && etPhone.textContent().length >= 11
                if ( fieldName && fieldPhone ) {
                    tvNext.apply {
                        setBackground(R.color.colorPrimary)
                    }
                } else {
                    tvNext.apply {
                        setBackground(R.color.grey)
                    }
                }
            }
        }
    }

    private fun startCameraX() {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
    }

}