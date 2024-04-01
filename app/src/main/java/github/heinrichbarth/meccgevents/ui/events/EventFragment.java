package github.heinrichbarth.meccgevents.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.databinding.FragmentEventBinding;
import github.heinrichbarth.meccgevents.ui.GenericDetailFragment;
import github.heinrichbarth.meccgevents.ui.OnCardClickImpl;

public class EventFragment extends Fragment {

    private final EventItem item;
    private final int actionId;

    public EventFragment(@NotNull EventItem pItem, int actionId)
    {
        this.item = pItem;
        this.actionId = actionId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final FragmentEventBinding binding = FragmentEventBinding.inflate(inflater, container, false);
        binding.eventTitle.setText(item.getTitle());
        binding.eventSubheadline.setText(GenericDetailFragment.fullDate(item.getSubheadline()));

        binding.eventCard.setClickable(true);
        binding.eventCard.setOnClickListener(new OnCardClickImpl(item.getId(), actionId));

        return binding.getRoot();
    }

}