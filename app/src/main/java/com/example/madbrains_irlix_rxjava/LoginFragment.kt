package com.example.madbrains_irlix_rxjava

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.jakewharton.rxbinding.widget.RxTextView
import rx.Observable


class LoginFragment : Fragment() {
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val list = arrayListOf(currentEmail.toString(), currentPassword.toString())
        outState.putStringArrayList("current", list)
    }

    var currentEmail: String? = null
    var currentPassword: String? = null
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState!=null) {
            val list = savedInstanceState.getStringArrayList("current")
            currentEmail = list?.get(0)
            currentPassword = list?.get(1)
        }
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextEmail = view.findViewById(R.id.editTextTextEmailAddress)
        editTextPassword = view.findViewById(R.id.editTextTextPassword)
        button = view.findViewById(R.id.buttonLogin)
        currentPassword?.let { editTextPassword.setText(it) }
        currentEmail?.let{ editTextEmail.setText(it) }
        checkForEnablingButton()
        button.setOnClickListener {
            val navController = it.findNavController()
            val action = LoginFragmentDirections.actionLoginFragmentToSearchFragment()
            navController.navigate(action)
        }
    }

    fun checkForEnablingButton() {
        val editTextEmailObservable = RxTextView.textChanges(editTextEmail).map { it.toString() }
        val editTextPasswordObservable = RxTextView.textChanges(editTextPassword).map { it.toString() }
        Observable
            .zip(editTextEmailObservable, editTextPasswordObservable) { email, password ->
                email.length >= 6 && password.length >=6 }
            .subscribe {
                button.isEnabled = it
                if (button.isEnabled) {
                button.setBackgroundColor(resources.getColor(R.color.teal_200))
            }
        }
    }
}