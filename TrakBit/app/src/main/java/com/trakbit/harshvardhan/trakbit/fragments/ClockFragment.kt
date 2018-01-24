package com.trakbit.harshvardhan.trakbit.fragments

import android.content.Context
import android.support.v4.app.Fragment
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trakbit.harshvardhan.trakbit.R
import kotlinx.android.synthetic.main.clock_fragment.*
import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Created by harshvardhan on 19/01/2018.
 */
class ClockFragment: Fragment() {

    private val PERMISSIONS_REQUEST_READ_PHONE_STATE: Int  = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE)  !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE  ),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.clock_fragment,container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clockButton.setOnClickListener {
            println(getDeviceDetail())
        }
    }

    private fun getDeviceDetail (): String? {
        val time  = DateTime.now()
        val detail = "no access"
        val tManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE)  ==
                PackageManager.PERMISSION_GRANTED) {
            return tManager.getDeviceId() + time
        } else return detail
    }


}
