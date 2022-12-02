package com.example.ingredientsapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import java.io.IOException
import java.lang.Exception

class ScanningActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var mGraphicOverlay: GraphicOverlay? = null
//    private var imageProcessor = TextRecognitionProcessor(this, TextRecognizerOptions.Builder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanning)

        val value = intent?.extras?.getString("savedPhotoUri").toString()
        imageUri = Uri.parse(value)
        Toast.makeText(baseContext, value, Toast.LENGTH_SHORT).show()

        val imgView: ImageView = findViewById<View>(R.id.resizeImageView) as ImageView
        imgView.setImageURI(imageUri)

        mGraphicOverlay = findViewById<View>(R.id.graphic_overlay) as GraphicOverlay

        val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri) ?: return

        runTextRecognition(imageBitmap)

    }

    private fun runTextRecognition(mSelectedImage : Bitmap) {
        val image: InputImage = InputImage.fromBitmap(mSelectedImage, 0)
        val recognizer: TextRecognizer = TextRecognition.getClient()
        recognizer.process(image)
            .addOnSuccessListener(
                object : OnSuccessListener<Text?> {
                    override fun onSuccess(texts: Text?) {
                        processTextRecognitionResult(texts)
                    }
                })
            .addOnFailureListener(
                object : OnFailureListener {
                    override fun onFailure(e: Exception) {
                        // Task failed with an exception
                        e.printStackTrace()
                    }
                })
    }

    private fun processTextRecognitionResult(texts: Text?) {
        val blocks = texts!!.textBlocks
        var arraylist = ArrayList<String>()
        var string: String = ""
        if (blocks.size == 0) {
            showToast("No text found")
            return
        }
        mGraphicOverlay!!.clear()
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    val textGraphic: GraphicOverlay.Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    mGraphicOverlay!!.add(textGraphic)
                    arraylist.add(elements[k].text)

                    string += elements[k].text + " "
                }
            }
        }
        showToast(string)
        val intent = Intent(applicationContext, IngredientActivity::class.java)
        intent.putExtra("TextString", string)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show()
    }

//    private fun tryReloadAndDetectInImage() {
//        Log.d(TAG, "Try reload and detect image")
//        try {
//            if (imageUri == null) {
//                return
//            }
//
//            if (SIZE_SCREEN == selectedSize && imageMaxWidth == 0) {
//                // UI layout has not finished yet, will reload once it's ready.
//                return
//            }
//
//            val imageBitmap = BitmapUtils.getBitmapFromContentUri(contentResolver, imageUri) ?: return
//
//
//            // Clear the overlay first
//            //graphicOverlay!!.clear()
//
//            val resizedBitmap: Bitmap
//            resizedBitmap = if (selectedSize == SIZE_ORIGINAL) {
//                imageBitmap
//            } else {
//                // Get the dimensions of the image view
//                val targetedSize: Pair<Int, Int> = targetedWidthHeight
//
//                // Determine how much to scale down the image
//                val scaleFactor = Math.max(
//                    imageBitmap.width.toFloat() / targetedSize.first.toFloat(),
//                    imageBitmap.height.toFloat() / targetedSize.second.toFloat()
//                )
//                Bitmap.createScaledBitmap(
//                    imageBitmap,
//                    (imageBitmap.width / scaleFactor).toInt(),
//                    (imageBitmap.height / scaleFactor).toInt(),
//                    true
//                )
//            }
//            //preview!!.setImageBitmap(resizedBitmap)
//            if (imageProcessor != null) {
//                graphicOverlay!!.setImageSourceInfo(
//                    resizedBitmap.width, resizedBitmap.height, /* isFlipped= */false
//                )
//                imageProcessor!!.processBitmap(resizedBitmap, graphicOverlay)
//            } else {
//                Log.e(
//                    TAG,
//                    "Null imageProcessor, please check adb logs for imageProcessor creation error"
//                )
//            }
//        } catch (e: IOException) {
//            Log.e(
//                TAG,
//                "Error retrieving saved image"
//            )
//            imageUri = null
//        }
//    }


    companion object {
        private const val TAG = "ScanningActivity"
    }
}