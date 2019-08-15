package br.com.udacity.popularmovies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.Review;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ListReviewsAdapter extends RecyclerView.Adapter<ListReviewsAdapter.ListReviewsViewHolder> {

    private Context mContext;
    private List<Review> mReviews;

    public ListReviewsAdapter(Context context, List<Review> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public ListReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ListReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListReviewsViewHolder holder, int position) {
        holder.bind(mReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class ListReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_author_text)
        TextView authorTextView;
        @BindView(R.id.review_content_text)
        TextView contentTextView;

        public ListReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Review review) {
            String author = mContext.getResources().getString(R.string.author) + " " + review.getAuthor();
            authorTextView.setText(author);
            contentTextView.setText(review.getContent());
        }
    }
}
