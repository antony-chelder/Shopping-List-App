package com.goyapp.shoppingtasklist.utils

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

object TimeManager {
     const val DEF_TIME_FORMAT = "hh:mm.ss - yyyy/MM/dd"
      fun getCurrentTime() : String{ // Функция для получения текущего времени создания заметки, указываем определенный формат
        val formatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault()) // Создали формат временеи, в каком виде оно будет
        return formatter.format(Calendar.getInstance().time) // Возвращаем готовый формат с реальным временем
    }


    fun getTimeFormat(time: String,defPreference : SharedPreferences) : String{ // Функция для того чтобы достать с сохранненого стринга формата времени данные
       val defFormatter = SimpleDateFormat(DEF_TIME_FORMAT, Locale.getDefault()) // Получаем формат времени
        val defDate = defFormatter.parse(time) // Разбиваем по отдельности данные с стринга

        val newFormat = defPreference.getString("time_style_key", DEF_TIME_FORMAT) // Берем по ключу с sharedPreference какой формат был выбран, если нет, то значение по умолчанию

        val newFormatter = SimpleDateFormat(newFormat, Locale.getDefault()) // Установка выбранного формата времени

        return  if(defDate != null) newFormatter.format(defDate) else time // Возвращение обновленного формата времени
    }
}