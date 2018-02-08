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
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.trakbit.harshvardhan.trakbit.models.Attendance
import io.realm.Realm
import io.realm.kotlin.createObject
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates


class ClockFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private var realm: Realm by Delegates.notNull()


    override fun onCreate(savedInstanceState: Bundle?) {
        realm = Realm.getDefaultInstance()
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater!!.inflate(R.layout.clock_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clockButton.setOnClickListener {
            clockDevice(realm)
        }
    }

    private fun clockDevice(realm: Realm) {
        val time = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss").print(DateTime.now())
        val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            realm.executeTransaction {
                val attendance = realm.createObject<Attendance>()
                attendance.clocking = time.toString()
                attendance.deviceIMEI = tManager.getDeviceId()
            }
        }
    }

}
