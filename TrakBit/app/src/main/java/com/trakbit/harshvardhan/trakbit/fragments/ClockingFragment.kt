package com.trakbit.harshvardhan.trakbit.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.adapters.ClockListAdapter
import com.trakbit.harshvardhan.trakbit.models.Attendance
import io.realm.Realm
import io.realm.kotlin.where
import kotlin.properties.Delegates

/**
 * Created by harshvardhan on 23/01/2018.
 */
class ClockingFragment : Fragment() {

    private var realm: Realm by Delegates.notNull()
    private var fragmentVisible = true
    private var deleteButton: ImageView? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.clocking_fragment, container, false)
        realm = Realm.getDefaultInstance()
        deleteButton = activity.findViewById<ImageView>(R.id.deleteButton)
        val listView = view!!.findViewById<ListView>(R.id.listView)
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

    private fun toggleHeader() {
        val appBar = activity.findViewById<AppBarLayout>(R.id.appBar)
        val toolbar = activity.findViewById<Toolbar>(R.id.toolBar)
        if (appBar.visibility == View.INVISIBLE) {
            appBar.visibility = View.VISIBLE
            toolbar.visibility = View.INVISIBLE
        } else if (appBar.visibility == View.VISIBLE) {
            appBar.visibility = View.INVISIBLE
            toolbar.visibility = View.VISIBLE
        }
    }

    private fun updateList() {
        val listView = view!!.findViewById<ListView>(R.id.listView)
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

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteButton!!.setOnClickListener {
            deleteClock(position!!)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        fragmentVisible = isVisibleToUser
        if (fragmentVisible) {
            val adapter = ClockListAdapter(activity, clockData(realm))
            val listView = view!!.findViewById<ListView>(R.id.listView)
            listView?.adapter = adapter
            adapter?.updateClockList(clockData(realm))
        }
    }

    private fun clockData(realm: Realm): ArrayList<Attendance> {
        val attendances = realm.where<Attendance>().findAll()
        val attendanceList = arrayListOf<Attendance>()
        attendances.forEachIndexed { i, it ->
            val attendance = Attendance()
            attendance.clocking = it.clocking
            attendance.deviceIMEI = it.deviceIMEI
            attendanceList.add(i, attendance)
        }
        return attendanceList
    }
}