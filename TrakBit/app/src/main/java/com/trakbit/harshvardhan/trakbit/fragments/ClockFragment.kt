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
import com.trakbit.harshvardhan.trakbit.models.Location
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.SyncConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates


class ClockFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 0
    private var realm: Realm by Delegates.notNull()
    private val readPhoneState = android.Manifest.permission.READ_PHONE_STATE
    private val permissionGranted = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        Realm.setDefaultConfiguration(SyncConfiguration.automatic());
        realm = Realm.getDefaultInstance()

        if (ContextCompat.checkSelfPermission(context, readPhoneState) != permissionGranted) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(readPhoneState),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
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
            clockDevice()
        }
    }

    private fun clockDevice() {
        Realm.setDefaultConfiguration(SyncConfiguration.automatic());
        realm = Realm.getDefaultInstance()
        if(realm.where<Location>().count() != 0L) {
            val location = realm.where<Location>()?.findAllAsync()?.last()
            val time = DateTimeFormat
                    .forPattern("yyyy-mm-dd'T'HH:mm:ssZZ")
                    .print(DateTime.now())
            val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (ContextCompat.checkSelfPermission(context, readPhoneState) == permissionGranted) {
                realm.executeTransaction {
                    val attendance = realm.createObject<Attendance>()
                    attendance.latitude = location?.latitude.toString()
                    attendance.longitude = location?.longitude.toString()
                    attendance.clocking = time.toString()
                    attendance.deviceIMEI = tManager.getDeviceId()
                }
            }
        }
    }

    fun putArguments(bundle: Bundle) {
        Realm.setDefaultConfiguration(SyncConfiguration.automatic());
        realm = Realm.getDefaultInstance()
        realm.executeTransaction{
            val location = realm.createObject<Location>()
            location.latitude = bundle.getDouble("latitude").toString()
            location.longitude = bundle.getDouble("longitude").toString()
        }
    }

}
