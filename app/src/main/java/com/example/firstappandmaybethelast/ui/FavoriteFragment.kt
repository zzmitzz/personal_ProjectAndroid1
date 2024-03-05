package com.example.firstappandmaybethelast.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.firstappandmaybethelast.databinding.FragmentFavoritePageBinding
import com.example.firstappandmaybethelast.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private val binding by lazy {
        FragmentFavoritePageBinding.inflate(layoutInflater)
    }
    private val viewmodel by viewModels<FavoriteViewModel>()
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