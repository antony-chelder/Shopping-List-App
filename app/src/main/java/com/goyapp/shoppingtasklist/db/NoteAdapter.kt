package com.goyapp.shoppingtasklist.db

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.NoteListItemBinding
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.utils.HtmlManager
import com.goyapp.shoppingtasklist.utils.TimeManager

class NoteAdapter( private  val listener:Listener,val pref : SharedPreferences) : ListAdapter<NoteItem,NoteAdapter.NoteItemHolder>(itemComparator()) {

    class NoteItemHolder(view: View) :RecyclerView.ViewHolder(view) { 
        private val binding = NoteListItemBinding.bind(view) 
        fun setData(noteItem: NoteItem,listener:Listener,pref: SharedPreferences) = with(binding){   
                tvTitle.text = noteItem.title
                tvDesc.text =  HtmlManager.getFromHtml(noteItem.desc_content).trim() 
                tvTime.text = TimeManager.getTimeFormat(noteItem.note_time,pref)
                imDelete.setOnClickListener {
                    listener.deleteItem(noteItem.id!!) 
                }
               itemView.setOnClickListener {
                   listener.onClickItem(noteItem)
               }
        }
        companion object{
            fun create(parent: ViewGroup) : NoteItemHolder{ 
                return NoteItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item,parent,false))
            }
        }

    }


    class itemComparator : DiffUtil.ItemCallback<NoteItem>(){
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteItemHolder {
        return NoteItemHolder.create(parent)  
    }

    override fun onBindViewHolder(holder: NoteItemHolder, position: Int) {
        return holder.setData(getItem(position),listener,pref)  
    }

    interface Listener{ 
        fun deleteItem(id:Int) 
        fun onClickItem(noteItem: NoteItem) 
    }
}
