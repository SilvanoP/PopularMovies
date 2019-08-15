package br.com.udacity.popularmovies.feature.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.util.ItemClickListener;


public class ListTrailersAdapter extends RecyclerView.Adapter<ListTrailersAdapter.ListTrailerViewHolder> {

    private static final String YOUTUBE_BASE_URL = "http://img.youtube.com/vi/";
    private final ItemClickListener mListener;

    private Context mContext;
    private List<Video> mTrailers;

    ListTrailersAdapter(Context context, List<Video> trailers, ItemClickListener listener) {
        mContext = context;
        mTrailers = trailers;
        mListener = listener;
    }

    @Override
    @NonNull
    public ListTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);

        return new ListTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTrailerViewHolder holder, int position) {
        Video trailer = mTrailers.get(position);
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class ListTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView thumbnailImage;
        private TextView nameTextView;

        ListTrailerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            thumbnailImage = view.findViewById(R.id.trailer_thumbnail_image);
            nameTextView = view.findViewById(R.id.trailer_name_text);
        }

        void bind(Video video) {
            String key = video.getKey();
            String url = YOUTUBE_BASE_URL + key + mContext.getResources().getString(R.string.youtube_thumb_endpoint);
            Picasso.get().load(url)
                    .placeholder(R.drawable.error_loading_image)
                    .error(R.drawable.error_loading_image)
                    .into(thumbnailImage);

            nameTextView.setText(video.getName());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onItemClick(clickedPosition);
        }
    }
}
