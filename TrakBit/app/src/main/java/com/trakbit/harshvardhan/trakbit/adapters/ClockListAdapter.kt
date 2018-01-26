package com.trakbit.harshvardhan.trakbit.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.models.Attendance

/**
 * Created by harshvardhan on 25/01/2018.
 */
class ClockListAdapter(private var activity: Activity, private var items: ArrayList<Attendance>): BaseAdapter() {

    private class ViewHolder(row: View?) {
        var txtClock: TextView? = null
        var txtDevice: TextView? = null

        init {
            this.txtClock = row?.findViewById<TextView>(R.id.txtClock)
            this.txtDevice = row?.findViewById<TextView>(R.id.txtDevice)
        }
    }

    fun updateClockList(attendances: ArrayList<Attendance>) {
        items.clear()
        items.addAll(attendances)
        this.notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.clock_list_rows,null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val attendance = items[position]
        viewHolder.txtClock?.text = attendance.clocking
        viewHolder.txtDevice?.text = attendance.deviceIMEI
        return view as View
    }

    override fun getItem(i: Int): Attendance {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

}