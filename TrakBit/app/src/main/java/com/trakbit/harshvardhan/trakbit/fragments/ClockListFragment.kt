package com.trakbit.harshvardhan.trakbit.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.adapters.ClockListAdapter
import com.trakbit.harshvardhan.trakbit.models.Attendance
import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.clocking_fragment.view.*
import kotlin.properties.Delegates

class ClockListFragment : Fragment() {
    private var realm: Realm by Delegates.notNull()
    private var fragmentVisible = true
    private var deleteButton: ImageView? = null
    private var position: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deleteButton = activity.deleteButton
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.clocking_fragment, container, false)
        Realm.setDefaultConfiguration(SyncConfiguration.automatic());
        realm = Realm.getDefaultInstance()
        val listView = view!!.listView
        val adapter = ClockListAdapter(activity, clockData(realm))
        listView?.adapter = adapter
        adapter?.notifyDataSetChanged()

        listView.setOnItemLongClickListener { _, _, pos, _ ->
            position = pos
            toggleHeader()
            true
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteButton?.setOnClickListener {
            deleteClock(position!!)
        }
    }

    private fun toggleHeader() {
        val appBar = activity.findViewById<AppBarLayout>(R.id.appBar)
        val toolBar = activity.findViewById<Toolbar>(R.id.toolBar)
        val visible = View.VISIBLE
        val invisible = View.INVISIBLE
        if (appBar.visibility == invisible) {
            appBar.visibility = visible
            toolBar?.visibility = invisible
        } else if (appBar.visibility == visible) {
            appBar.visibility = invisible
            toolBar?.visibility = visible
        }
    }

    private fun updateList() {
        val listView = view!!.listView
        val adapter = ClockListAdapter(activity, clockData(realm))
        listView?.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun deleteClock(position: Int) {
        val clockList = realm.where<Attendance>().findAll()
        realm.executeTransaction {
            clockList.deleteFromRealm(position)
        }
        toggleHeader()
        updateList()
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentVisible = isVisibleToUser
        if (fragmentVisible) {
            updateList()
        }
    }

    private fun clockData(realm: Realm): ArrayList<Attendance> {
        val attendances = realm.where<Attendance>().findAllAsync()
        val attendanceList = arrayListOf<Attendance>()
        attendances.forEachIndexed { i, it ->
            val attendance = Attendance()
            attendance.clocking = it.clocking
            attendance.deviceIMEI = it.deviceIMEI
            attendance.latitude = it.latitude
            attendance.longitude = it.longitude
            attendanceList.add(i, attendance)
        }
        return attendanceList
    }
}