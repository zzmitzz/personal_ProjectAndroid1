package com.example.firstappandmaybethelast.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstappandmaybethelast.databinding.FragmentSearchBinding
import com.example.firstappandmaybethelast.service.MusicPlayerActivity
import com.example.firstappandmaybethelast.viewmodel.SearchViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private val binding by lazy {
        FragmentSearchBinding.inflate(layoutInflater)
    }
    private val viewmodel by viewModels<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rcvSearch.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = viewmodel.musicAdapter
        }
        viewmodel.musicAdapter.onItemClick = {
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("mediaPosition", it)
            startActivity(intent)
        }
        binding.imageButton2.setOnClickListener { Toast.makeText(context,"is developing",Toast.LENGTH_SHORT).show() }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewmodel.filterArray(newText)
                return true
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }



    companion object{
        const val TAG: String = "Search Fragment"
    }
}