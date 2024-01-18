package com.elmaref.ui.quran.paged.tfseer.options.share

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.elmaref.databinding.ActivityShareAyahBinding
import com.elmaref.ui.quran.paged.functions.saveImage
import com.google.android.material.slider.Slider
import com.quranscreen.utils.ViewUtils


class ShareAyahActivity : AppCompatActivity() {

    lateinit var binding: ActivityShareAyahBinding

    companion object {
        const val STORAGE_PERMISSION_CODE = 100
        const val TAG = "PERMISSION_TAG"

        const val EXTRA_ARABIC = "extra_arabic"
        const val EXTRA_TRANSLATION = "extra_translation"
        const val EXTRA_SOURCE = "extra_source"
        const val EXTRA_TITLE = "extra_title"

        // fun start is used to start the activity with the required data to share the ayah image to other apps like whatsapp, facebook, etc.
        fun start(
            context: Context,
            arabic: String,
            translation: String,
            source: String,
            title: String? = null
        ) {
            context.startActivity(Intent(context, ShareAyahActivity::class.java).apply {
                putExtra(EXTRA_ARABIC, arabic)
                putExtra(EXTRA_TRANSLATION, translation)
                putExtra(EXTRA_SOURCE, source)
                putExtra(EXTRA_TITLE, title)
            })
        }

        // fun ShareImageActivity.getArabic is used to get the arabic text from the intent extras that was passed to the activity when it was started using ShareImageActivity.start
        fun ShareAyahActivity.getArabic(): String {
            return intent.getStringExtra(EXTRA_ARABIC).toString()
        }

        // fun ShareImageActivity.getTranslation is used to get the translation text from the intent extras that was passed to the activity when it was started using ShareImageActivity.start
        fun ShareAyahActivity.getTranslation(): String {
            return intent.getStringExtra(EXTRA_TRANSLATION).toString()
        }

        // fun ShareImageActivity.getSource is used to get the source text from the intent extras that was passed to the activity when it was started using ShareImageActivity.start
        fun ShareAyahActivity.getSource(): String {
            return intent.getStringExtra(EXTRA_SOURCE).toString()
        }

        // fun ShareImageActivity.getTitleValue is used to get the title text from the intent extras that was passed to the activity when it was started using ShareImageActivity.start
        fun ShareAyahActivity.getTitleValue(): String {
            return intent.getStringExtra(EXTRA_TITLE).toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this binding is used to inflate the layout of the activity and to access the views in the layout


        binding = ActivityShareAyahBinding.inflate(LayoutInflater.from(this))
        binding.lifecycleOwner =
            this // this is used to make the binding lifecycle aware so that it can be used to access the views in the layout
        setContentView(binding.root) // this is used to set the content view of the activity to the inflated layout

        binding.arabic.text = getArabic()
        binding.translation.text = getTranslation()
        binding.source.text = getSource()
        binding.arabicSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.arabic.isVisible = b
        }
        binding.translationSwitch.setOnCheckedChangeListener { compoundButton, b ->
            binding.translation.isVisible = b
        }
        binding.arabicSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.arabic.textSize = value
        })
        binding.translationSlider.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->
            binding.translation.textSize = value
        })


        binding.shareButton.setOnClickListener {
            if (checkPermission()) {
                shareImage()
            } else {
                requestPermissionLauncher()
            }
        }

    }
    // fun shareImage is used to share the ayah image to other apps like whatsapp, facebook, etc.
    fun shareImage(){
        saveImage(
            ViewUtils.getBitmapFromView(
                binding.image
            )
        ) { uri ->
            sharePicture(uri)
        }
    }

    private fun sharePicture(uri: Uri?) {
        val intent = Intent(Intent.ACTION_SEND)

        // putting uri of image to be shared

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        // adding text to share

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "تَطْبِيقُ اَلْمَعَارِفِ")

        // Add subject Here

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

        // setting type to image

        // setting type to image
        intent.type = "image/png"

        // calling startactivity() to share

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"))


    }

    fun requestPermissionLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent()
                // this intent is used to open the settings of the app to allow the app to read and write to external storage
                // Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION is used to open the settings of the app to allow the app to read and write to external storage
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                intent.data = android.net.Uri.parse("package:$packageName")
                storageActivityResultLauncher.launch(intent)

            } catch (e: Exception) {
                val intent = Intent()
                // this intent is used to open the settings of the app to allow the app to read and write to external storage
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        } else {
            // android 10 and below
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    val storageActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { isGranted ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    shareImage()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            } else {

            }
        }

    fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val read = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            result == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeAccepted && readAccepted) {
                    shareImage()

                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}