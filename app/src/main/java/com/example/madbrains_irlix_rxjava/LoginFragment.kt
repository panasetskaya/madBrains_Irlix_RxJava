package com.example.madbrains_irlix_rxjava

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view?.findViewById<View>(R.id.buttonLogin)?.setOnClickListener {
            val navController = view?.findNavController()
            val action = LoginFragmentDirections.actionLoginFragmentToSearchFragment()
            navController?.navigate(action)
            //            findNavController().navigate(R.id.searchFragment)
            // Navigation.createNavigateOnClickListener(R.id.searchFragment, null)
        }
    }
}