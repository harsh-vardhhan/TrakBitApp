package com.trakbit.harshvardhan.trakbit.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class ClockingFragment: Fragment() {

    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater!!.inflate(R.layout.clocking_fragment,container,false)
        realm = Realm.getDefaultInstance()
        val adapter = ClockListAdapter(activity, clockData(realm))
        val listView = view!!.findViewById<ListView>(R.id.listView)
        listView?.adapter = adapter
        adapter?.notifyDataSetChanged()
        return view
    }

    private fun clockData(realm: Realm): ArrayList<Attendance> {
        val attendances = realm.where<Attendance>().findAll()
        val attendanceList: ArrayList<Attendance> = arrayListOf<Attendance>()
        attendances.forEachIndexed { i,it ->
            val attendance = Attendance()
            attendance.clocking = it.clocking
            attendance.deviceIMEI = it.deviceIMEI
            attendanceList.add(i,attendance)
        }
        return attendanceList
    }
}