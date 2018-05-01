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
import org.joda.time.format.DateTimeFormat


class ClockListAdapter(private var activity: Activity, private var items: ArrayList<Attendance>) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var txtDate: TextView? = null
        var txtClock: TextView? = null

        init {
            this.txtDate = row?.findViewById(R.id.txtDate)
            this.txtClock = row?.findViewById(R.id.txtClock)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.clock_list_rows, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val attendance = items[position]
        val dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ")
        val dateTimeFormat = dateTimeFormatter.parseDateTime(attendance.clocking)
        val dateFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
        val timeFormatter = DateTimeFormat.forPattern("HH:mm:ss")
        viewHolder.txtDate?.text = dateFormatter.print(dateTimeFormat)
        viewHolder.txtClock?.text = timeFormatter.print(dateTimeFormat)
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