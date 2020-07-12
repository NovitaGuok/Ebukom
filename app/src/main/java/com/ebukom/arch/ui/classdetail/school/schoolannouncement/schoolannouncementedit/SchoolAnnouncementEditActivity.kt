package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementedit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAnnouncementAttachmentDao
import com.ebukom.arch.dao.ClassDetailAnnouncementTemplateDao
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementAttachmentAdapter
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementTemplateAdapter
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_announcement_edit.*
import kotlinx.android.synthetic.main.activity_school_announcement_edit.rvSchoolAnnouncementAttachment
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.bottom_sheet_announcement_attachment.view.*

class SchoolAnnouncementEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_announcement_edit)

//        val data : MutableList<ClassDetailAnnouncementTemplateDao> = ArrayList()
//
//        data.add(ClassDetailAnnouncementTemplateDao("Field Trip"))
//        for (i: Int in 1..10) data.add(ClassDetailAnnouncementTemplateDao("Perubahan Seragam"))
//
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//
//        val adapter =
//            SchoolAnnouncementTemplateAdapter(
//                data
//            )
//
//        rvSchoolAnnouncementEditTemplate.layoutManager = layoutManager
//        rvSchoolAnnouncementEditTemplate.adapter = adapter

        // Attachment
        val attachment: MutableList<ClassDetailAnnouncementAttachmentDao> = ArrayList()

        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
                "https://drive.google.com",
                "drive.google.com", 0
            )
        )
        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
                "-",
                "drive.google.com", 1
            )
        )
        attachment.add(
            ClassDetailAnnouncementAttachmentDao(
                "-",
                "drive.google.com", 2
            )
        )


        rvSchoolAnnouncementAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementEditActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = SchoolAnnouncementAttachmentAdapter(
                attachment
            )
        }

        // Template Title
        val template: MutableList<ClassDetailAnnouncementTemplateDao> = ArrayList()
        template.add(ClassDetailAnnouncementTemplateDao("Field Trip"))
        for (i: Int in 1..10) template.add(ClassDetailAnnouncementTemplateDao("Perubahan Seragam"))
        rvSchoolAnnouncementEditTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementEditActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = SchoolAnnouncementTemplateAdapter(
                template
            )
        }

        // Attach Icon
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_announcement_attachment, null)
        bottomSheetDialog.setContentView(view)
        ivSchoolAnnouncementEditTitle.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.clBottomSheetAnnouncementAttachmentPhoto.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentFile.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentLink.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetAnnouncementAttachmentUseCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
