package com.goyapp.shoppingtasklist.fragments

import androidx.appcompat.app.AppCompatActivity
import com.goyapp.shoppingtasklist.R

object FragmentManager { 
    var currentFrag:BaseFragment? =null 

    fun setFragment(newFrag:BaseFragment,activity:AppCompatActivity){ 
       val transaction = activity.supportFragmentManager.beginTransaction() 
        transaction.replace(R.id.placeholder,newFrag) 
        transaction.commit() 
        currentFrag = newFrag 
    }
}
