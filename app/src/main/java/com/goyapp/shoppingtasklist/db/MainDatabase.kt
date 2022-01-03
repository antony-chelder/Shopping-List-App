package com.goyapp.shoppingtasklist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName


@Database(entities = [LibraryItem::class,NoteItem::class,ShopListItem::class,ShopListName::class],version = 1) 
 abstract class MainDatabase : RoomDatabase() { 
    abstract fun getDao() :Dao 

  companion object{  
   @Volatile 
   private var INSTANCE : MainDatabase? = null
   fun getDatabase(context:Context) : MainDatabase{ 
      return INSTANCE ?: synchronized(this){
       val instance = Room.databaseBuilder(  
        context.applicationContext,
        MainDatabase::class.java,
        "shoppinglist.db"
       ).build()
      instance  
      }

   }

  }
}
