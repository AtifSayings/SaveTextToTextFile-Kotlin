package com.blogspot.atifsoftwares.savetotextfiles

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.blogspot.atifsoftwares.savetotextfiles.R.id.inputEt
import com.blogspot.atifsoftwares.savetotextfiles.R.id.saveBtn
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    var mText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //handle button click
        saveBtn.setOnClickListener {
            //input text form EditText
            mText = inputEt.text.toString()
            //validate
            if (mText=="") {//user has not entered anything
                Toast.makeText(this@MainActivity, "Please enter something...", Toast.LENGTH_SHORT).show()
            } else {//user has entered string data
                //if user OS is >= marshmallow we need runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        //show popup for runtime permission
                        requestPermissions(permissions, WRITE_EXTERNAL_STORAGE_CODE)
                    } else {
                        //permission already granted, save data
                        saveToTxtFile(mText)
                    }
                } else {
                    //system OS is < marshmallow, no need to runtime permission, save data
                    saveToTxtFile(mText)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_CODE -> {
                //if request is cancelled, the result arrays are empty
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted, save data
                    saveToTxtFile(mText)
                } else {
                    //permission was denied, show toast
                    Toast.makeText(this, "Storage permission is required to store data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveToTxtFile(mText: String) {
        //get current time for file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis())
        try {
            //path tp storage
            val path = Environment.getExternalStorageDirectory()
            //create folder named "My Files"
            val dir = File(path.toString() + "/My Files/")
            dir.mkdirs()
            //file name
            val fileName = "MyFile_$timeStamp.txt" //e.g MyFile_20180623_152322.txt

            val file = File(dir, fileName)

            //FileWriter class is used to store characters in file
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(mText)
            bw.close()

            //show file name and path where file is saved
            Toast.makeText(this, "$fileName is saved to\n$dir", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            //if anything goes wrong
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }

    companion object {
        private val WRITE_EXTERNAL_STORAGE_CODE = 1
    }

}