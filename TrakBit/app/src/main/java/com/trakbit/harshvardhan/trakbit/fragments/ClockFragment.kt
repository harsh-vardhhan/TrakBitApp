package com.trakbit.harshvardhan.trakbit.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trakbit.harshvardhan.trakbit.R
import kotlinx.android.synthetic.main.clock_fragment.*

/**
 * Created by harshvardhan on 19/01/2018.
 */
class ClockFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.clock_fragment,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clockButton.setOnClickListener { println("clockButton")}
    }


}