package com.ebukom.arch.ui.classdetail.school

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.R
import com.ebukom.arch.dao.ClassDetailSchoolInfoDao
import com.ebukom.arch.ui.chooseclass.ChooseClassViewHolder
import com.ebukom.arch.ui.classdetail.school.schoolannouncement.SchoolAnnouncementFragment
import com.ebukom.arch.ui.classdetail.school.schoolphoto.SchoolPhotoFragment
import com.ebukom.arch.ui.classdetail.school.schoolschedule.SchoolScheduleFragment
import kotlinx.android.synthetic.main.item_class.view.*
import kotlinx.android.synthetic.main.item_school_info.view.*

class SchoolPageAdapter(private val items: List<ClassDetailSchoolInfoDao>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_school_info, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind (items: ClassDetailSchoolInfoDao) {
            itemView.tvItemShoolInfoTitle.text = items?.title
            itemView.tvItemShoolInfoMore.text = "Lihat " + items?.type
            itemView.tvItemShoolInfoMore.setTextColor(items?.colorTheme)
            itemView.ivItemShoolInfo.setImageResource(items?.background)
        }
    }
}

//class SchoolPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//    override fun getItem(position: Int): Fragment {
//        when (position) {
//            0 -> {return SchoolAnnouncementFragment()}
//            1 -> {return SchoolScheduleFragment()}
//            2 -> {return SchoolPhotoFragment()}
//            else -> {return SchoolAnnouncementFragment()}
//        }
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        when (position) {
//            0 -> {return "PENGUMUMAN"}
//            1 -> {return "JADWAL"}
//            2 -> {return "FOTO"}
//        }
//        return super.getPageTitle(position)
//    }
//
//    override fun getCount(): Int {
//        return 3
//    }
//}
