package com.goyapp.shoppingtasklist.db

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ListNameItemBinding
import com.goyapp.shoppingtasklist.databinding.ShopLibraryListItemBinding
import com.goyapp.shoppingtasklist.databinding.ShopListItemBinding
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName

class ShopListNameItemAdapter(private val listener: Listener) : ListAdapter<ShopListItem,ShopListNameItemAdapter.ShopItemHolder>(itemComparator()) {

    class ShopItemHolder( val view: View) :RecyclerView.ViewHolder(view) { 

        fun setItemData(shopListName: ShopListItem, listener: Listener){   
             val binding = ShopListItemBinding.bind(view) 
            binding.apply {
                tvShopNameItem.text = shopListName.name
                tvShopInfoItem.text = shopListName.item_info
                checkboxItem.isChecked = shopListName.item_bought
                tvShopInfoItem.visibility = checkInfoVisibility(shopListName) 
                setPaintFlagAndColor(binding) 
                checkboxItem.setOnClickListener {

                    listener.onClickItem(shopListName.copy(item_bought = checkboxItem.isChecked),
                        CHECKBOX) 
                }
                eDbutton.setOnClickListener {
                    listener.onClickItem(shopListName, EDIT)
                }


            }


        }
        fun setDataLibrary(shopListName: ShopListItem, listener: Listener){  
            val binding = ShopLibraryListItemBinding.bind(view)  
            binding.apply {
             tvLibName.text = shopListName.name
                imEditLib.setOnClickListener {
                    listener.onClickItem(shopListName, EDIT_LIB) 
                }
                imDeleteLib.setOnClickListener {
                    listener.onClickItem(shopListName, DELETE_LIB)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(shopListName, CLICK_ITEM_ADD_LIB)
                }
            }

        }

        private fun setPaintFlagAndColor(binding: ShopListItemBinding){ 
            binding.apply {
                if(checkboxItem.isChecked){
                    tvShopNameItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvShopInfoItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                    tvShopNameItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.hint_color)) 
                    tvShopInfoItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.hint_color))
                }else{
                    tvShopNameItem.paintFlags = Paint.ANTI_ALIAS_FLAG 
                    tvShopInfoItem.paintFlags = Paint.ANTI_ALIAS_FLAG


                    tvShopNameItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.black))
                    tvShopInfoItem.setTextColor(ContextCompat.getColor(binding.root.context,R.color.black))
                }
            }


        }


        private fun checkInfoVisibility(shopListName: ShopListItem) : Int{ 
         return  if(shopListName.item_info.isEmpty()){ 
             View.GONE
         }else {
             View.VISIBLE
         }
        }
        companion object{
            fun createShopItem(parent: ViewGroup) : ShopItemHolder{ 
                return ShopItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_list_item,parent,false))
            }

            fun createItemLibrary(parent: ViewGroup) : ShopItemHolder{ 
                return ShopItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.shop_library_list_item,parent,false))
            }
        }

    }


    class itemComparator : DiffUtil.ItemCallback<ShopListItem>(){
        override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun getItemViewType(position: Int): Int { 
        return getItem(position).item_type
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemHolder {
       return if(viewType == 0) { 
             ShopItemHolder.createShopItem(parent)  
        }else{
           ShopItemHolder.createItemLibrary(parent)
        }
    }

    override fun onBindViewHolder(holder: ShopItemHolder, position: Int) {
        return if(getItem(position).item_type == 0) { 
            holder.setItemData(getItem(position), listener)
        } else{
            holder.setDataLibrary(getItem(position), listener)
        }  
    }

    interface Listener{ 
        fun onClickItem(shopListItem: ShopListItem,state:Int) 
    }

    companion object{
        const val DELETE_LIB = 3
        const val EDIT = 0
        const val CHECKBOX = 1
        const val EDIT_LIB = 2
        const val CLICK_ITEM_ADD_LIB = 4

    }
}
