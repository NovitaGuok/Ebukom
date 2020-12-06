package com.ebukom.arch.ui.classdetail.school.schoolschedule.schoolschedulenew

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.data.DataDummy
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_school_schedule_new.*
import java.io.File


class SchoolScheduleNewActivity : AppCompatActivity() {
    private var isSetPelajaran = false
    private var isSetEskul = false
    private var isSetCalendar = false
    private var file: File? = null
    private var path: String? = null
    var classId: String? = null
    val db = FirebaseFirestore.getInstance()
    var storage = Firebase.storage
    var storageRef = FirebaseStorage.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_schedule_new)

        initToolbar()

        classId = intent?.extras?.getString("classId")

        /**
         * File chooser
         */
        btnSchoolScheduleNewSubject.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 10)
        }
        btnSchoolScheduleNewEskul.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 11)
        }
        btnSchoolScheduleNewCalendar.setOnClickListener {
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"
            startActivityForResult(fileIntent, 12)
        }

        /**
         * Insert data to Firestore
         */
        btnSchoolScheduleNewDone.setOnClickListener {
            loading.visibility = View.VISIBLE

//            db.collection("classes").document(classId!!).update("schedules", )

            Handler().postDelayed({
                loading.visibility = View.GONE
                finish()
            }, 1000)
        }

        checkSection()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10, 11, 12 -> {
                    path = data?.data?.path ?: ""
                    newSchedule(path!!, requestCode)
                }
                else -> {
                    btnSchoolScheduleNewDone.isEnabled = false
                    btnSchoolScheduleNewDone.setBackgroundColor(
                        Color.parseColor("#BDBDBD")
                    )
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun newSchedule(path: String, section: Int = 0) {
        var btn: Button? = findViewById(R.id.btnSchoolScheduleNewSubject)
        var tv: TextView? = findViewById(R.id.tvSchoolScheduleNewSubjectPath)
        var iv: ImageView? = findViewById(R.id.ivSchoolScheduleNewSubjectDelete)
        var alert: String? = ""
//        var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(path))

        if (section == 10) {
            btn = findViewById(R.id.btnSchoolScheduleNewSubject)
            tv = findViewById(R.id.tvSchoolScheduleNewSubjectPath)
            iv = findViewById(R.id.ivSchoolScheduleNewSubjectDelete)
            alert = "jadwal"
            DataDummy.scheduleData.add(ClassDetailScheduleDao(path))
            isSetPelajaran = true
        } else if (section == 11) {
            btn = findViewById(R.id.btnSchoolScheduleNewEskul)
            tv = findViewById(R.id.tvSchoolScheduleNewEskulPath)
            iv = findViewById(R.id.ivSchoolScheduleNewEskulDelete)
            alert = "jadwal"
            DataDummy.scheduleData.add(ClassDetailScheduleDao("", path))
            isSetEskul = true
        } else if (section == 12) {
            btn = findViewById(R.id.btnSchoolScheduleNewCalendar)
            tv = findViewById(R.id.tvSchoolScheduleNewCalendarPath)
            iv = findViewById(R.id.ivSchoolScheduleNewCalendarDelete)
            alert = "kalender"
            DataDummy.scheduleData.add(ClassDetailScheduleDao("", "", path))
            isSetCalendar = true
        }

        tv?.text = path
        tv?.visibility = View.VISIBLE
        iv?.visibility = View.VISIBLE
        btn?.text = "UBAH FILE"
        btn?.background =
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.btn_red_rectangle
            )

        btnSchoolScheduleNewDone.isEnabled = true
        btnSchoolScheduleNewDone.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorSuperDarkBlue
            )
        )

        /**
         * Delete schedule
         */
        iv?.setOnClickListener {
            val builder = AlertDialog.Builder(this@SchoolScheduleNewActivity)

            builder.setMessage("Apakah Anda yakin ingin menghapus $alert ini?")

            builder.setNegativeButton("BATALKAN") { dialog, which ->
                Toast.makeText(applicationContext, "Next?", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("HAPUS") { dialog, which ->
//                DataDummy.scheduleData.remove(item)
//                mList.remove(item)
//                mAdapter.addAll(mList)

                alert = alert?.toUpperCase()
                btn?.setText("PILIH FILE $alert")
                btn?.background =
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.btn_blue_rectangle_8dp
                    )
                tv?.visibility = View.INVISIBLE
                iv?.visibility = View.GONE

                if (section == 10) isSetPelajaran = false
                else if (section == 11) isSetEskul = false
                else if (section == 12) isSetCalendar = false

                checkSection()
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorGray
                )
            )

            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.colorRed
                )
            )
        }

        checkSection()
    }

    fun checkSection() {
        // Button disabled
        if (!isSetCalendar
            && !isSetEskul
            && !isSetPelajaran
        ) {
            btnSchoolScheduleNewDone.isEnabled = false
            btnSchoolScheduleNewDone.setBackgroundColor(
                Color.parseColor("#BDBDBD")
            )
        }

    }

    fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun uploadImage() {
//        if (filePath != null) {
//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading...")
//            progressDialog.show()
//            val ref: StorageReference =
//                storageReference.child("images/" + UUID.randomUUID().toString())
//            ref.putFile(filePath)
//                .addOnSuccessListener {
//                    progressDialog.dismiss()
//                    Toast.makeText(this@MainActivity, "Uploaded", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e ->
//                    progressDialog.dismiss()
//                    Toast.makeText(this@MainActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
//                        .show()
//                }
//                .addOnProgressListener { taskSnapshot ->
//                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
//                        .totalByteCount
//                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
//                }
//        }



    }
}
