package github.heinrichbarth.meccgevents.ui.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentEventListBinding;
import github.heinrichbarth.meccgevents.ui.GenericDetailFragment;
import github.heinrichbarth.meccgevents.ui.news.NewsFragment;

public class EventListFragment extends GenericDetailFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final FragmentEventListBinding binding = FragmentEventListBinding.inflate(inflater, container, false);

        loadEvents();

        setActivityTitle(getString(R.string.menu_events_list));
        changeToolbarImage(R.drawable.event_background);
        return binding.getRoot();
    }

    private void loadEvents()
    {
        @NotNull final DataRepository repo = DataRepository.get();
        @NotNull final List<EventItem> vpRL = repo.getEvents(0, false);
        @NotNull final List<EventItem> vpOnline = repo.getEvents(0, true);

        final FragmentActivity activity = vpRL.isEmpty() && vpOnline.isEmpty() ? null : getActivity();
        if (activity == null)
            return;

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        for (EventItem item : vpRL)
            ft.add(R.id.event_list_listrl, new EventFragment(item, R.id.action_eventListFragment_to_eventDetailFragment));

        for (EventItem item : vpOnline)
            ft.add(R.id.event_list_listonline, new EventFragment(item, R.id.action_eventListFragment_to_eventDetailFragment));

        ft.commit();
    }

}