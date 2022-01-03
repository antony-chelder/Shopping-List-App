package com.goyapp.shoppingtasklist.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.goyapp.shoppingtasklist.databinding.EditItemDialogBinding
import com.goyapp.shoppingtasklist.entities.ShopListItem

object UpdateListDialog {
    fun showdialog(context: Context,item : ShopListItem,listener:Listener){ 
        var dialog : AlertDialog? = null  
        val builder = AlertDialog.Builder(context) 
        val binding = EditItemDialogBinding.inflate(LayoutInflater.from(context)) 

        builder.setView(binding.root) 

         binding.apply {
             edName.setText(item.name) 
             edDesc.setText(item.item_info)
             if(item.item_type == 1) edDesc.visibility = View.GONE 
          bUpdate.setOnClickListener {
              if(edName.text.toString().isNotEmpty()) {
                  listener.onClick(item.copy(name = edName.text.toString(), item_info = edDesc.text.toString())) 

              }
              dialog?.dismiss()
          }
         }
        dialog = builder.create() 
        dialog.window?.setBackgroundDrawable(null) 
        dialog.show() 

    }
    interface Listener{ 
        fun onClick(item:ShopListItem)

    }
}
