package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.CardDataItem;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.databinding.FragmentEventBinding;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsBinding;

public class EventFragment extends Fragment {

    private final EventItem item;

    public EventFragment(@NotNull EventItem pItem)
    {
        this.item = pItem;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final FragmentEventBinding binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventTitle.setText(item.getTitle());
        binding.eventSubheadline.setText(GenericDetailFragment.fullDate(item.getSubheadline()));

        binding.eventCard.setClickable(true);
        binding.eventCard.setOnClickListener(new OnCardClickImpl(item.getId(), R.id.action_nav_home_to_eventDetailFragment));

        return binding.getRoot();
    }

}