package com.richrelevance.richrelevance.FindDemo;


import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.richrelevance.Callback;
import com.richrelevance.Error;
import com.richrelevance.RichRelevance;
import com.richrelevance.find.autocomplete.AutoCompleteBuilder;
import com.richrelevance.find.autocomplete.AutoCompleteResponseInfo;
import com.richrelevance.find.autocomplete.AutoCompleteSuggestion;
import com.richrelevance.find.search.Facet;
import com.richrelevance.find.search.Filter;
import com.richrelevance.find.search.SearchRequestBuilder;
import com.richrelevance.find.search.SearchResponseInfo;
import com.richrelevance.find.search.SearchResultProduct;
import com.richrelevance.recommendations.Placement;
import com.richrelevance.richrelevance.R;

import java.util.ArrayList;
import java.util.List;

import static com.richrelevance.richrelevance.FindDemo.CatalogProductDetailActivity.createCatalogProductDetailActivityIntent;
import static com.richrelevance.richrelevance.FindDemo.SearchSortFilterActivity.KEY_SELECTED_FILTER_BY;
import static com.richrelevance.richrelevance.FindDemo.SearchSortFilterActivity.KEY_SELECTED_SORTED_BY;
import static com.richrelevance.richrelevance.FindDemo.SearchSortFilterActivity.KEY_SELECTED_SORT_ORDER;
import static com.richrelevance.richrelevance.FindDemo.SearchSortFilterActivity.createSearchSortFilterActivityIntent;

public class SearchActivity extends FindBaseActivity {

    private static final int SELECT_SORT_FILTER_RESULT = 199;

    private FloatingSearchView searchView;

    private CatalogProductsAdapter adapter;

    private ViewFlipper viewFlipper;

    private View emptyState;

    private RecyclerView recyclerView;

    private FloatingActionButton fabSortFilter;

    private Button backButton;

    private Button nextButton;

    private String query;

    private SearchResultProduct.Field sortBy;

    private SearchRequestBuilder.SortOrder sortOrder;

    private List<Filter> filters = new ArrayList<>();

    private int offSet = 0;

    private final int RESULT_SIZE = 20;

    private ArrayList<Facet> facets = new ArrayList<>();

    private String addToCartParam;

    public static Intent createSearchActivityIntent(Activity activity) {
        return new Intent(activity, SearchActivity.class);
    }

