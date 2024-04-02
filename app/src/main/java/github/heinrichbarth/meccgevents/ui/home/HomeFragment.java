package github.heinrichbarth.meccgevents.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentHomeBinding;
import github.heinrichbarth.meccgevents.ui.OnCardClickImpl;
import github.heinrichbarth.meccgevents.ui.TopActionBarInteractionFragment;
import github.heinrichbarth.meccgevents.ui.events.EventFragment;
import github.heinrichbarth.meccgevents.ui.news.NewsFragment;

public class HomeFragment extends TopActionBarInteractionFragment {

    private FragmentHomeBinding binding;

    private static final String TAG ="HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.ttleLatestNews.setClickable(true);
        binding.ttleLatestNews.setOnClickListener(new OnCardClickImpl(R.id.action_nav_home_to_newsListFragment));

        binding.titleUpcomingEvents.setClickable(true);
        binding.titleUpcomingEvents.setOnClickListener(new OnCardClickImpl(R.id.action_nav_home_to_eventListFragment));

        binding.titleUpcomingEventsOnline.setClickable(true);
        binding.titleUpcomingEventsOnline.setOnClickListener(new OnCardClickImpl(R.id.action_nav_home_to_eventListFragment));

        DataRepository.init(getActivityContext());
        onPopulateViewWithCachedData(true, true);
        setActivityTitle(getString(R.string.menu_home));

        return binding.getRoot();
    }

    @Override
    protected void onRefreshCallbackEvent()
    {
        refreshDataFromUrl(true);
    }

    @Nullable
    private Context getActivityContext()
    {
        return getActivity() == null ? null : getActivity().getBaseContext();
    }

    private void refreshDataFromUrl(boolean forceRefresh)
    {
        new RefreshNewsTask(this, forceRefresh).execute("");
        new RefreshEventsTask(this, forceRefresh).execute("");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        /* fetch automatically */
        changeToolbarImage(R.drawable.rivendell_wallpaper);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (DataRepository.get(getActivityContext()).hasAgreedToTerms()) {
            refreshDataFromUrl(false);
            return;
        }

        /* impass. we need to have user accept terms and conditions */
        final Spanned sHtml = getLegalText();
        if (getActivity() == null || sHtml == null)
            return;

        new AlertDialog.Builder(getActivity())
                .setMessage(sHtml)
                .setPositiveButton("Agree", (DialogInterface dialog, int id) -> {
                    DataRepository.get(getActivityContext()).agreeToTerms();
                    refreshDataFromUrl(false);
                })
                .setNegativeButton("Disagree", (DialogInterface dialog, int id) -> System.exit(0))
                .setCancelable(false)
                .create()
                .show();
    }


    private byte[] readStream(InputStream in) throws IOException {
        if (in == null)
            return new byte[0];

        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        final byte[] data = new byte[1024];
        while ((nRead = in.read(data, 0, data.length)) != -1)
            buffer.write(data, 0, nRead);

        buffer.flush();
        return buffer.toByteArray();
    }

    private Spanned getLegalText()
    {
        try (InputStream in = getContext().getAssets().open("about/short.html"))
        {
            final String sHtml = new String(readStream(in), StandardCharsets.UTF_8);
            return Html.fromHtml(sHtml, HtmlCompat.FROM_HTML_MODE_COMPACT);
        }
        catch (IOException | RuntimeException ex)
        {
            Log.e(TAG, ex.getMessage(), ex);
        }
        return null;
    }

    private abstract static class RefreshTask extends AsyncTask<String, Void, String>
    {
        private final HomeFragment fragment;
        private final boolean forceRefresh;

        private RefreshTask(HomeFragment homeFragment, boolean bForceRefresh) {
            this.fragment = homeFragment;
            this.forceRefresh = bForceRefresh;
        }

        @Override
        protected String doInBackground(String... params)
        {
            final Context context = fragment.getActivity() == null ? null : fragment.getActivity().getBaseContext();
            if (DataRepository.get(context).fetchData(forceRefresh, fetchNews(), fetchEvents(), false))
                return "1";
            else
                return "";
        }

        protected boolean fetchNews()
        {
            return false;
        }

        protected boolean fetchEvents()
        {
            return false;
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (result.isEmpty() || fragment == null)
                return;

            try {
                fragment.onPopulateViewWithCachedData(fetchNews(), fetchEvents());
            }
            catch (RuntimeException exIgnore)
            {
                /* since it is async, the fragment might be destroyed before this here is done */
            }

        }
    }

    private static class RefreshNewsTask extends RefreshTask
    {
        private RefreshNewsTask(HomeFragment homeFragment, boolean bForceRefresh) {
            super(homeFragment, bForceRefresh);
        }

        @Override
        protected boolean fetchNews() {
            return true;
        }

    }

    private static class RefreshEventsTask extends RefreshTask
    {
        private RefreshEventsTask(HomeFragment homeFragment, boolean bForceRefresh) {
            super(homeFragment, bForceRefresh);
        }

        @Override
        protected boolean fetchEvents() {
            return true;
        }

    }

    private void onPopulateViewWithCachedData(boolean news, boolean events)
    {
        if (!news && !events)
            return;

        final FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.w(TAG, "Cannot get activity");
            return;
        }

        final int maxNews = 3;
        @NotNull DataRepository repository = DataRepository.get();
        if (news)
            loadNews(activity, repository.getNews(maxNews));

        if (events) {
            loadEvents(activity, repository.getEvents(maxNews, false), false);
            loadEvents(activity, repository.getEvents(maxNews, true), true);
        }
    }


    private void loadNews(@NotNull FragmentActivity activity, List<NewsItem> vpNews)
    {
        if (vpNews.isEmpty())
        {
            Log.i(TAG, "No news available to display");
            binding.homeLayoutNews.setVisibility(View.INVISIBLE);
            return;
        }

        binding.homeLayoutNewsList.removeAllViewsInLayout();

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        for (NewsItem item : vpNews)
            ft.add(R.id.home_layout_news_list, new NewsFragment(item, R.id.action_nav_home_to_newsDetailsFragment));

        ft.commit();
        binding.homeLayoutNews.setVisibility(View.VISIBLE);
    }

    private void loadEvents(@NotNull FragmentActivity activity, List<EventItem> vpNews, boolean isOnline)
    {
        if (vpNews.isEmpty())
        {

            if (isOnline) {
                Log.i(TAG, "No online event available to display");
                binding.homeLayoutOnline.setVisibility(View.INVISIBLE);
            }
            else {
                Log.i(TAG, "No event available");
                binding.homeLayoutEventsRl.setVisibility(View.INVISIBLE);
            }

            return;
        }

        if (isOnline) {
            binding.homeLayoutOnline.setVisibility(View.VISIBLE);
            binding.homeLayoutEventsOnlineList.removeAllViewsInLayout();
        }
        else
        {
            binding.homeLayoutEventsRl.setVisibility(View.VISIBLE);
            binding.homeLayoyutEventsRlList.removeAllViewsInLayout();
        }

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        final int layoutId = isOnline ? R.id.home_layout_events_online_list : R.id.home_layoyut_events_rl_list;
        for (EventItem item : vpNews)
            ft.add(layoutId, new EventFragment(item, R.id.action_nav_home_to_eventDetailFragment));

        ft.commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}