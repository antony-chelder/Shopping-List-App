package com.goyapp.shoppingtasklist.db

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ListNameItemBinding
import com.goyapp.shoppingtasklist.entities.ShopListName

class ShopListNameAdapter(private val listener: Listener) : ListAdapter<ShopListName,ShopListNameAdapter.ShopItemHolder>(itemComparator()) {

    class ShopItemHolder(view: View) :RecyclerView.ViewHolder(view) { 
        private val binding = ListNameItemBinding.bind(view) 
        fun setData(shopListName: ShopListName, listener: Listener) = with(binding){   
                listTitle.text = shopListName.name
                listTime.text = shopListName.time
                 val counterText = "${shopListName.countItemBought} / ${shopListName.countItemAll}"
                 tvCounter.text = counterText
                 pBar.max = shopListName.countItemAll 
                 pBar.progress = shopListName.countItemBought 
                  val colorState = ColorStateList.valueOf(getColorStateProgressBar(shopListName,binding.root.context)) 
                 pBar.progressTintList = colorState
                 cardCounterItem.backgroundTintList = colorState 
                imDelete.setOnClickListener {
                 listener.deleteItem(shopListName.id!!)
                }
               itemView.setOnClickListener { 
                  listener.onClickItem(shopListName)
               }

              imEdit.setOnClickListener {
                  listener.editItem(shopListName)
              }
        }

        private fun getColorStateProgressBar(item:ShopListName, context : Context) : Int { 
            return  if(item.countItemBought == item.countItemAll){  
                ContextCompat.getColor(context,R.color.picker_green)
            } else {
                ContextCompat.getColor(context,R.color.picker_red)
            }

        }
        companion object{
            fun create(parent: ViewGroup) : ShopItemHolder{ 
                return ShopItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_name_item,parent,false))
            }
        }

    }




    class itemComparator : DiffUtil.ItemCallback<ShopListName>(){
        override fun areItemsTheSame(oldItem: ShopListName, newItem: ShopListName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopListName, newItem: ShopListName): Boolean {
            return oldItem == newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemHolder {
        return ShopItemHolder.create(parent)  
    }

    override fun onBindViewHolder(holder: ShopItemHolder, position: Int) {
        return holder.setData(getItem(position),listener)  
    }

    interface Listener{ 
        fun deleteItem(id:Int) 
        fun editItem(shopListName: ShopListName) 
        fun onClickItem(shopListName: ShopListName) 
    }
}