    @Override
    protected void loadActivity() {
        setContentView(R.layout.activity_search);

        if (getSupportActionBar() != null) {
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        }

        searchView = (FloatingSearchView) findViewById(R.id.searchView);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        emptyState = findViewById(R.id.emptyState);

        fabSortFilter = (FloatingActionButton) findViewById(R.id.fabSortFilter);
        fabSortFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(createSearchSortFilterActivityIntent(SearchActivity.this, facets), SELECT_SORT_FILTER_RESULT);
            }
        });

        setUpSearchView();

        adapter = new CatalogProductsAdapter() {
            private boolean hasProducts = false;

            @Override
            public void onProductClicked(SearchResultProduct product) {
                startActivity(createCatalogProductDetailActivityIntent(SearchActivity.this, product, addToCartParam));
            }

            @Override
            protected void onNotifiedDataSetChanged(boolean hasProducts) {
                if (this.hasProducts != hasProducts) {
                    if (hasProducts) {
                        showValidResults();
                        enablePaginationButton(nextButton);
                    } else {
                        if (offSet > 0) {
                            showPagination();
                            disablePaginationButton(nextButton);
                        } else {
                            showEmptyState();
                        }
                    }
                    this.hasProducts = hasProducts;
                }
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        backButton = (Button) findViewById(R.id.back);
        nextButton = (Button) findViewById(R.id.next);
    }

    private void setUpSearchView() {
        searchView.setCloseSearchOnKeyboardDismiss(true);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                query = newQuery;
                resetSearch();
                executeSearch();
                executeAutoComplete();
            }
        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                query = searchSuggestion.getBody();
                resetSearch();
                executeSearch();
            }

            @Override
            public void onSearchAction(String currentQuery) {
                query = currentQuery;
                resetSearch();
                executeSearch();
            }
        });

        searchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                onBackPressed();
            }
        });
    }

    private void resetSearch() {
        offSet = 0;
        sortBy = null;
        sortOrder = null;
        filters = new ArrayList<>();
        addToCartParam = null;
        disablePaginationButton(backButton);
        enablePaginationButton(nextButton);
    }

    public void onBackClicked(View view) {
        if (offSet >= RESULT_SIZE) {
            offSet -= RESULT_SIZE;
        } else {
            offSet = 0;
        }

        if (offSet == 0) {
            disablePaginationButton(backButton);
        }
        executeSearch();
    }

    public void onNextClicked(View view) {
        offSet += RESULT_SIZE;

        if (offSet > 0) {
            enablePaginationButton(backButton);
        }
        executeSearch();
    }

    private void disablePaginationButton(Button button) {
        button.setClickable(false);
        button.setTextColor(button.getDrawingCacheBackgroundColor());
    }

    private void enablePaginationButton(Button button) {
        button.setClickable(true);
        button.setTextColor(ContextCompat.getColor(this, R.color.Primary_Light_Main));
    }

    public void showPagination() {
        backButton.setVisibility(View.VISIBLE);
        nextButton.setVisibility(View.VISIBLE);
    }

    public void hidePagination() {
        backButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(emptyState));
        fabSortFilter.setVisibility(View.GONE);
        hidePagination();
    }

    private void showValidResults() {
        viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(recyclerView));
        fabSortFilter.setVisibility(View.VISIBLE);
        showPagination();
    }

    private void executeAutoComplete() {
        AutoCompleteBuilder builder = RichRelevance.buildAutoCompleteRequest(query, 20);
        builder.setCallback(new Callback<AutoCompleteResponseInfo>() {
            @Override
            public void onResult(final AutoCompleteResponseInfo result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null) {
                            List<AutoCompleteSearchSuggestion> searchSuggestions = new ArrayList<>();
                            for (AutoCompleteSuggestion suggestion : result.getSuggestions()) {
                                searchSuggestions.add(new AutoCompleteSearchSuggestion(suggestion));
                            }
                            searchView.swapSuggestions(searchSuggestions);
                        } else {
                            searchView.clearSuggestions();
                        }
                    }
                });
            }

            @Override
            public void onError(Error error) {
                Log.e(SearchActivity.class.getSimpleName(), "Error response from autocomplete request.");
            }
        });
        builder.execute();
    }

    private void executeSearch() {
        SearchRequestBuilder builder = RichRelevance.buildSearchRequest(query, new Placement(Placement.PlacementType.SEARCH, "find"));
        if (sortBy != null && sortOrder != null) {
            builder.setSort(sortBy, sortOrder);
        }
        if (filters != null) {
            builder.setFilters(filters);
        }
        builder.setRows(RESULT_SIZE);
        builder.setStart(offSet);
        builder.setCallback(new Callback<SearchResponseInfo>() {
                                @Override
                                public void onResult(final SearchResponseInfo result) {

                                    runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          if (result == null || result.getProducts() == null || result.getFacets() == null) {
                                                              adapter.setProducts(new ArrayList<SearchResultProduct>());
                                                              facets = new ArrayList<>();
                                                              showEmptyState();
                                                          } else {
                                                              addToCartParam = result.getAddToCartParams();
                                                              adapter.setProducts(result.getProducts());
                                                              facets = new ArrayList<>(result.getFacets());

                                                              if (!result.getProducts().isEmpty()) {
                                                                  showValidResults();
                                                              } else {
                                                                  if (offSet > 0) {
                                                                      disablePaginationButton(nextButton);
                                                                  } else {
                                                                      showEmptyState();
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }

                                    );

                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

        );
        builder.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_SORT_FILTER_RESULT) {

            if (resultCode == Activity.RESULT_OK) {
                SearchResultProduct.Field sortBy = (SearchResultProduct.Field) data.getSerializableExtra(KEY_SELECTED_SORTED_BY);
                SearchRequestBuilder.SortOrder sortOrder = (SearchRequestBuilder.SortOrder) data.getSerializableExtra(KEY_SELECTED_SORT_ORDER);
                List<Filter> filters = data.getParcelableArrayListExtra(KEY_SELECTED_FILTER_BY);
                if (searchView.getQuery() != null && !searchView.getQuery().isEmpty()) {
                    this.query = searchView.getQuery();
                    this.sortBy = sortBy;
                    this.sortOrder = sortOrder;
                    this.filters = filters;
                    executeSearch();
                }
            }
        }
    }
}
