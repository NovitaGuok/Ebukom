package com.ebukom.arch.ui.classdetail.school.schoolschedule

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailScheduleDao
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import kotlinx.android.synthetic.main.fragment_school_schedule.*

class SchoolScheduleFragment : Fragment() {
    var objectList = ArrayList<ClassDetailScheduleDao>()
    lateinit var schoolScheduleAdapter : SchoolScheduleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_school_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        schoolScheduleAdapter = SchoolScheduleAdapter(objectList, callback)
        rvSchoolSchedule.layoutManager = LinearLayoutManager(this.context)
        rvSchoolSchedule.adapter = schoolScheduleAdapter
    }

    private fun addData() {
        for (i in 0..10) {
            objectList.add(
                ClassDetailScheduleDao(
                    "Jadwal Pelajaran",
                    0,
                    "JadwalPelajaran2020.pdf"
                )
            )
        }
    }

    lateinit var callback : OnMoreCallback

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            callback = context as OnMoreCallback
        } catch (e : ClassCastException){
            throw ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }
}
