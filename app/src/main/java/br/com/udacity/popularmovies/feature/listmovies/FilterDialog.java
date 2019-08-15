package br.com.udacity.popularmovies.feature.listmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;

import br.com.udacity.popularmovies.R;
import br.com.udacity.popularmovies.data.entities.MovieCategory;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterDialog extends DialogFragment implements View.OnClickListener {

    public static final String MOVIE_CATEGORY = "MOVIE_CATEGORY";

    private HashMap<MovieCategory, Integer> mapCategoryFilter;

    @BindView(R.id.dialogFilterRadioGroup)
    RadioGroup dialogFilterRadioGroup;
    @BindView(R.id.dialogFilterPopularRadio)
    RadioButton dialogFilterPopularRadio;
    @BindView(R.id.dialogFilterTopRatedRadio)
    RadioButton dialogFilterTopRatedRadio;
    @BindView(R.id.dialogFilterUpcomingRadio)
    RadioButton dialogFilterUpcomingRadio;
    @BindView(R.id.dialogFilterFavoriteRadio)
    RadioButton dialogFilterFavoriteRadio;

    @BindView(R.id.dialogFilterAcceptButton)
    Button dialogFilterAcceptButton;
    @BindView(R.id.dialogFilterCancelButton)
    Button dialogFilterCancelButton;

    public static FilterDialog newInstance(MovieCategory category) {
        FilterDialog dialog = new FilterDialog();
        Bundle args = new Bundle();
        args.putSerializable(MOVIE_CATEGORY, category);
        dialog.setArguments(args);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container);
        ButterKnife.bind(this, view);

        MovieCategory category = MovieCategory.POPULAR;
        if (getArguments() != null) {
            category = (MovieCategory) getArguments().getSerializable(MOVIE_CATEGORY);
        }

        dialogFilterRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    dialogFilterAcceptButton.setEnabled(true);
                }
            }
        });

        mapCategoryFilter = new HashMap<>();
        mapCategoryFilter.put(MovieCategory.POPULAR, dialogFilterPopularRadio.getId());
        mapCategoryFilter.put(MovieCategory.TOP_RATED, dialogFilterTopRatedRadio.getId());
        mapCategoryFilter.put(MovieCategory.UPCOMING, dialogFilterUpcomingRadio.getId());
        mapCategoryFilter.put(MovieCategory.FAVORITE, dialogFilterFavoriteRadio.getId());

        dialogFilterRadioGroup.check(mapCategoryFilter.get(category) == null ? -1 : mapCategoryFilter.get(category));

        dialogFilterAcceptButton.setOnClickListener(this);
        dialogFilterCancelButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == dialogFilterCancelButton.getId()) {
            dismiss();
        } else {
            SortDialogListener listener = (SortDialogListener) requireActivity();
            MovieCategory selectedCategory = MovieCategory.POPULAR;
            for (MovieCategory category : mapCategoryFilter.keySet()) {
                Integer value = mapCategoryFilter.get(category);
                if (dialogFilterRadioGroup.getCheckedRadioButtonId() == value) {
                    selectedCategory = category;
                    break;
                }
            }
            listener.sort(selectedCategory);
        }
    }

    interface SortDialogListener {
        void sort(MovieCategory category);
    }
}
