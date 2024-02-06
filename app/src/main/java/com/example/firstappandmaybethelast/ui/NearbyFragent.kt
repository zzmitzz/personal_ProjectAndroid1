package com.example.firstappandmaybethelast.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firstappandmaybethelast.R

/**
 * A simple [Fragment] subclass.
 * Use the [NearbyFragent.newInstance] factory method to
 * create an instance of this fragment.
 */
class NearbyFragent : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_fragent, container, false)
    }

}