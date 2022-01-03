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

    class ShopItemHolder(view: View) :RecyclerView.ViewHolder(view) { // Класс хранит ссылку на разметку, на каждый Item
        private val binding = ListNameItemBinding.bind(view) // Наш view с разметкой превращается в binding
        fun setData(shopListName: ShopListName, listener: Listener) = with(binding){   // Благодаря этой функции, заполняем элементы в разметке
                listTitle.text = shopListName.name
                listTime.text = shopListName.time
                 val counterText = "${shopListName.countItemBought} / ${shopListName.countItemAll}"
                 tvCounter.text = counterText
                 pBar.max = shopListName.countItemAll // Установка максимального значения в ProgressBar
                 pBar.progress = shopListName.countItemBought // Установка от каких данных будет идти заполнение ProgressBar
                  val colorState = ColorStateList.valueOf(getColorStateProgressBar(shopListName,binding.root.context)) // Специальный класс ColorState, через который можем задавать цвета
                 pBar.progressTintList = colorState
                 cardCounterItem.backgroundTintList = colorState // Меняется цвет фона в cardview
                imDelete.setOnClickListener {
                 listener.deleteItem(shopListName.id!!)
                }
               itemView.setOnClickListener { // Создание слушателя на весь итем, внутри которого наш метод с интерфейса
                  listener.onClickItem(shopListName)
               }

              imEdit.setOnClickListener {
                  listener.editItem(shopListName)
              }
        }

        private fun getColorStateProgressBar(item:ShopListName, context : Context) : Int { // Проверка на состояние цвета на текущий момент
            return  if(item.countItemBought == item.countItemAll){ // Если все полностью заполнено, и отмеченные совпадают с количеством item , то цвет зеленый,иначе красный
                ContextCompat.getColor(context,R.color.picker_green)
            } else {
                ContextCompat.getColor(context,R.color.picker_red)
            }

        }
        companion object{
            fun create(parent: ViewGroup) : ShopItemHolder{ // Выдает инициализированный класс NoteItemHolder который в себе хранит ссылку на загруженную в память разметку
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
        return ShopItemHolder.create(parent)  // Передается наша готовая разметка в функции create
    }

    override fun onBindViewHolder(holder: ShopItemHolder, position: Int) {
        return holder.setData(getItem(position),listener)  // Подсчет элементов, создан каждый элемент с помощью функции setData и так как это ListAdapter мы берем вместо массива, напрямую с помощью getItem, передача listener для всех элементов в массиве
    }

    interface Listener{ // Создаем интерфейс, чтобы передать его с адаптера в ViewModel
        fun deleteItem(id:Int) // Передаем в параметр id, так как по нему будем удалять с базы данных
        fun editItem(shopListName: ShopListName) // Передаем в параметр весь класс, так как будем полностью обновлять заметку
        fun onClickItem(shopListName: ShopListName) // Метод который отвечает за нажатие на наш весь элемент
    }
}