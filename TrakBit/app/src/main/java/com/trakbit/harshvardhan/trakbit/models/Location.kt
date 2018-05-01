package com.trakbit.harshvardhan.trakbit.models

import io.realm.RealmObject

open class Location : RealmObject() {
    var userId: String? = null
    var latitude: String? = null
    var longitude: String? = null
}