package com.example.imagesorter3
import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesorter3.databinding.ActivityMainBinding
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picsAdapter: ImageAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(permissions()){
            val allpics = loadpics() // this is a list of all the pics in form of model
            picsAdapter = ImageAdapter(this,allpics)
            binding.recycler.adapter = picsAdapter
            binding.recycler.layoutManager = GridLayoutManager(this,3)
        }
        else{
            requestpermission()
        }
    }

    private fun requestpermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),100)
    }

    private fun permissions(): Boolean {
        return (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun loadpics() : List<Picmodel> {
        val res = mutableListOf<Picmodel>()
        val uri = when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }
            else -> {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        }
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        contentResolver.query(uri,projection,null,null,null)
            .use {cursor->
                cursor?.let {
                    while ( cursor.moveToNext() ){
                        val picid = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                        val disp = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,picid)
                        val model = Picmodel(picid,disp,uri)
                        println(uri)
                        println(picid)
                        println(disp)
                        res.add(model)
                    }
                }

            }
        return res
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                loadpics()
            }
        }
        else{
            Toast.makeText(this,"Please give permission",Toast.LENGTH_SHORT).show()
            requestpermission()
        }
    }


//    private val requestPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//            if (isGranted) {
//                // Permission is granted, display images
//                displayImages()
//            } else {
//                // Permission denied, show a message or handle it accordingly
//                Toast.makeText(this, "Please give permission", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
//
//        // Check for permission to access external storage
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            requestPermission()
//        } else {
//            // Permission is already granted
//            displayImages()
//        }
//    }
//
//    private fun requestPermission() {
//        // Request permission to access external storage
//        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//    }
//
//    private fun displayImages() {
//        val images = mutableListOf<String>()
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//        val cursor = contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
//        cursor?.use {
//            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            while (it.moveToNext()) {
//                val imagePath = it.getString(columnIndex)
//                images.add(imagePath)
//            }
//        }
//
//        imageAdapter = com.example.imagesorter3.ImageAdapter(images)
//        binding.recyclerView.adapter = imageAdapter
//    }
}