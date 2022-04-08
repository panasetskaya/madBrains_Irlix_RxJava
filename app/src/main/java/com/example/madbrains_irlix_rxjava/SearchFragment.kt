package com.example.madbrains_irlix_rxjava

import android.os.Bundle
import android.text.SpannableString
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import com.jakewharton.rxbinding4.widget.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private lateinit var textViewResult: TextView
    private lateinit var textViewNoResult: TextView
    private lateinit var searchView: SearchView
    private lateinit var textForSearch: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewResult = view.findViewById(R.id.textViewSearchResults)
        readJsonInRxJava()
        textViewNoResult = view.findViewById(R.id.textViewNoResults)
        searchView = view.findViewById(R.id.searchViewForWords)
        searchInRxJava(searchView)
    }

    fun searchInRxJava(searchView: SearchView) {
        searchView.queryTextChanges()
            .subscribeOn(AndroidSchedulers.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                val searched = it.toString()
                highlightInString(textForSearch, searched)
            }, {
                Log.i("MyRes", it.toString())
            })
    }

    fun highlightInString(s: String, searched: String) {
        val sLow = s.lowercase()
        val searchedLow = searched.lowercase().trim()
        if (sLow.contains(searchedLow)) {
            textViewResult.visibility = View.VISIBLE
            textViewNoResult.visibility = View.GONE
            val spannable = SpannableString(s)
            val foregroundColor = ForegroundColorSpan(resources.getColor(R.color.teal_200))
            val firstIndex = sLow.indexOf(searchedLow)
            spannable.setSpan(CharacterStyle.wrap(foregroundColor), firstIndex, firstIndex+searchedLow.length, 0 )
            textViewResult.text = spannable
        } else {
            textViewResult.visibility = View.GONE
            textViewNoResult.visibility = View.VISIBLE
        }
    }

    fun readJsonInRxJava() {
        Observable.fromCallable {
            parseJson()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                textForSearch = it
                "Текст для поиска:\n$it".also { textViewResult.text = it }
            }, {
                Log.i("MyRes", it.toString())
            })
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