package com.ebukom.arch.ui.classdetail.material

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.ebukom.R
import com.ebukom.arch.ui.classdetail.OnMoreCallback
import com.ebukom.arch.ui.classdetail.school.SchoolPageAdapter
import com.google.android.material.tabs.TabLayout
import java.lang.ClassCastException

class MaterialFragment : Fragment() {
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    lateinit var callback : OnMoreCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_material, container, false)
        tabLayout = view.findViewById(R.id.mainClassMaterialTabLayout) as TabLayout
        viewPager = view.findViewById(R.id.mainClassMaterialViewPager) as ViewPager
        viewPager?.adapter = MaterialPageAdapter(childFragmentManager, context!!)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.currentItem = 0
        return view
    }

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
