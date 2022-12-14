package com.example.dm.activity

import android.Manifest
import android.R
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dm.MainActivity
import com.example.dm.databinding.ActivityProfileBinding
import com.example.dm.model.UserInfo
import com.example.dm.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException
import java.io.InputStream
import java.util.*


class Profile : AppCompatActivity() {


    private lateinit var dialog:AlertDialog
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // photo select button
        binding.card.setOnClickListener {
            choosePhotoFromGallery()
        }

        // finish button
        binding.finishBtn.setOnClickListener {

            val name = binding.nameEdtxt.text.toString()
            if(name.isNotEmpty() && check==1) {
                var builder = AlertDialog.Builder(this)
                builder.setMessage("Loading..")
                builder.setCancelable(false)
                dialog = builder.create()
                dialog.show()
                uploadData()
            }else {
                Toast.makeText(this,"Enter your name",Toast.LENGTH_SHORT).show()
            }
        }

        // firebase variable initialisation
        storage = FirebaseUtils.firebaseStorage
        database = FirebaseUtils.firebaseDatabase
        auth = FirebaseUtils.firebaseAuth

    }

    // function to upload data of newly created user
    private fun uploadData() {
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(contentUri).addOnCompleteListener{task->
            if(task.isSuccessful) {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    uploadDataInfo(uri.toString())
                }
            }
        }
    }

//    function to create user
    private fun uploadDataInfo(imgUri: String) {

        val user = UserInfo(auth.uid.toString(),
            binding.nameEdtxt.text.toString(),
            auth.currentUser?.phoneNumber.toString(),
            imgUri, "online"
        )

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                dialog.dismiss()
                Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
    }

    // photo select
    private fun choosePhotoFromGallery() {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(
            object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()) {
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        startActivityForResult(galleryIntent, GALLERY)
                    }
                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?) {
                    showDialogForPermissions()
                }
            }).onSameThread().check();

    }

    // function for storage permission
    fun showDialogForPermissions() {
        androidx.appcompat.app.AlertDialog.Builder(this).setMessage("" +
                "Allow permission to use this feature"
        ).setPositiveButton("Go to Settings") {
                _, _ ->
            try {
                val intent  = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()

            }
        }.setNegativeButton("Cancel") {dialog, _ ->
            dialog.dismiss()
        }.show()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK) {
            if(requestCode== GALLERY) {

                if(data!=null) {

                    contentUri = data.data!!
                    try {
                         val selectedImage = MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            contentUri
                        )
                        check = 1
                        binding.profileImg.setImageBitmap(selectedImage)

                        Toast.makeText(this, "Image Set", Toast.LENGTH_SHORT).show()
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    companion object{
        private const val GALLERY = 1
        private lateinit var contentUri: Uri
        private  var check = 0
    }
}