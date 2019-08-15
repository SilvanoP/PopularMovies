package br.com.udacity.popularmovies.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Video;
import br.com.udacity.popularmovies.util.ItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ListTrailersAdapter extends RecyclerView.Adapter<ListTrailersAdapter.ListTrailerViewHolder> {

    private final String YOUTUBE_BASE_URL = "http://img.youtube.com/vi/";
    private final ItemClickListener mListener;

    private Context mContext;
    private List<Video> mTrailers;

    public ListTrailersAdapter(Context context, List<Video> trailers, ItemClickListener listener) {
        mContext = context;
        mTrailers = trailers;
        mListener = listener;
    }

    @Override
    public ListTrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.trailers_list_item, parent, false);

        return new ListTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListTrailerViewHolder holder, int position) {
        Video trailer = mTrailers.get(position);
        holder.bind(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    class ListTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_thumbnail_image)
        ImageView thumbnailImage;
        @BindView(R.id.trailer_name_text)
        TextView nameTextView;

        public ListTrailerViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, view);
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
