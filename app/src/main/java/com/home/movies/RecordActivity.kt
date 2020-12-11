package com.home.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.home.movies.data.MovieDatabase
import com.home.movies.data.Record
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.movie_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordActivity : AppCompatActivity() {
    companion object {
        val TAG = RecordActivity::class.java.simpleName
    }

    private var record: List<Record>? = null
    private lateinit var list: MutableList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        setSupportActionBar(toolbar_record)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        recycler_record.setHasFixedSize(true)
        recycler_record.layoutManager = LinearLayoutManager(this)
        reFlashScreen()



    }

    private fun reFlashScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            record = MovieDatabase.getInstance(this@RecordActivity)?.recordDao()?.getAll()
            list = mutableListOf()

            record?.forEach {
                val movie = Movie(it.poster,it.id,it.backdrop ,it.title, it.vote, it.info, it.date)
                list.add(movie)
                Log.d(TAG, "onCreate:${it.id} ")
            }
            runOnUiThread {
                recycler_record.adapter = MovieAdapter(list)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        reFlashScreen()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }


}