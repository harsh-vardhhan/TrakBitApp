package com.trakbit.harshvardhan.trakbit.models

import io.realm.RealmObject
import org.joda.time.DateTime

open class Attendance : RealmObject() {
    var clocking: String? = null
    var deviceIMEI: String? = null
}