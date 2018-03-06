package com.trakbit.harshvardhan.trakbit.models

import io.realm.RealmObject

open class Attendance : RealmObject() {
    var clocking: String? = null
    var deviceIMEI: String? = null
}