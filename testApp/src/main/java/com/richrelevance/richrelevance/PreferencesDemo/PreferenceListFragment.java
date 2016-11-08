package com.richrelevance.richrelevance.PreferencesDemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.richrelevance.Callback;
import com.richrelevance.RichRelevance;
import com.richrelevance.recommendations.CompleteProduct;
import com.richrelevance.recommendations.ProductResponseInfo;
import com.richrelevance.richrelevance.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PreferenceListFragment extends Fragment {

    public interface LoadingListener {
        void startLoading();

        void stopLoading();
    }

    LoadingListener loadingListener;

    private RecyclerView recyclerView;

    private PreferenceAdapter adapter;

    @Override
    public void onAttach(Activity activity) {
        if(!(activity instanceof LoadingListener)) {
            throw new RuntimeException(activity.getLocalClassName() + " does not implement " + LoadingListener.class.getSimpleName());
        }
        loadingListener = (LoadingListener) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        adapter = new PreferenceAdapter();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);

        adapter.loadData(null);

        return rootView;
    }

    @Override
    public void onDetach() {
        loadingListener = null;
        super.onDetach();
    }

    public void loadProducts(List<String> products) {
        getProducts(products);
    }

    private void getProducts(List<String> products) {
        if(loadingListener != null) {
            loadingListener.startLoading();
        }
        if((products == null || products.isEmpty()) && adapter != null) {
            adapter.loadData(null); //Load empty list
        } else {
            RichRelevance.buildProductsRequest(products).setCallback(new Callback<ProductResponseInfo>() {
                                                                         @Override
                                                                         public void onResult(final ProductResponseInfo result) {
                                                                             if(getActivity() != null) {
                                                                                 getActivity().runOnUiThread(new Runnable() {
                                                                                     @Override
                                                                                     public void run() {
                                                                                         if(adapter != null && result != null) {
                                                                                             adapter.loadData(result.getProducts());
                                                                                         }
                                                                                         if(loadingListener != null) {
                                                                                             loadingListener.stopLoading();
                                                                                         }
                                                                                     }
                                                                                 });
                                                                             }
                                                                         }

                                                                         @Override
                                                                         public void onError(final com.richrelevance.Error error) {
                                                                             if(getActivity() != null) {
                                                                                 getActivity().runOnUiThread(new Runnable() {
                                                                                     @Override
                                                                                     public void run() {
                                                                                         if(loadingListener != null) {
                                                                                             loadingListener.stopLoading();
                                                                                         }
                                                                                         Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                                                                     }
                                                                                 });
                                                                             }
                                                                         }
                                                                     }

            ).execute();
        }
    }

    public static class PreferenceAdapter extends RecyclerView.Adapter<PreferenceAdapter.ViewHolder> {

        protected List<CompleteProduct> list = new ArrayList<>();

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(ViewHolder.LAYOUT_RESOURCE, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            CompleteProduct product = list.get(position);
            if(holder.image != null && (product.getImageUrl() != null && product.getImageUrl() != "")) {
                Picasso.with(holder.image.getContext()).load(product.getImageUrl()).into(holder.image);
            }

            holder.productName.setText(product.getName());
            holder.productBrand.setText(product.getBrand());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void loadData(List<CompleteProduct> list) {
            if(list == null) {
                list = new ArrayList<>();
            }
            this.list = list;
            notifyDataSetChanged();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private static final int LAYOUT_RESOURCE = R.layout.list_item;

            public ImageView image;

            public TextView productName;

            public TextView productBrand;

            public ViewHolder(View v) {
                super(v);
                image = (ImageView) v.findViewById(R.id.image);
                productName = (TextView) v.findViewById(R.id.name);
                productBrand = (TextView) v.findViewById(R.id.brand);
            }
        }
    }
}