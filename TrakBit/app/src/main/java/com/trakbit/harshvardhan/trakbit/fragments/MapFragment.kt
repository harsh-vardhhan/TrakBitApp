package com.trakbit.harshvardhan.trakbit.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trakbit.harshvardhan.trakbit.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapView
import com.trakbit.harshvardhan.trakbit.models.Attendance
import io.realm.Realm
import io.realm.kotlin.where
import kotlin.properties.Delegates


class MapFragment : Fragment() {


    lateinit var mMapView: MapView
    private var googleMap: GoogleMap? = null
    private var realm: Realm by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.map_fragment, container, false)

        mMapView = rootView.findViewById(R.id.mapView)
        mMapView.onCreate(savedInstanceState)

        mMapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMapView.getMapAsync { mMap ->
            googleMap = mMap

            val attendances = realm.where<Attendance>().findAll()
            attendances.forEachIndexed { i, it ->
                val location = LatLng((it.latitude)!!.toDouble(),(it.longitude)!!.toDouble())
                googleMap!!.addMarker(MarkerOptions().position(location).title("Marker Title").snippet("Marker Description"))
            }

            val firstLocation = LatLng(
                    (attendances.first()?.latitude)!!.toDouble(),
                    (attendances.first()?.longitude)!!.toDouble())
            val cameraPosition = CameraPosition.Builder().target(firstLocation).zoom(12f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}
