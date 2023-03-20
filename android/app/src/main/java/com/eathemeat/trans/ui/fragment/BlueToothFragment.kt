package com.eathemeat.trans.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eathemeat.trans.R

class BlueToothFragment : Fragment() {

    companion object {
        fun newInstance() = BlueToothFragment()
    }

    private lateinit var viewModel: BlueToothViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_blue_tooth, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BlueToothViewModel::class.java)
        // TODO: Use the ViewModel
    }

}