package com.goyapp.shoppingtasklist.activities

import android.app.Application
import com.goyapp.shoppingtasklist.db.MainDatabase

class MainApp: Application() { 
    val database by lazy { MainDatabase.getDatabase(this) } 
}
