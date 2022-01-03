package com.goyapp.shoppingtasklist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import kotlinx.coroutines.flow.Flow

@Dao   
interface Dao {
    @Query("SELECT * FROM notes_table")     
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM shopping_list_names")
    fun getAllShopListName() : Flow<List<ShopListName>>


    @Query("SELECT * FROM list_item_table WHERE list_id LIKE :listId") 
    fun getAllShopListItem(listId:Int) : Flow<List<ShopListItem>>


    @Query("SELECT * FROM library WHERE name LIKE :name") 
     suspend fun getAllLibrayItems(name:String) : List<LibraryItem> 


    @Query("DELETE FROM list_item_table WHERE list_id LIKE :listId") 
    suspend fun deleteShopItemsById(listId:Int)

    @Query("DELETE FROM notes_table WHERE id IS :id") 
    suspend fun DeleteNote(id: Int) 

    @Query("DELETE FROM shopping_list_names WHERE id IS :id") 
    suspend fun DeleteListName(id: Int) 


    @Query("DELETE FROM library WHERE id IS:id" ) 
    suspend fun  DeleteLibItem(id:Int)

    @Insert
    suspend fun insertNote(note: NoteItem)   

    @Insert
    suspend fun insertShopList(shopListName: ShopListName) 

    @Update
    suspend fun updateNote(note: NoteItem) 

    @Insert
    suspend fun InsertItem(shoplistnameItem:ShopListItem)

    @Insert
    suspend fun InsertLibraryItem(libraryItem:LibraryItem) 

    @Update
    suspend fun updateListName(shopListName: ShopListName) 

    @Update
    suspend fun updateLibraryItem(libraryItem: LibraryItem) 

    @Update
    suspend fun updateListItem(shopListItem: ShopListItem) 


}
