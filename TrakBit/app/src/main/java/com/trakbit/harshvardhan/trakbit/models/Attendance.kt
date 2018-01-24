package com.trakbit.harshvardhan.trakbit.models

import io.realm.RealmObject
import org.joda.time.DateTime

/**
 * Created by harshvardhan on 24/01/2018.
 */
open class Attendance : RealmObject() {
    var clocking: String? = null
    var deviceIMEI: String? = null
}