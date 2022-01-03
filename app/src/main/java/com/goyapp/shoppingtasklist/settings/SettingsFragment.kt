package com.goyapp.shoppingtasklist.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.goyapp.shoppingtasklist.R

class SettingsFragment : PreferenceFragmentCompat() { // Создаем специальный фрагмент, который сможет сохранять выбранные настройки
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_screen,rootKey)

    }
}