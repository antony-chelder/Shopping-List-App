package com.goyapp.shoppingtasklist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.goyapp.shoppingtasklist.activities.MainApp
import com.goyapp.shoppingtasklist.activities.ShopListActivity
import com.goyapp.shoppingtasklist.databinding.FragmentShopListNamesBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.ShopListNameAdapter
import com.goyapp.shoppingtasklist.dialogs.DeleteDialog
import com.goyapp.shoppingtasklist.dialogs.NewListDialog
import com.goyapp.shoppingtasklist.entities.ShopListName
import com.goyapp.shoppingtasklist.utils.TimeManager


class ShopListNamesFragment : BaseFragment(),ShopListNameAdapter.Listener {
    private lateinit var binding : FragmentShopListNamesBinding
    private  lateinit var adapter : ShopListNameAdapter

    private  val mainViewModel: MainViewModel by activityViewModels{ 
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database) 
    }
    override fun onClickNewFrag() {
       NewListDialog.showdialog(activity as AppCompatActivity, object : NewListDialog.Listener{
           override fun onClick(name: String) { 
            val shoplistname = ShopListName( 
                null,name, TimeManager.getCurrentTime(),0,0,""
            )
               mainViewModel.InsertShopListName(shoplistname) /
           }

       }, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun observer(){
        mainViewModel.allShopListName.observe(viewLifecycleOwner,{ 
            adapter.submitList(it)


        })
    }


    private fun initRcView() = with(binding){
      rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ShopListNameAdapter(this@ShopListNamesFragment)
        rcView.adapter = adapter

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopListNamesBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ShopListNamesFragment()

    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showdialog(context as AppCompatActivity, object : DeleteDialog.Listener{
            override fun onClick() {
                mainViewModel.DeleteShopList(id,true)
            }

        })
    }

    override fun editItem(shopListName: ShopListName) { 
        NewListDialog.showdialog(activity as AppCompatActivity, object : NewListDialog.Listener{
            override fun onClick(name: String) { 

                mainViewModel.UpdateListName(shopListName.copy(name = name)) 
            }

        },shopListName.name) 
    }

    override fun onClickItem(shopListName: ShopListName) {

        val i = Intent(activity,ShopListActivity::class.java).apply {
            putExtra(ShopListActivity.SHOP_LIST_NAME_KEY,shopListName) 
        }
        startActivity(i)
    }
}
