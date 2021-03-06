/*
 * Copyright (c) Microsoft. All rights reserved. Licensed under the MIT license.
 * See LICENSE in the project root for license information.
 */
package com.microsoft.office365.meetingfeedback.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.microsoft.office365.meetingfeedback.BaseFragment;
import com.microsoft.office365.meetingfeedback.CalendarActivity;
import com.microsoft.office365.meetingfeedback.R;
import com.microsoft.office365.meetingfeedback.model.DataStore;
import com.microsoft.office365.meetingfeedback.model.meeting.EventDecorator;

import java.util.List;

import javax.inject.Inject;

public class CalendarPageFragment extends BaseFragment {

    @Inject
    DataStore mDataStore;
    private RecyclerView mEventsRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mNoEventsFoundIndicator;

    public int mPage;

    public static final String PAGE_NUMBER = "PAGE_NUMBER";
    private List<EventDecorator> mEventDecorators;

    public static CalendarPageFragment newInstance(int page) {
        CalendarPageFragment calendarPageFragment = new CalendarPageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_NUMBER, page);
        calendarPageFragment.setArguments(bundle);
        return calendarPageFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mPage = arguments.getInt(PAGE_NUMBER);
        mEventDecorators = mDataStore.getEventGroups().get(mPage).mGroupedEventDecorators;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
        setupAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_page, container, false);
        mEventsRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_calendar_page_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_calendar_page_swipe_refresh_layout);
        mNoEventsFoundIndicator = (TextView) view.findViewById(R.id.fragment_calendar_page_no_events_indicator);
        mSwipeRefreshLayout.setOnRefreshListener((CalendarActivity)getActivity());
        return view;
    }

    public void setupAdapter() {
        if (mEventDecorators.size() > 0) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
            mNoEventsFoundIndicator.setVisibility(View.GONE);
            EventsRecyclerViewAdapter adapter = new EventsRecyclerViewAdapter(getActivity(), mEventDecorators);
            mEventsRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mNoEventsFoundIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEventsRecyclerView.setHasFixedSize(true);
        mEventsRecyclerView.setLayoutManager(layoutManager);
        mEventsRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

}
