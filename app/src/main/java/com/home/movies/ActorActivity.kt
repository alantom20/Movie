package com.home.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_actor.*
import kotlinx.android.synthetic.main.actor_row.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ActorActivity : AppCompatActivity() {
    companion object{
        val TAG = ActorActivity::class.java.simpleName
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor)
        setSupportActionBar(toolbar_actor)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val id = intent.getIntExtra("id",0)
        Log.d(TAG, "onCreate:${id} ")
        recycler_actor.setHasFixedSize(true)
        recycler_actor.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val data = URL("https://api.themoviedb.org/3/movie/${id.toString()}/credits?api_key=27e6fc81fba3ed70aa3644aeb07d3749&language=en-US")
                .readText()
            val actorResult = Gson().fromJson(data,ActorResult::class.java)
            actorResult.cast.forEach {
                Log.d(TAG, "onCreate:${it.name} ")
            }
            runOnUiThread{
                recycler_actor.adapter = ActorAdapter(actorResult.cast)
            }

        }

    }
    class ActorAdapter(val casts : List<Cast>) : RecyclerView.Adapter<ActorHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorHolder {
            val  view = LayoutInflater.from(parent.context).inflate(R.layout.actor_row,parent,false)
            return ActorHolder(view)
        }

        override fun onBindViewHolder(holder: ActorHolder, position: Int) {
            val cast = casts.get(position)
            holder.name.setText(cast.name)
            holder.profile.load("https://image.tmdb.org/t/p/w500${cast.profile_path}"){
                placeholder(R.drawable.person)
                error(R.drawable.person)
                //transformations(CircleCropTransformation())
            }
        }

        override fun getItemCount(): Int {
            return  casts.size
        }

    }

    class ActorHolder(view : View) : RecyclerView.ViewHolder(view){
        var profile: ImageView = view.actor_image
        var name: TextView =  view.actor_name
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