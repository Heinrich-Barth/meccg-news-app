package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.databinding.FragmentEventDetailBinding;

public class EventDetailFragment extends GenericDetailFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final String eventId = getDataId(savedInstanceState);
        final EventItem item = DataRepository.get().getEventById(eventId);
        if (item == null)
            return null;

        final FragmentEventDetailBinding binding = FragmentEventDetailBinding.inflate(inflater, container, false);

        binding.eventDetailTitle.setText(item.getTitle());
        binding.eventDetailDate.setText(item.getDate());
        binding.eventDetailSummary.setText(item.getSummary());

        if (!item.hasText() || getActivity() == null)
            binding.eventDetailsLayout.setVisibility(View.INVISIBLE);
        else
        {
            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            item.getText().forEach(_item -> ft.add(R.id.event_details_layout, new TextblockFragment(_item.getTitle(), _item.getText())));

            ft.commit();
        }

        if (item.getUrl().isEmpty())
            binding.buttonUrl.setVisibility(View.INVISIBLE);
        else {
            binding.buttonUrl.setText(item.getUrl());
            binding.buttonUrl.setOnClickListener(v -> openURL(item.getUrl()));
        }

        if (item.getAddress().isEmpty())
            binding.eventDetailAddress.setVisibility(View.INVISIBLE);
        else
            binding.eventDetailAddress.setText(item.getAddress());

        if (item.getVenue().isEmpty())
            binding.eventDetailVenue.setVisibility(View.INVISIBLE);
        else
            binding.eventDetailVenue.setText(item.getVenue());

        if (item.getVenue().isEmpty() && item.getAddress().isEmpty())
            binding.layoutAddress.setVisibility(View.INVISIBLE);

        setActivityTitle(item.getTitle());
        return binding.getRoot();
    }

}