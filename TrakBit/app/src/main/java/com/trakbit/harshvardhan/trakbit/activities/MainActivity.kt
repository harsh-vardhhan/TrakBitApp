package com.trakbit.harshvardhan.trakbit.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.adapters.ViewPageAdapter
import com.trakbit.harshvardhan.trakbit.fragments.ClockFragment
import com.trakbit.harshvardhan.trakbit.fragments.ClockingFragment
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        toolbar = toolBar
        toolbar.visibility = View.INVISIBLE
        viewPager = viewpager
        setupViewPager(viewPager)
        tabLayout = tabs
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()
    }

    private fun setupTabIcons () {
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_access_time_white)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_list_white)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_account_circle)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ClockFragment(), "clock")
        adapter.addFragment(ClockingFragment(), "clockings")
        adapter.addFragment(ClockFragment(), "profile")
        viewPager.adapter = adapter
    }

}
