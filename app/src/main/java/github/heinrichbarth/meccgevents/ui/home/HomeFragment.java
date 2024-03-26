package github.heinrichbarth.meccgevents.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentHomeBinding;
import github.heinrichbarth.meccgevents.ui.EventFragment;
import github.heinrichbarth.meccgevents.ui.NewsFragment;
import github.heinrichbarth.meccgevents.ui.TopActionBarInteractionFragment;

public class HomeFragment extends TopActionBarInteractionFragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
/*
        binding.homeFrameLayout.setOnRefreshListener(() -> {
            try {
                refreshDataFromUrl();
            }
            finally {
                binding.homeFrameLayout.setRefreshing(false);
            }
        });
*/
        binding.buttonCurrentGamesMellon.setOnClickListener(v -> {

            try {
                final Uri uriUrl = Uri.parse("https://meccg.herokuapp.com");
                final Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
            catch (IllegalStateException exIngore)
            {
                /* ignore */
            }
        });

        onPopulateViewWithCachedData();
        setActivityTitle(getString(R.string.menu_home));
        return binding.getRoot();
    }

    private void refreshDataFromUrl()
    {
        if (timeLastRequested == 0 || System.currentTimeMillis() - timeLastRequested > 1000 * 60) {
            new RefreshTask(this).execute("");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        /* fetch automatically */
        refreshDataFromUrl();
    }

    private static long timeLastRequested = 0;

    private static class RefreshTask extends AsyncTask<String, Void, String>
    {
        private final HomeFragment fragment;

        private RefreshTask(HomeFragment homeFragment) {
            this.fragment = homeFragment;
        }

        @Override
        protected String doInBackground(String... params)
        {
            if (DataRepository.get().fetchData())
                timeLastRequested = System.currentTimeMillis();

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            fragment.onPopulateViewWithCachedData();
        }
    }

    private void onPopulateViewWithCachedData()
    {
        final FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        final int maxNews = 0;
        loadNews(activity, DataRepository.get().getNews(maxNews));
        loadEvents(activity, DataRepository.get().getEvents(maxNews));
        updateOnlineGames(DataRepository.get().getCurrentGames());
    }

    private void updateOnlineGames(int count)
    {
        if (count < 0)
            return;

        final String sText = binding.buttonCurrentGamesMellon.getText().toString().split(":")[0];
        binding.buttonCurrentGamesMellon.setText(sText + ": " + count);
    }

    private void loadEvents(FragmentActivity activity, List<EventItem> events)
    {
        @NotNull List<EventItem> vpRL = filterEvents(events, false);
        @NotNull List<EventItem> vpOnline = filterEvents(events, true);

        loadEvents(activity, vpRL, false);
        loadEvents(activity, vpOnline, true);
    }

    @NotNull
    private List<EventItem> filterEvents(List<EventItem> vpNews, boolean onlineEvents)
    {
        if (vpNews.isEmpty())
            return Collections.emptyList();

        final List<EventItem> vpList = new ArrayList<>();
        for (EventItem item : vpNews)
        {
            if (item.isOnline() == onlineEvents)
                vpList.add(item);
        }

        return vpList;
    }

    private void loadNews(@NotNull FragmentActivity activity, List<NewsItem> vpNews)
    {
        if (vpNews.isEmpty())
        {
           // binding.homeLayoutNews.setVisibility(View.INVISIBLE);
            return;
        }

        //clearLayout(activity, binding.homeLayoutNewsList);

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        for (NewsItem item : vpNews) {
            Log.i("TEST", "SHOW NEWS");
            ft.add(R.id.home_layout_news_list, new NewsFragment(item));
        }

        ft.commit();
        binding.homeLayoutNews.setVisibility(View.VISIBLE);
    }

    private void loadEvents(@NotNull FragmentActivity activity, List<EventItem> vpNews, boolean isOnline)
    {
        /*
        if (vpNews.isEmpty())
        {
            if (isOnline)
                binding.homeLayoutOnline.setVisibility(View.INVISIBLE);
            else
                binding.homeLayoutEventsRl.setVisibility(View.INVISIBLE);

            return;
        }
*/
        if (isOnline) {
            //binding.homeLayoutOnline.setVisibility(View.VISIBLE);
            //clearLayout(activity, binding.homeLayoutEventsOnlineList);
        }
        else
        {
            //binding.homeLayoutEventsRl.setVisibility(View.VISIBLE);
            //clearLayout(activity, binding.homeLayoyutEventsRlList);
        }

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        final int layoutId = isOnline ? R.id.home_layout_events_online_list : R.id.home_layoyut_events_rl_list;
        for (EventItem item : vpNews)
            ft.add(layoutId, new EventFragment(item));

        for (EventItem item : vpNews)
            ft.add(layoutId, new EventFragment(item));
        ft.commit();
    }

    private void clearLayout(@NotNull FragmentActivity activity, LinearLayout layout)
    {
        layout.removeAllViewsInLayout();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}