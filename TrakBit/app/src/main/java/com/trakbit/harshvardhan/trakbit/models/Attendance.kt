package com.trakbit.harshvardhan.trakbit.models

import io.realm.RealmObject

open class Attendance : RealmObject() {
    var userId: String? = null
    var clocking: String? = null
    var deviceIMEI: String? = null
    var latitude: String? = null
    var longitude: String? = null
}