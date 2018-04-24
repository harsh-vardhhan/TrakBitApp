package com.trakbit.harshvardhan.trakbit.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.trakbit.harshvardhan.trakbit.R
import com.trakbit.harshvardhan.trakbit.adapters.ViewPageAdapter
import com.trakbit.harshvardhan.trakbit.fragments.ClockFragment
import com.trakbit.harshvardhan.trakbit.fragments.ClockListFragment
import com.trakbit.harshvardhan.trakbit.fragments.MapFragment
import com.trakbit.harshvardhan.trakbit.ui.Constants
import io.realm.*
import kotlinx.android.synthetic.main.activity_main.*




open class MainActivity : AppCompatActivity(),
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val UPDATE_INTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    lateinit var mLocation: Location
    lateinit var locationManager: LocationManager

    private val realm: Realm? = null

    var credentials = SyncCredentials.nickname("nickname", false)


    val fineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    val coarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION
    val permissionGranted = PackageManager.PERMISSION_GRANTED
    val permissions = arrayOf(fineLocation, coarseLocation)

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(this, fineLocation) != permissionGranted &&
                ActivityCompat.checkSelfPermission(this, coarseLocation) != permissionGranted) {
            ActivityCompat.requestPermissions(this, permissions, 200)
        }
        startLocationUpdates()

        var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient
                .lastLocation
                .addOnSuccessListener(this, {
                    if (it != null) {
                        mLocation = it;
                    }
                })
    }

    override fun onLocationChanged(location: Location?) {
        val bundle = Bundle()
        bundle.putDouble("latitude",location!!.latitude)
        bundle.putDouble("longitude",location!!.longitude)
        val clockFragment = ClockFragment()
        clockFragment.putArguments(bundle)
    }

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        toolbar = toolBar
        toolbar.visibility = View.INVISIBLE
        viewPager = viewpager
        setupViewPager(viewPager)
        tabLayout = tabs
        tabLayout.setupWithViewPager(viewPager)
        setupTabIcons()

        mGoogleApiClient = GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()

        val constants = Constants()
        SyncUser.logInAsync(credentials, constants.AUTH_URL, object: SyncUser.Callback<SyncUser> {
            override fun onSuccess(user:SyncUser) {
                println("success")
            }
            override fun onError(error: ObjectServerError) {
                println("fail")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        realm?.close()
    }

    private fun setupTabIcons() {
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_access_time_white)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_list_white)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_map_white_24px)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ClockFragment(), "clock")
        adapter.addFragment(ClockListFragment(), "clockings")
        adapter.addFragment(MapFragment(), "map")
        viewPager.adapter = adapter
    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog
                .setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
                .setPositiveButton("Location Settings", { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                })
                .setNegativeButton("Cancel", { _, _ -> })
        dialog.show()
    }

    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest
                .create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)

        if (ActivityCompat.checkSelfPermission(this, fineLocation) != permissionGranted &&
                ActivityCompat.checkSelfPermission(this, coarseLocation) != permissionGranted) {
            return
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest, this);
    }

}
