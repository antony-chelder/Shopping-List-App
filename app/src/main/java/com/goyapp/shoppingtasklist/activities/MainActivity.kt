package com.goyapp.shoppingtasklist.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ActivityMainBinding
import com.goyapp.shoppingtasklist.dialogs.NewListDialog
import com.goyapp.shoppingtasklist.fragments.FragmentManager
import com.goyapp.shoppingtasklist.fragments.NoteFragment
import com.goyapp.shoppingtasklist.fragments.ShopListNamesFragment
import com.goyapp.shoppingtasklist.settings.SettingsActivity

class MainActivity : AppCompatActivity(),NewListDialog.Listener {
    private lateinit var binding : ActivityMainBinding
    private lateinit var pref : SharedPreferences
    private var currentTheme = "" // Переменная с текущей темой, чтобы отслеживать обновление и установку текущей темы
    private var currentMenuItemId = R.id.shop_list // Инстанция переменной через которую будет проверка и корректное обновление menuItem на котором находился пользователь так как мы используем два врагмента на одном активити
    override fun onCreate(savedInstanceState: Bundle?) {
        init()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this) // Запускаем как первый Fragment
        setButtonNavListener()

    }

    private fun getSelectedTheme() : Int{ // Функция для изменение темы в зависимости что выбрал пользователь
        return when {
            pref.getString("theme_key","Green") == "Green" -> {
                R.style.Theme_ShoppingThemeGreen
            }
            pref.getString("theme_key","Yellow") == "Yellow" -> {
                R.style.Theme_ShoppingThemeYellow
            }
            else -> {
                R.style.Theme_ShoppingThemeRed
            }
        }
    }


    private fun init(){
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = pref.getString("theme_key","Green").toString() // Указываем значение по умолчанию, для проверки когда пересоздавать с новой темой
    }

    private fun setButtonNavListener(){ // Создали слушатель нажатий на item в нашем ButtonNavMenu
        binding.bnav.setOnItemSelectedListener {
            when(it.itemId){ // Прогоняем через when на какой из item было нажатие
                R.id.settings->{
                    startActivity(Intent(this,SettingsActivity::class.java))
                }
                R.id.notes->{
                    FragmentManager.setFragment(NoteFragment.newInstance(),this) // Запуск определенного фрагмента для Note
                    currentMenuItemId = R.id.notes
                }
                R.id.shop_list->{
                    FragmentManager.setFragment(ShopListNamesFragment.newInstance(),this) // Запуск определенного фрагмента для ShopListNames
                    currentMenuItemId = R.id.shop_list
                }
                R.id.new_item->{
                    FragmentManager.currentFrag?.onClickNewFrag()

                }
            }

            true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bnav.selectedItemId = currentMenuItemId // Когда возвращаемся к примеру с настроек будет сохранена информация на каком menuItem находился пользователь
        if(pref.getString("theme_key","Green") != currentTheme) recreate()
    }

    override fun onClick(name: String) {
        Log.d("Test", "Name : $name")
    }
}