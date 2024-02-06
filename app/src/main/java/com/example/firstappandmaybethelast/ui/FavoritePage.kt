package com.example.firstappandmaybethelast.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstappandmaybethelast.databinding.FragmentFavoritePageBinding

class FavoritePage : Fragment() {
    private val binding by lazy {
        FragmentFavoritePageBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}