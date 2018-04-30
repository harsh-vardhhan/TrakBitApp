package com.trakbit.harshvardhan.trakbit.fragments

import android.content.Context
import android.content.Intent
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
import com.trakbit.harshvardhan.trakbit.activities.LoginActivity
import com.trakbit.harshvardhan.trakbit.models.Attendance
import com.trakbit.harshvardhan.trakbit.models.Location
import io.realm.Realm
import io.realm.SyncConfiguration
import io.realm.SyncUser
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class ClockFragment : Fragment() {

    private val PERMISSIONS_REQUEST_READ_PHONE_STATE = 0
    private var realm: Realm by Delegates.notNull()
    private val readPhoneState = android.Manifest.permission.READ_PHONE_STATE
    private val permissionGranted = PackageManager.PERMISSION_GRANTED
    private var chart: BarChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(context, readPhoneState) != permissionGranted) {
            ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(readPhoneState),
                    PERMISSIONS_REQUEST_READ_PHONE_STATE
            )
        }
        super.onCreate(savedInstanceState)
        val user = SyncUser.current()
        Realm.setDefaultConfiguration(SyncConfiguration.automatic(user));
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater!!.inflate(R.layout.clock_fragment, container, false)

        //Chart
        chart = view.findViewById(R.id.chart1)
        var latList: ArrayList<BarEntry> = ArrayList()
        var count = 1f
        var date = 0f
        val attendances = realm.where<Attendance>().findAll()
        attendances.forEach {
            val dateTimeFormatter = DateTimeFormat.forPattern("yyyy-mm-dd'T'HH:mm:ssZZ")
            val dateTimeFormat = dateTimeFormatter.parseDateTime(it.clocking)
            val dateFormatter = DateTimeFormat.forPattern("dd")
            val newDate = dateFormatter.print(dateTimeFormat).toFloat()
            if (date == newDate) {
                count++
            } else {
                date = newDate
            }
            latList.add(BarEntry(date, count, "date"))
        }
        val dataSet = BarDataSet(latList, "")
        val data = BarData(dataSet)
        data.setValueTextSize(10f);
        data.barWidth = 0.1f;
        chart?.data = data
        chart?.invalidate()

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clockButton.setOnClickListener {
            clockDevice()
        }
        logoutButton.setOnClickListener {
            val users = SyncUser.all()
            users.forEach {
                it.value.logOut()
            }
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun clockDevice() {
        val user = SyncUser.current()
        Realm.setDefaultConfiguration(SyncConfiguration.automatic(user));
        realm = Realm.getDefaultInstance()
        if (realm.where<Location>().count() != 0L) {
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
                    realm.where<Location>().findAll().deleteAllFromRealm()
                }
            }
        }
    }

    fun putArguments(bundle: Bundle) {
        val user = SyncUser.current()
        Realm.setDefaultConfiguration(SyncConfiguration.automatic(user));
        realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val location = realm.createObject<Location>()
            location.latitude = bundle.getDouble("latitude").toString()
            location.longitude = bundle.getDouble("longitude").toString()
        }
    }

}
