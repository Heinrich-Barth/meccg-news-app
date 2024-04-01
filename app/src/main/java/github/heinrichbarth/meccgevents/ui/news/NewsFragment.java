package github.heinrichbarth.meccgevents.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.data.CardDataItem;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsBinding;
import github.heinrichbarth.meccgevents.ui.GenericDetailFragment;
import github.heinrichbarth.meccgevents.ui.OnCardClickImpl;

public class NewsFragment extends Fragment {

    private final CardDataItem item;
    private final int actionId;

    public NewsFragment(@NotNull CardDataItem pItem, int actionDetailsNavId)
    {
        this.item = pItem;
        this.actionId = actionDetailsNavId;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final FragmentNewsBinding binding = FragmentNewsBinding.inflate(inflater, container, false);
        binding.newsTitle.setText(item.getTitle());
        binding.newsSubheadline.setText(GenericDetailFragment.fullDate(item.getSubheadline()));
        binding.newsCard.setClickable(true);
        binding.newsCard.setOnClickListener(new OnCardClickImpl(item.getId(), actionId));
        return binding.getRoot();
    }
}