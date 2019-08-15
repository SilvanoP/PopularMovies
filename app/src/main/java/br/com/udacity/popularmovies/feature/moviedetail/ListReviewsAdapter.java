package br.com.udacity.popularmovies.feature.moviedetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Review;


public class ListReviewsAdapter extends RecyclerView.Adapter<ListReviewsAdapter.ListReviewsViewHolder> {

    private Context mContext;
    private List<Review> mReviews;

    ListReviewsAdapter(Context context, List<Review> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @NonNull
    @Override
    public ListReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ListReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListReviewsViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ListReviewsViewHolder extends RecyclerView.ViewHolder {

        private TextView authorTextView;
        private TextView contentTextView;

        ListReviewsViewHolder(View itemView) {
            super(itemView);

            authorTextView = itemView.findViewById(R.id.review_author_text);
            contentTextView = itemView.findViewById(R.id.review_content_text);
        }

        void bind(Review review) {
            String author = mContext.getResources().getString(R.string.author) + " " + review.getAuthor();
            authorTextView.setText(author);
            contentTextView.setText(review.getContent());
        }
    }
}
