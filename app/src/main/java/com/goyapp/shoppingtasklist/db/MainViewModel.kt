package com.goyapp.shoppingtasklist.db

import androidx.lifecycle.*
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(database: MainDatabase) : ViewModel() { 
    val dao = database.getDao() 
    val libraryItems = MutableLiveData<List<LibraryItem>>() 
    val allNotes : LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()   
    val allShopListName : LiveData<List<ShopListName>> = dao.getAllShopListName().asLiveData()  

    fun getAllItemsFromList(listid: Int) : LiveData<List<ShopListItem>>{ 
      return dao.getAllShopListItem(listid).asLiveData()
    }

    fun getAllLibraryItems(name: String) = viewModelScope.launch{ 
         libraryItems.postValue(dao.getAllLibrayItems(name)) 
    }



    fun InsertNote(noteItem: NoteItem) = viewModelScope.launch { 
        dao.insertNote(noteItem)
    }

    fun DeleteNote(id: Int) = viewModelScope.launch { 
        dao.DeleteNote(id)
    }

    fun DeleteLibraryItem(id: Int) = viewModelScope.launch { 
        dao.DeleteLibItem(id)
    }
    fun DeleteShopList(id: Int,deleteList : Boolean) = viewModelScope.launch { 
        if(deleteList)dao.DeleteListName(id)
        dao.deleteShopItemsById(id)
    }

    fun UpdateNote(noteItem: NoteItem) = viewModelScope.launch {
        dao.updateNote(noteItem)
    }

    fun UpdateLibraryItem(libraryItem: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(libraryItem)
    }
    fun UpdateListName(shopListName: ShopListName) = viewModelScope.launch {
        dao.updateListName(shopListName)
    }

    fun InsertShopListName(shopListName: ShopListName) = viewModelScope.launch {
        dao.insertShopList(shopListName)
    }


    fun InsertShopListItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.InsertItem(shopListItem)
        if(!isLibraryItemExist(shopListItem.name)) dao.InsertLibraryItem(LibraryItem(null,shopListItem.name)) 
    }

    fun UpdateShopListItem(shopListItem: ShopListItem) = viewModelScope.launch {
        dao.updateListItem(shopListItem)
    }

     private suspend fun isLibraryItemExist(name :String) : Boolean{ 
        return dao.getAllLibrayItems(name).isNotEmpty() 
    }


    class MainViewModelFactory( val database: MainDatabase) : ViewModelProvider.Factory{ 
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){ 
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(database) as T

            }
            throw IllegalArgumentException("Unknown ViewModelClass")
        }

    }
}
