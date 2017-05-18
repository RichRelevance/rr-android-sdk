package com.richrelevance.richrelevance.FindDemo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.richrelevance.find.search.Facet;
import com.richrelevance.find.search.Filter;
import com.richrelevance.find.search.SearchRequestBuilder;
import com.richrelevance.find.search.SearchResultProduct;
import com.richrelevance.richrelevance.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchSortFilterActivity extends AppCompatActivity {

    private static final String KEY_FACET_LIST = "KEY_FACET_LIST";
    public static final String KEY_SELECTED_SORTED_BY = "KEY_SELECTED_SORTED_BY";
    public static final String KEY_SELECTED_SORT_ORDER = "KEY_SELECTED_SORT_ORDER";
    public static final String KEY_SELECTED_FILTER_BY = "KEY_SELECTED_FILTER_BY";

    private SearchResultProduct.Field sortBy;
    private SearchRequestBuilder.SortOrder sortOrder;
    private List<Filter> filterBys = new ArrayList<>();

    private ArrayAdapter<SearchResultProduct.Field> sortedAdapter;
    private ArrayAdapter<String> filterAdapter;

    public static Intent createSearchSortFilterActivityIntent(Activity activity, ArrayList<Facet> facets) {
        Intent intent = new Intent(activity, SearchSortFilterActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_FACET_LIST, facets);
        intent.putExtras(bundle);
        return intent;
    }

    private List<Facet> getFacets() {
        return getIntent().getParcelableArrayListExtra(KEY_FACET_LIST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_filter);

        final TextView sortOrderLabel = (TextView) findViewById(R.id.ascDescTextView);
        SwitchCompat sortOrderButton = (SwitchCompat) findViewById(R.id.ascDescSwitch);

        assert sortOrderButton != null;
        sortOrderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    //Do something when Switch button is on/checked
                    sortOrderLabel.setText(SearchRequestBuilder.SortOrder.ASCENDING.toString());
                    sortOrder = SearchRequestBuilder.SortOrder.ASCENDING;
                } else {
                    //Do something when Switch is off/unchecked
                    sortOrderLabel.setText(SearchRequestBuilder.SortOrder.DESCENDING.toString());
                    sortOrder = SearchRequestBuilder.SortOrder.DESCENDING;
                }
            }
        });
        sortOrderButton.setChecked(true);

        ListView sortListView = (ListView) findViewById(R.id.sortListView);
        ListView filterListView = (ListView) findViewById(R.id.filterListView);

        sortedAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, SearchResultProduct.Field.values());

        final Map<String, Facet> facetsMap = new HashMap<>();
        for (Facet facet : getFacets()) {
            facetsMap.put(facet.getType(), facet);
        }

        filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(facetsMap.keySet()));
        sortListView.setAdapter(sortedAdapter);
        filterListView.setAdapter(filterAdapter);


        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sortBy = sortedAdapter.getItem(position);
            }
        });

        filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchFilterSelector(facetsMap.get(filterAdapter.getItem(position)));
            }
        });

        ListUtils.setDynamicHeight(sortListView);
        ListUtils.setDynamicHeight(filterListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        menu.findItem(R.id.addUser).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.done:
                sendFilterSortSettingsResult(sortBy, filterBys);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchFilterSelector(Facet facet) {
        if(facet != null && facet.getFilters() != null && !facet.getFilters().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final List<Filter> facetFilters = facet.getFilters();
            final List<String> facetFilterStrings = new ArrayList<>();
            final boolean[] selectedFilters = new boolean[facet.getFilters().size()];
            for(int i = 0; i < facetFilters.size(); i++) {
                facetFilterStrings.add(facetFilters.get(i).getValue());
                if(filterBys.contains(facetFilters.get(i))) {
                    selectedFilters[i] = true;
                } else {
                    selectedFilters[i] = false;
                }
            }

            CharSequence[] filterStrings = facetFilterStrings.toArray(new CharSequence[facetFilterStrings.size()]);

            builder.setTitle(facet.getType());
            builder.setMultiChoiceItems(filterStrings,
                    selectedFilters,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {

                            Filter filter = facetFilters.get(whichButton);

                            if (filterBys.contains(filter)) {
                                filterBys.remove(filter);
                            } else {
                                filterBys.add(filter);
                            }
                        }
                    });
            builder.setPositiveButton("Done",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
    }

    private void sendFilterSortSettingsResult(SearchResultProduct.Field selectedSortedBys, List<Filter> selectedFilterBys) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_SELECTED_SORTED_BY, selectedSortedBys);
        returnIntent.putParcelableArrayListExtra(KEY_SELECTED_FILTER_BY, new ArrayList<Parcelable>(selectedFilterBys));
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_SELECTED_SORT_ORDER, sortOrder);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
