package com.goyapp.shoppingtasklist.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.NewListDialogBinding

object NewListDialog {
    fun showdialog(context: Context,listener:Listener,name: String){
        var dialog : AlertDialog? = null  
        val builder = AlertDialog.Builder(context) 
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context)) 

        builder.setView(binding.root) 



        binding.apply {
            if(name.isNotEmpty()){
                button.setText(R.string.update_text_button)
            }
            ednewListName.setText(name)
            button.setOnClickListener {
                val listname = ednewListName.text.toString()

                if(listname.isEmpty()){ 
                    dialog?.dismiss()

                }else{
                  listener.onClick(listname) 
                    dialog?.dismiss()
                }

            }
        }
        dialog = builder.create() 
        dialog.window?.setBackgroundDrawable(null) 
        dialog.show() 

    }
    interface Listener{ 
        fun onClick(name:String)

    }
}
