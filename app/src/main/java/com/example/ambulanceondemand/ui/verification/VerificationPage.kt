package com.example.ambulanceondemand.ui.verification

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.ambulanceondemand.R
import com.example.ambulanceondemand.databinding.ActivityVerificationPageBinding
import com.example.ambulanceondemand.ui.CameraActivity
import com.example.ambulanceondemand.ui.landing.MapsActivity
import com.example.ambulanceondemand.ui.landing.MapsViewModel
import com.example.ambulanceondemand.ui.landing.model.ModelPreference
import com.example.ambulanceondemand.util.UserPreference
import com.example.ambulanceondemand.util.ViewModelFactory
import com.example.ambulanceondemand.util.rotateBitmap
import com.google.tflite.catvsdog.tflite.Classifier
import id.byu.salesagen.external.extension.textContent
import java.io.File

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VerificationPage : AppCompatActivity() {
    private val mInputSize = 350
    private val mModelPath = "converted_model_malam_senin.tflite"
    private val mLabelPath = "labels.txt"
    private lateinit var classifier: Classifier
    private lateinit var verificationViewModel: VerificationViewModel

    private lateinit var binding: ActivityVerificationPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        initClassifier()

        binding.ivUpload.setOnClickListener { startCameraX() }
        binding.ivBack.setOnClickListener {
            val intentBack = Intent(this, MapsActivity::class.java)
            startActivity(intentBack)
        }
        binding.tvNext.setOnClickListener {
            Toast.makeText(this, "Maaf, lengkapi form telebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        verificationViewModel = ViewModelProvider(this, ViewModelFactory(UserPreference.getInstance(dataStore))
        )[VerificationViewModel::class.java]
    }

    private fun initClassifier() {
        classifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)
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

            binding.ivCamera.visibility = View.GONE
            binding.ivUpload.setImageBitmap(result)

            val bitmap = ((binding.ivUpload).drawable as BitmapDrawable).bitmap

            val predict = classifier.recognizeImage(bitmap)

            if (predict.get(0).title == "No_accident"){
                binding.tvStatusAccident.text = "Tidak Tergolong Kecelakaan"
                binding.tvNext.apply {
                    setBackground(R.color.grey)
                }
                binding.tvNext.setOnClickListener {
                    Toast.makeText(this, "Maaf, foto tidak tergolong kecelakaan\nsilakan foto kembali", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.tvStatusAccident.text = "Tergolong Kecelakaan"
                binding.tvNext.apply {
                    setBackground(R.color.colorPrimary)
                }
                //klik tombol next
                binding.tvNext.setOnClickListener {
                    val nameField = binding.etName.text
                    val phoneField = binding.etPhone.text
                    when {
                        nameField.isEmpty() -> {
                            binding.etName.error = "Masukkan Nama terlebih dahulu"
                        }
                        phoneField.isEmpty() -> {
                            binding.etPhone.error = "Masukkan No.Ponsel telebih dahulu"
                        } else -> {
                        verificationViewModel.setVerification(ModelPreference(VERIFICATION))
                        showLoading(false)
                        val intentOrderAmbulance = Intent(this@VerificationPage, MapsActivity::class.java)
                        startActivity(intentOrderAmbulance)
                        finish()
                    }                        }

                }
            }
        }
    }

    private fun View.setBackground(resDrawableId: Int) {
        background = ContextCompat.getDrawable(context, resDrawableId)
    }

    private fun startCameraX() {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private const val VERIFICATION = true
    }
}