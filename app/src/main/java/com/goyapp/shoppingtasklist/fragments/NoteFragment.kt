package com.goyapp.shoppingtasklist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.goyapp.shoppingtasklist.activities.MainApp
import com.goyapp.shoppingtasklist.activities.NewNoteActivity
import com.goyapp.shoppingtasklist.databinding.FragmentNoteBinding
import com.goyapp.shoppingtasklist.db.MainViewModel
import com.goyapp.shoppingtasklist.db.NoteAdapter
import com.goyapp.shoppingtasklist.entities.NoteItem


class NoteFragment : BaseFragment(),NoteAdapter.Listener {
    private lateinit var binding:FragmentNoteBinding
    private lateinit var pref : SharedPreferences
    lateinit var adapter : NoteAdapter 
    private lateinit var activeLaunch : ActivityResultLauncher<Intent> 
    private  val mainViewModel: MainViewModel by activityViewModels{ 
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    override fun onClickNewFrag() { 
        activeLaunch.launch(Intent(activity,NewNoteActivity::class.java)) 
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { 
        super.onViewCreated(view, savedInstanceState)
        initRcView() 
        observer()
    }

    private fun observer(){
        mainViewModel.allNotes.observe(viewLifecycleOwner,{ 
            adapter.submitList(it)

        })
    }


    private fun initRcView() = with(binding){
        pref = PreferenceManager.getDefaultSharedPreferences(activity)
        rcViewNote.layoutManager = checkLayoutView()   
        adapter = NoteAdapter(this@NoteFragment,pref) 
        rcViewNote.adapter = adapter  

    }

    private fun  checkLayoutView() : RecyclerView.LayoutManager{ 
        return if(pref.getString("note_style_key","Linear") == "Linear"){ 
            LinearLayoutManager(activity)
        }else {
            StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) 
        }
    }

    private fun onEditResult(){
        activeLaunch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ 
            if(it.resultCode == Activity.RESULT_OK){
                val edistate = it.data?.getStringExtra(UPDATE_NOTE_KEY)
                if(edistate== "new") {
                    mainViewModel.InsertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem) 
                }else{
                    mainViewModel.UpdateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }
            }

        }
    }

    companion object {
        const val NEW_NOTE_KEY = "note_key"
        const val UPDATE_NOTE_KEY = "update_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    override fun deleteItem(id: Int) {
        mainViewModel.DeleteNote(id)
    }

    override fun onClickItem(noteItem: NoteItem) {
        val i = Intent(activity,NewNoteActivity::class.java).apply { 
            putExtra(NEW_NOTE_KEY,noteItem) 
        }
        activeLaunch.launch(i)
    }
}
