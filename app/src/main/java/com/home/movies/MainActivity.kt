package com.home.movies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.home.movies.data.MovieDatabase
import com.home.movies.data.Record
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity : AppCompatActivity() {


    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)


        CoroutineScope(Dispatchers.IO).launch {
            val data = URL("https://api.themoviedb.org/3/movie/popular?api_key=27e6fc81fba3ed70aa3644aeb07d3749&language=zh-TW&page=1")
                    .readText()
            Log.d(TAG, "onCreate: $data")
            val movieResult = Gson().fromJson(data,MovieResult::class.java)
            movieResult.results.forEach {
                Log.d(TAG, "onCreate:${it.title}")

            }
            val adapter = MovieAdapter(movieResult.results)
            runOnUiThread {
                recycler.adapter = adapter
            }

        }



        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
           startActivity(Intent(this,RecordActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_exit ->{
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}