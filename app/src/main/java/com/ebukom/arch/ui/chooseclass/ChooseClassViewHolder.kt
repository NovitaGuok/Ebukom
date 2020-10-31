package com.ebukom.arch.ui.chooseclass

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ebukom.arch.dao.ChooseClassDao
import com.ebukom.arch.ui.classdetail.MainClassDetailActivity
import com.ebukom.arch.ui.joinclass.JoinClassActivity
import kotlinx.android.synthetic.main.item_class.view.*

class ChooseClassViewHolder(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
    fun onBind(item: ChooseClassDao) {

        view.tvItemClassGrade.text = item?.classNumber
        view.tvItemClassGrade.setTextColor(item.colorTheme)
        view.tvItemClassName.text = item?.className
        view.tvItemClassTeacher.text = item?.teacher
        view.tvItemClassTeacher.setTextColor(item.colorTheme)
        view.ivItemClass.setImageResource(item?.background)

        if (context is ChooseClassActivity) {
            view.ibItemClass.setOnClickListener {
                (context as ChooseClassActivity).popupMenu(item)
            }
            view.clItemClass.setOnClickListener {
                (context as ChooseClassActivity).startActivity(
                    Intent(
                        (context as ChooseClassActivity),
                        MainClassDetailActivity::class.java
                    )
                )
            }

        } else {
            view.ibItemClass.visibility = View.INVISIBLE

            view.clItemClass.setOnClickListener {
                (context as JoinClassActivity).addClass(item)
            }
        }
    }
}