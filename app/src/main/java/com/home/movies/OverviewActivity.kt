package com.home.movies

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.room.Room
import coil.load
import com.google.gson.Gson
import com.home.movies.data.MovieDatabase
import com.home.movies.data.Record
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class OverviewActivity : AppCompatActivity() {

    private lateinit var backdrop: String
    private  var  record: Record? = null
    private  var records: List<Record>? =  null
    private lateinit var date: String
    private lateinit var title: String
    private var vote: Double = 0.0
    private var id: Int = 0
    private lateinit var info: String
    private lateinit var poster: String
    private lateinit var database: MovieDatabase
    private var star: Boolean = false
    private lateinit var key: String

    companion object{
        val TAG = OverviewActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(toolbar_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        backdrop = intent.getStringExtra("backdrop")
        poster = intent.getStringExtra("poster")
        info = intent.getStringExtra("info")
        id = intent.getIntExtra("id",0)
        vote = intent.getDoubleExtra("vote",0.0)
        date = intent.getStringExtra("date")
        title = intent.getStringExtra("title")
        Log.d(TAG, "onCreate:$poster/$id/$vote/$date")
        toolbar_info.title = title



        //video
        CoroutineScope(Dispatchers.IO).launch {
            records =  MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.getAll()
            val data = URL("https://api.themoviedb.org/3/movie/$id/videos?api_key=27e6fc81fba3ed70aa3644aeb07d3749&language=en-US")
                    .readText()
            Log.d(TAG, "onCreate:$data ")
            val video = Gson().fromJson(data,VideoResult::class.java)

            video.results.forEach {
                key = it.key
                Log.d(TAG, "onCreate: ${it.key}")
            }

        }
        video.setOnClickListener {
            val uri = Uri.parse("https://www.youtube.com/watch?v=$key")
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }
        actor.setOnClickListener {
            val actor = Intent(this,ActorActivity::class.java)
            actor.putExtra("id",id)
            startActivity(actor)
        }

        movie_poster_overview.load("https://image.tmdb.org/t/p/w500$backdrop")

        if(info.equals("")){
            movie_info.setText("新增中")
        }else{
            movie_info.setText(info)
        }

        //star
        CoroutineScope(Dispatchers.IO).launch{
            record = MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.findByMovieId(id.toString())
            if(record?.star == true){
                star = true
            }

        }




    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_star,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_star ->{
                star = !star
                // Log.d(TAG, "onOptionsItemSelected:${star} ")
                if (star){
                    item.setIcon(R.drawable.star)
                    val record = Record(poster,backdrop,title,date,vote,star,info,id)
                    CoroutineScope(Dispatchers.IO).launch {
                        MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.insert(record)

                    }
                }else{
                    item.setIcon(android.R.drawable.btn_star)
                    CoroutineScope(Dispatchers.IO).launch {
                        record = MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.findByMovieId(id.toString())
                        MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.delete(record)
                    }
                }
            }
            android.R.id.home -> {
                finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        CoroutineScope(Dispatchers.IO).launch{
            record = MovieDatabase.getInstance(this@OverviewActivity)?.recordDao()?.findByMovieId(id.toString())
            Log.d(TAG, "onPrepareOptionsMenu: ${record?.star}")
            runOnUiThread {
                if(record?.star == true){
                    menu?.findItem(R.id.action_star)?.setIcon(R.drawable.star)
                }

            }

        }

        return super.onPrepareOptionsMenu(menu)
    }
}