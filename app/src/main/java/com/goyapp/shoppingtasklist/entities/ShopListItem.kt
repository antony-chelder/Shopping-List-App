package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "list_item_table")
data class ShopListItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,

    @ColumnInfo(name = "name")
    val name :String,

    @ColumnInfo(name = "item_info")
    val item_info :String = "",  


    @ColumnInfo(name = "item_bought")
    val item_bought :Boolean = false, 

    @ColumnInfo(name = "list_id") 
    val list_id :Int,



    @ColumnInfo(name = "item_type")
    val item_type :Int = 0  


)
