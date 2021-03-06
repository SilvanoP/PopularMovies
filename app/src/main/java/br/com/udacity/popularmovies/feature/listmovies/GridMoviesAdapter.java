package br.com.udacity.popularmovies.feature.listmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Movie;
import br.com.udacity.popularmovies.util.Constants;

public class GridMoviesAdapter extends RecyclerView.Adapter<GridMoviesAdapter.GridMoviesViewHolder> {

    private final GridAdapterClickListener mOnClickListener;

    private Context mContext;
    private List<Movie> mMovies;

    GridMoviesAdapter(Context context, List<Movie> movies, GridAdapterClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
        this.mMovies = movies;
    }

    @Override
    @NonNull
    public GridMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movies_grid_item, parent, false);
        return  new GridMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridMoviesViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    void addItems(List<Movie> newMovies) {
        int startPos = mMovies.size();
        mMovies.addAll(newMovies);
        this.notifyItemRangeInserted(startPos, newMovies.size());
    }

    void swapItems(List<Movie> newMovies) {
        mMovies.clear();
        mMovies = newMovies;
        this.notifyDataSetChanged();
    }

    class GridMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView posterImageView;
        private TextView movieNameTextView;
        private RatingBar movieRatingBar;

        GridMoviesViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            posterImageView = v.findViewById(R.id.movie_poster_image);
            movieNameTextView = v.findViewById(R.id.movie_name_text);
            movieRatingBar = v.findViewById(R.id.movie_score_rating);
        }

        void bind(Movie movie) {
            String url = Constants.IMAGE_BASE_URL + Constants.POSTER_SMALL_SIZE + movie.getPosterUrl();

            Picasso.get().load(url)
                    .placeholder(R.drawable.error_loading_image)
                    .error(R.drawable.error_loading_image)
                    .into(posterImageView);

            movieNameTextView.setText(movie.getName());
            float scoreMax5 = (movie.getVoteAverage() * 5)/10;
            movieRatingBar.setRating(scoreMax5);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onItemClick(mMovies.get(clickedPosition));
        }
    }

    interface GridAdapterClickListener {
        void onItemClick(Movie movie);
    }
}
