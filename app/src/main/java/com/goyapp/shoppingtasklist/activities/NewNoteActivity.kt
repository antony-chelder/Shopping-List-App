package com.goyapp.shoppingtasklist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.goyapp.shoppingtasklist.R
import com.goyapp.shoppingtasklist.databinding.ActivityNewNoteBinding
import com.goyapp.shoppingtasklist.entities.NoteItem
import com.goyapp.shoppingtasklist.fragments.NoteFragment
import com.goyapp.shoppingtasklist.utils.HtmlManager
import com.goyapp.shoppingtasklist.utils.MyTouchListener
import com.goyapp.shoppingtasklist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewNoteBinding
    private var note: NoteItem? = null
    private lateinit var defpref : SharedPreferences
    private var pref : SharedPreferences? = null 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        init()
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        setTextSize()
        actionBarSettings()
        getNote()
        onClickColorPicker()
        actionMenuCallback()
    }

    private fun onClickColorPicker() = with(binding){
        imRed.setOnClickListener {

            setColorForSelectedText(R.color.picker_red)
        }
        imBlack.setOnClickListener {  setColorForSelectedText(R.color.picker_black)}
        imBlue.setOnClickListener {  setColorForSelectedText(R.color.picker_blue) }
        imOrange.setOnClickListener {  setColorForSelectedText(R.color.picker_orange)}
        imPurple.setOnClickListener {  setColorForSelectedText(R.color.picker_purple)}
        imGreen.setOnClickListener {  setColorForSelectedText(R.color.picker_green) }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){ 
        binding.colorPickerLayout.setOnTouchListener(MyTouchListener())
        pref = PreferenceManager.getDefaultSharedPreferences(this) 
        defpref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_nemu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_note){
           setResult()


        } else if(item.itemId == android.R.id.home){ 
          finish()
        }else if(item.itemId == R.id.bold_style){ 
          setBoldSelectedText()

        }else if(item.itemId == R.id.color_picker){
           if(binding.colorPickerLayout.isShown){ 
               closeColorPicker()
           }else{
               openColorPicker()
           }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getSelectedTheme() : Int{ 
        return when {
            defpref.getString("theme_key","Green") == "Green" -> {
                R.style.Theme_NewNoteThemeGreen
            }
            defpref.getString("theme_key","Yellow") == "Yellow" -> {
                R.style.Theme_NewNoteThemeYellow
            }
            else -> {
                R.style.Theme_NewNoteThemeRed
            }
        }
    }

    private fun setBoldSelectedText() = with(binding){
        val startPos = edDesc.selectionStart 
        val endPos = edDesc.selectionEnd  


        val styles = edDesc.text.getSpans(startPos,endPos,StyleSpan::class.java) 
        var boldStyle : StyleSpan? = null 

        if(styles.isNotEmpty()){ 
            edDesc.text.removeSpan(styles[0]) 
        }else{
            boldStyle = StyleSpan(Typeface.BOLD)

        }
        edDesc.text.setSpan(boldStyle,startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) 
        edDesc.text.trim() 
        edDesc.setSelection(startPos) 


    }

    private fun setColorForSelectedText(colorId : Int) = with(binding){ 
        val startPos = edDesc.selectionStart
        val endPos = edDesc.selectionEnd  


        val styles = edDesc.text.getSpans(startPos,endPos,ForegroundColorSpan::class.java) 

        if(styles.isNotEmpty()){
            edDesc.text.removeSpan(styles[0]) 
        }
        edDesc.text.setSpan(ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity,colorId)),startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) 
        edDesc.text.trim() 
        edDesc.setSelection(startPos) 


    }

    private fun getNote(){
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY) 
        if(sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }


    }

    private fun fillNote() = with(binding){
            edTitle.setText(note?.title) 
            edDesc.setText(HtmlManager.getFromHtml(note?.desc_content!!).trim()) 
    }

    private fun setResult(){
        var editstate = "new" 
        val tempNote: NoteItem? = if(note == null) {
            createNewNote() 

        }else {
            editstate = "update"
            updateNote() 

        }

        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY,tempNote) 
            putExtra(NoteFragment.UPDATE_NOTE_KEY,editstate) 
        }

        setResult(RESULT_OK,i)          
        finish()
    }

    private fun updateNote() : NoteItem? = with(binding){
       return note?.copy(title = edTitle.text.toString(), desc_content = HtmlManager.toHtml(edDesc.text))
    }

    private fun createNewNote() : NoteItem{
        return NoteItem(null,binding.edTitle.text.toString(),HtmlManager.toHtml(binding.edDesc.text) ,TimeManager.getCurrentTime(),"")

    }





    private fun actionBarSettings(){ 
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker(){ 
        binding.colorPickerLayout.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this,R.anim.open_color_picker)
        binding.colorPickerLayout.startAnimation(openAnim) 
        

    }

    private fun closeColorPicker(){ // Функция для закрытия colorpicker
        val openAnim = AnimationUtils.loadAnimation(this,R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(anim: Animation?) {

            }

            override fun onAnimationEnd(anim: Animation?) {
                binding.colorPickerLayout.visibility = View.GONE 
            }

            override fun onAnimationRepeat(anim: Animation?) {

            }

        })
        binding.colorPickerLayout.startAnimation(openAnim) // Запуск анимации к layout


    }
    private fun actionMenuCallback(){ 
        val actionmenucallback = object : ActionMode.Callback{ 
            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear() 
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear() 
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
        binding.edDesc.customSelectionActionModeCallback = actionmenucallback 
    }


    private fun setTextSize() = with(binding){
        edTitle.setTextSize(pref?.getString("text_title_size","16")) 
        edDesc.setTextSize(pref?.getString("text_content_size","14"))

    }

    private fun EditText.setTextSize( size : String?){ 
        if(size != null) this.textSize = size.toFloat() 
}
