package com.trakbit.harshvardhan.trakbit

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        var adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ClockFragment(), "Clock")
        adapter.addFragment(ClockFragment(), "Clock")
        adapter.addFragment(ClockFragment(), "Clock")
        viewPager.adapter = adapter
    }

}
