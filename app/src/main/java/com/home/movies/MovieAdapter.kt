package com.home.movies

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.movie_row.view.*

class MovieAdapter(var movies : List<Movie>?) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    inner class MovieHolder(view : View) : RecyclerView.ViewHolder(view){
        val poster = view.movie_poster
        val title = view.movie_title
        val popularity = view.movie_pop
        val date = view.movie_date

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_row,parent,false)
        return MovieHolder(view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        movies.run {
            val movie = movies?.get(position)
            holder.title.setText(movie?.title)
            holder.date.setText(movie?.release_date)
            holder.popularity.setText(movie?.vote_average.toString())
            holder.poster.load("https://image.tmdb.org/t/p/w500${movie?.poster_path}"){
                placeholder(R.drawable.porter)
                //transformations(CircleCropTransformation())
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(it.context,OverviewActivity::class.java)
                intent.putExtra("poster",movie?.poster_path)
                intent.putExtra("info",movie?.overview)
                intent.putExtra("id",movie?.id)
                intent.putExtra("vote",movie?.vote_average)
                intent.putExtra("date",movie?.release_date)
                intent.putExtra("title",movie?.title)
                it.context.startActivity(intent)
            }


        }

    }

    override fun getItemCount(): Int {
        return movies?.size ?: 0
    }
}