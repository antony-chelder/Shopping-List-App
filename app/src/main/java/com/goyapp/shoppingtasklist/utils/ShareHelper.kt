package com.goyapp.shoppingtasklist.utils

import android.content.Intent
import com.goyapp.shoppingtasklist.entities.ShopListItem
import com.goyapp.shoppingtasklist.entities.ShopListName
import java.lang.StringBuilder

object ShareHelper {
    fun shareShopList(shopList : List<ShopListItem>,listName : String) : Intent{ 
        val intent = Intent(Intent.ACTION_SEND) 
        intent.type = "text/plane" 
        intent.apply { 
            putExtra(Intent.EXTRA_TEXT, makeShareText(shopList,listName)) 

        }
        return intent

    }

    private fun makeShareText(shopList : List<ShopListItem>,listName : String) : String{ 
        val sBuilder = StringBuilder() 
        sBuilder.append("<<$listName>>") 
        sBuilder.append("\n") 
        var counter = 0 

      shopList.forEach {
         sBuilder.append("${++counter} - ${it.name} (${it.item_info})") 
          sBuilder.append("\n") 


      }
        return sBuilder.toString()
    }
}
