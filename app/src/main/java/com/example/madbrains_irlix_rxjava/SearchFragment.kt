package com.example.madbrains_irlix_rxjava

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class SearchFragment : Fragment() {

    private lateinit var textViewResult: TextView
    private lateinit var textViewNoResult: TextView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewResult = view.findViewById(R.id.textViewSearchResults)
        textViewNoResult = view.findViewById(R.id.textViewNoResults)
        searchView = view.findViewById(R.id.searchViewForWords)
//        Observable.fromCallable {
//            Thread.sleep(500)
//            Toast.makeText(activity, "Прошло 500 миллисек", Toast.LENGTH_SHORT).show()
//        }
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                textViewResult.text = "пиу"
//            }

    }

    fun parseJson(): String {
        var rawAsString: String?
        val rawInputStream = resources.openRawResource(R.raw.same_little_json)
        val reader = BufferedReader(InputStreamReader(rawInputStream))
        try {
            val results = StringBuilder()
            while (true) {
                val line = reader.readLine() ?: break
                results.append(line)
                results.append("\n")
            }
            rawAsString = results.toString()
        } finally {
            reader.close()
        }
        val jsonArray = JSONArray(rawAsString)
        val latinList = mutableListOf<Latina>()
        var finalText = ""
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val userId = jsonObject.getString("userId")
            val id = jsonObject.getString("id")
            val body = jsonObject.getString("body")
            val title = jsonObject.getString("title")
            val latina = Latina(userId, id, title, body)
            latinList.add(latina)
            finalText = finalText + "${latina.title}: \n${latina.body}" + "\n\n\n"
        }
        return finalText
    }


}