package com.ufcg.es.loanalert;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.os.Looper.getMainLooper;


public class MyLoansFragment extends Fragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    private RVRendererAdapter<LoanEntry> adapter;
    private Handler mainHandler = new Handler(getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return inflater.inflate(R.layout.fragment_my_loans, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        adapter = new RVRendererAdapter<>(getActivity().getLayoutInflater(),
            new MyLoansRendererBuilder(getActivity().getApplicationContext()),
            new ListAdapteeCollection<>(new LinkedList<LoanEntry>()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshLoanEntries(null);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SearchEvent event) {
        String newQuery = event.getQueryString();
        refreshLoanEntries(newQuery == null || newQuery.trim().isEmpty() ? null : newQuery);
    }

    private void refreshLoanEntries(final String queryString) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final List<LoanEntry> loanEntryList = new Select().from(LoanEntry.class).execute();
                final List<LoanEntry> filteredEntries = new LinkedList<>();
                if (queryString == null) {
                    filteredEntries.addAll(loanEntryList);
                } else {
                    for (LoanEntry loanEntry : loanEntryList) {
                        if (loanEntry.getTitle().toUpperCase().contains(queryString.toUpperCase())) {
                            filteredEntries.add(loanEntry);
                        }
                    }
                }
                mainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        adapter.clear();
                        adapter.addAll(filteredEntries);
                        adapter.notifyDataSetChanged();
                    }

                });
            }

        }).start();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
