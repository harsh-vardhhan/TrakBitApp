package com.trakbit.harshvardhan.trakbit

import android.os.Bundle
import android.support.design.widget.TabItem
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()
    }

    private fun setupTabIcons () {
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_access_time_white)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_list_white)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_account_circle)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        var adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ClockFragment(), "clock")
        adapter.addFragment(ClockingFragment(), "clockings")
        adapter.addFragment(ClockFragment(), "profile")
        viewPager.adapter = adapter
    }

}
