package br.com.udacity.popularmovies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.model.Movie;
import br.com.udacity.popularmovies.util.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GridMoviesAdapter extends RecyclerView.Adapter<GridMoviesAdapter.GridMoviesViewHolder> {

    private final GridItemClickListener mOnClickListener;

    private Context mContext;
    private List<Movie> movies;

    public GridMoviesAdapter(Context context, List<Movie> movies, GridItemClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
        this.movies = movies;
    }

    public interface GridItemClickListener {
        void onGridItemClick(int clickedItemIndex);
    }

    @Override
    public GridMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.movies_grid_item, parent, false);
        return  new GridMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridMoviesViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class GridMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster_image)
        ImageView posterImageView;
        @BindView(R.id.movie_name_text)
        TextView movieNameTextView;
        @BindView(R.id.movie_score_rating)
        RatingBar movieRatingBar;

        public GridMoviesViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            ButterKnife.bind(this, v);
        }

        void bind(Movie movie) {
            String url = Constants.POSTER_BASE_URL + Constants.POSTER_SMALL_SIZE + movie.getPosterUrl();

            Picasso.with(mContext).load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.drawable.error_loading_image)
                    .into(posterImageView);

            movieNameTextView.setText(movie.getName());
            movieRatingBar.setRating(movie.getVoteAverage());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onGridItemClick(clickedPosition);
        }
    }
}
