package com.ebukom.arch.ui.classdetail.school.schoolannouncement.schoolannouncementnew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailAttachmentDao
import com.ebukom.arch.dao.ClassDetailTemplateTextDao
import com.ebukom.arch.ui.classdetail.ClassDetailAttachmentAdapter
import com.ebukom.arch.ui.classdetail.ClassDetailTemplateTextAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_school_anouncement_new.*
import kotlinx.android.synthetic.main.activity_school_anouncement_new.rvSchoolAnnouncementNewTemplate
import kotlinx.android.synthetic.main.bottom_sheet_class_detail_attachment.view.*

class SchoolAnnouncementNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_school_anouncement_new)

        // Attachment
        val attachment: MutableList<ClassDetailAttachmentDao> = ArrayList()

        attachment.add(
            ClassDetailAttachmentDao(
                "https://drive.google.com",
                "drive.google.com", 0
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "-",
                "drive.google.com", 1
            )
        )
        attachment.add(
            ClassDetailAttachmentDao(
                "-",
                "drive.google.com", 2
            )
        )
        rvSchoolAnnouncementAttachment.apply {
            layoutManager = LinearLayoutManager(
                this@SchoolAnnouncementNewActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter =
                ClassDetailAttachmentAdapter(
                    attachment
                )
        }

        // Template Title
        val templateText: MutableList<ClassDetailTemplateTextDao> = ArrayList()
        templateText.add(ClassDetailTemplateTextDao("Field Trip"))
        for (i: Int in 1..10) templateText.add(ClassDetailTemplateTextDao("Perubahan Seragam"))
        rvSchoolAnnouncementNewTemplate.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SchoolAnnouncementNewActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter =
                ClassDetailTemplateTextAdapter(
                    templateText
                )
        }

        // Attach Icon
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_class_detail_attachment, null)
        bottomSheetDialog.setContentView(view)
        ivSchoolAnnouncementNewAttach.setOnClickListener {
            bottomSheetDialog.show()
        }
        view.clBottomClassDetailAttachmentPhoto.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "A", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetClassDetailAttachmentFile.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetClassDetailAttachmentLink.setOnClickListener {
            bottomSheetDialog.dismiss()
            Toast.makeText(this, "F", Toast.LENGTH_LONG).show()
        }
        view.clBottomSheetClassDetailAttachmentUseCamera.setOnClickListener {
        }
    }
}
