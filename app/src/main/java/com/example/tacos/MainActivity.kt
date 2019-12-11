package com.example.tacos

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast

import android.provider.MediaStore
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE as WRITE_EXTERNAL_STORAGE1


class MainActivity : AppCompatActivity() {

    var view = R.layout.activity_main
    lateinit var listView: ListView
    lateinit var editTextView: EditText
    lateinit var snapimg: ImageView
    lateinit var ItemModelList: ArrayList<ModelClass>
    lateinit var customAdapter: CustomAdapter
    val sharePath = "no"
    lateinit var snap: Button
    private var main: View? = null
    internal var shared: SharedPreferences? = null
    internal lateinit var ll: LinearLayout
    internal var bytearrayoutputstream: ByteArrayOutputStream? = null
    internal var file: File? = null
    internal var fileoutputstream: FileOutputStream? = null
    var imageFile: File? = null
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10
    lateinit var mSelectedImageUri: Uri
    var mCurrentPhotoPath: String? = null
    private var imageHolder: ImageView? = null
    private val requestCode = 20

    private val MY_CAMERA_REQUEST_CODE = 100

//    var packageName: String = null
//     File imageFile = null;


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listview) as ListView
        editTextView = findViewById(R.id.editTextView) as EditText
        snap = findViewById(R.id.button)
        main = findViewById(R.id.main)
        ll = findViewById(R.id.linearLayout)

        val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
        ItemModelList = ArrayList<ModelClass>()
        pref.getString("name", null)

        customAdapter = CustomAdapter(this@MainActivity, ItemModelList)
        listView.emptyView = findViewById(R.id.listview)
        listView.adapter = customAdapter


        // image capture from camera

        snap.setOnClickListener {

            //check for permission

            if (checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf<String>(Manifest.permission.CAMERA),
                    MY_CAMERA_REQUEST_CODE
                )
            } else {
                // get storage and camera permission
                val photoCaptureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(photoCaptureIntent, requestCode)
            }
        }
    }

    // for camera and camera permission

    override
    fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestCode
                )
            }
        }
    }

    // send text click event

    @SuppressLint("NewApi")
    fun addValue(v: View) {
        val name = editTextView.text.toString()
        if (name.isEmpty()) {
            Toast.makeText(
                applicationContext, "Plz enter Values",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val md = ModelClass(name, "", "", "")
            ItemModelList.add(md)
            customAdapter.notifyDataSetChanged()
            editTextView.setText("")
        }
    }

    // get captured image

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras!!["data"] as Bitmap
            val partFilename: String = currentDateFormat().toString()
            bitmap?.let { storeCameraPhotoInSDCard(it, partFilename) }
        }
    }

    // for captured image name in date and time format

    private fun currentDateFormat(): String? {
        val dateFormat = SimpleDateFormat("yyyyMMdd_HH_mm_ss")
        return dateFormat.format(Date())
    }

    // store image in sd card

    private fun storeCameraPhotoInSDCard(bitmap: Bitmap, currentDate: String) {
        val outputFile =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "photo_$currentDate.jpg"
            )

        try {
            val fileOutputStream = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(this, outputFile.toString() + "OK", Toast.LENGTH_LONG).show()

        val md = ModelClass("", outputFile.toString(), "", "")
        ItemModelList.add(md)
        customAdapter.notifyDataSetChanged()
        editTextView.setText("")
    }
}



