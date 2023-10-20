package com.diyarbekdev.mapsappforexam

import android.app.Activity
import android.os.Build
import java.io.Serializable

//fun <T : Serializable> Activity.getSerializable(name: String, clazz: Class<T>): T {
//    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        intent.getSerializableExtra(name, clazz)!!
//    }else {
//        intent.getSerializableExtra(name) as T
//    }
// }

fun <T : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}