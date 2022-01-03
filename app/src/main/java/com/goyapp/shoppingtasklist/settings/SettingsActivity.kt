package com.goyapp.shoppingtasklist.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.goyapp.shoppingtasklist.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var pref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setTheme(getSelectedTheme())
        setContentView(R.layout.activity_settings)
        if(savedInstanceState == null){ 
            supportFragmentManager.beginTransaction().replace(R.id.placeholder,SettingsFragment()).commit()


        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)

    }

    private fun init(){
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun getSelectedTheme() : Int{ 
        return when {
            pref.getString("theme_key","Green") == "Green" -> {
                R.style.Theme_ShoppingThemeGreen
            }
            pref.getString("theme_key","Yellow") == "Yellow" -> {
                R.style.Theme_ShoppingThemeYellow
            }
            else -> {
                R.style.Theme_ShoppingThemeRed
            }
        }
    }


}
