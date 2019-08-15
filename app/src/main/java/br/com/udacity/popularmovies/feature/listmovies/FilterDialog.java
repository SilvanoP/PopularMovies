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

public class FilterDialog extends DialogFragment implements View.OnClickListener {

    public static final String MOVIE_CATEGORY = "MOVIE_CATEGORY";

    private HashMap<MovieCategory, Integer> mapCategoryFilter;

    private RadioGroup dialogFilterRadioGroup;
    private RadioButton dialogFilterPopularRadio;
    private RadioButton dialogFilterTopRatedRadio;
    private RadioButton dialogFilterUpcomingRadio;
    private RadioButton dialogFilterFavoriteRadio;

    private Button dialogFilterAcceptButton;
    private Button dialogFilterCancelButton;

    static FilterDialog newInstance(MovieCategory category) {
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
        init(view);

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

    private void init(View view) {
        dialogFilterRadioGroup = view.findViewById(R.id.dialogFilterRadioGroup);
        dialogFilterPopularRadio = view.findViewById(R.id.dialogFilterPopularRadio);
        dialogFilterTopRatedRadio = view.findViewById(R.id.dialogFilterTopRatedRadio);
        dialogFilterUpcomingRadio = view.findViewById(R.id.dialogFilterUpcomingRadio);
        dialogFilterFavoriteRadio = view.findViewById(R.id.dialogFilterFavoriteRadio);

        dialogFilterAcceptButton = view.findViewById(R.id.dialogFilterAcceptButton);
        dialogFilterCancelButton = view.findViewById(R.id.dialogFilterCancelButton);
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
            dismiss();
        }
    }

    interface SortDialogListener {
        void sort(MovieCategory category);
    }
}
