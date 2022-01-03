package com.goyapp.shoppingtasklist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
     const val DEF_TIME_FORMAT = "hh:mm.ss - yyyy/MM/dd"
      fun getCurrentTime() : String{ 
        val formatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault()) 
        return formatter.format(Calendar.getInstance().time) 
    }


    fun getTimeFormat(time: String,defPreference : SharedPreferences) : String{ 
       val defFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault()) 
        val defDate = defFormatter.parse(time) 

        val newFormat = defPreference.getString("time_style_key", DEF_TIME_FORMAT) 

        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault()) 

        return  if(defDate != null) newFormatter.format(defDate) else time 
    }
}
