package com.goyapp.shoppingtasklist.activities

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ShopListActivityBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.ShopListNameItemAdapter
import com.goyapp.shoppingtasklist.dialogs.UpdateListDialog
import com.goyapp.shoppingtasklist.entities.LibraryItem
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import com.goyapp.shoppingtasklist.utils.ShareHelper

class ShopListActivity : AppCompatActivity(),ShopListNameItemAdapter.Listener {
    private  lateinit var binding : ShopListActivityBinding
    private var adapter : ShopListNameItemAdapter? = null
    private lateinit var textwatcher : TextWatcher
    private  var edItem: EditText? = null 
    private lateinit var saveItem :MenuItem 
    private var shoplistName : ShopListName? = null 
    private  val mainViewModel: MainViewModel by viewModels{
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).database) 
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShopListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        rcView()
        listItemObserver()
    }

    private fun rcView() = with(binding){ 
        adapter = ShopListNameItemAdapter(this@ShopListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ShopListActivity)
        rcView.adapter = adapter

    }

    private fun init(){
        shoplistName = intent.getSerializableExtra(SHOP_LIST_NAME_KEY) as ShopListName
       
    }




    private fun expandActionView() : MenuItem.OnActionExpandListener{ 
        return object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(menu: MenuItem?): Boolean {
                saveItem.isVisible = true 
                edItem?.addTextChangedListener(textwatcher) 
                libraryitemObserver() 
                mainViewModel.getAllItemsFromList(shoplistName?.id!!).removeObservers(this@ShopListActivity) 
                mainViewModel.getAllLibraryItems("%%") 
                return true
            }

            override fun onMenuItemActionCollapse(menu: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textwatcher)  
                invalidateOptionsMenu() 
                mainViewModel.libraryItems.removeObservers(this@ShopListActivity)
                edItem?.setText("") 
                listItemObserver()
                return true
            }

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_list_item -> {
                addNewItem(edItem?.text.toString())
            }
            R.id.delete_list -> { 
                shoplistName?.id?.let { mainViewModel.DeleteShopList(it,true) }
                finish()
            }
            R.id.clear_list -> { 
                shoplistName?.id?.let { mainViewModel.DeleteShopList(it,false) }
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(shoplistName?.let { 
                    ShareHelper.shareShopList(adapter?.currentList!!,
                        it.name)
                },"Share by"))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewItem(name:String){ 
        if(name.isEmpty()) return 
        val item = ShopListItem(
            null,name,"",false,shoplistName?.id!!,0
        )
        edItem?.setText("") 
        mainViewModel.InsertShopListItem(item) 


    }


    private fun listItemObserver(){
        mainViewModel.getAllItemsFromList(shoplistName?.id!!).observe(this,{
            adapter?.submitList(it) 
            binding.tvEmpty.visibility = if (it.isEmpty()){ 
                View.VISIBLE
            } else{
                View.GONE
            }

        })

    }

    private fun libraryitemObserver(){ 
       mainViewModel.libraryItems.observe(this,{
           val tempShopList = ArrayList<ShopListItem>() 
         it.forEach { item -> 
           val shopItem = ShopListItem(
               item.id,item.name,"",false,0,1
           )
             tempShopList.add(shopItem)
         }
           adapter?.submitList(tempShopList)
           binding.tvEmpty.visibility = if (it.isEmpty()){ 
               View.VISIBLE
           } else{
               View.GONE
           }
       })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { 
        menuInflater.inflate(R.menu.shop_list_menu,menu)
        saveItem = menu?.findItem(R.id.save_list_item)!! 
        val newItem = menu.findItem(R.id.new_list_item)!! 
        edItem = newItem.actionView.findViewById(R.id.ed_new_shop_item) as EditText 
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false 
        textwatcher = textWatcher() 

        return true

    }

    private fun textWatcher() : TextWatcher{ 
        return object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(letter: CharSequence?, p1: Int, p2: Int, p3: Int) {
                  mainViewModel.getAllLibraryItems("%$letter%") 
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
    }


    companion object{
        const val SHOP_LIST_NAME_KEY = "shop_list_name"
    }

    override fun onClickItem(shopListItem: ShopListItem,state : Int) { 
        when(state){
            ShopListNameItemAdapter.CHECKBOX -> mainViewModel.UpdateShopListItem(shopListItem)
            ShopListNameItemAdapter.EDIT -> editListItem(shopListItem)
            ShopListNameItemAdapter.CLICK_ITEM_ADD_LIB -> addNewItem(shopListItem.name) 
            ShopListNameItemAdapter.EDIT_LIB -> editLibItem(shopListItem)
            ShopListNameItemAdapter.DELETE_LIB ->{
                mainViewModel.DeleteLibraryItem(shopListItem.id!!) 
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") 
                libraryitemObserver()

            }
        }

    }

    private fun editListItem(shopListItem: ShopListItem){ 
        UpdateListDialog.showdialog(this,shopListItem, object : UpdateListDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.UpdateShopListItem(item) 

            }

        })

    }

    private fun editLibItem(shopListItem: ShopListItem){ 
        UpdateListDialog.showdialog(this,shopListItem, object : UpdateListDialog.Listener{
            override fun onClick(item: ShopListItem) {
                mainViewModel.UpdateLibraryItem(LibraryItem(item.id,item.name)) 
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%") 
                libraryitemObserver()
            }

        })

    }

    private fun saveItemCount(){ 

        var checkedItemCounter = 0
        adapter?.currentList?.forEach{ 
            if(it.item_bought) {
                checkedItemCounter ++ 
            }
        }
        val tempshopListItem =  shoplistName?.copy(
            countItemAll = adapter?.itemCount!!, 
             countItemBought = checkedItemCounter
        )
        tempshopListItem?.let { mainViewModel.UpdateListName(it) }

    }


    override fun onBackPressed() {
        saveItemCount()
        super.onBackPressed()

    }


}
