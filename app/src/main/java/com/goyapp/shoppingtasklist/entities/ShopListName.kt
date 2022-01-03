package com.goyapp.shoppingtasklist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "shopping_list_names")   
data class ShopListName(
    @PrimaryKey(autoGenerate = true)  
    val id : Int?, 

    @ColumnInfo(name = "name") 
    val name : String,

    @ColumnInfo(name = "time")
    val  time : String,

    @ColumnInfo(name = "total_item_count")
    val  countItemAll : Int,

    @ColumnInfo(name = "bought_item_count")
    val  countItemBought : Int,


    @ColumnInfo(name = "items_id")
    val  items_id : String,   



): Serializable  
