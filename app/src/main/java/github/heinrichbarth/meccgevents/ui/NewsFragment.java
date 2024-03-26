package github.heinrichbarth.meccgevents.ui;

import static androidx.navigation.Navigation.findNavController;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.CardDataItem;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsBinding;

public class NewsFragment extends Fragment {

    private final CardDataItem item;

    public NewsFragment(@NotNull CardDataItem pItem)
    {
        this.item = pItem;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final FragmentNewsBinding binding = FragmentNewsBinding.inflate(inflater, container, false);
        binding.newsTitle.setText(item.getTitle());
        binding.newsSubheadline.setText(GenericDetailFragment.fullDate(item.getSubheadline()));
        binding.newsCard.setClickable(true);
        binding.newsCard.setOnClickListener(new OnCardClickImpl(item.getId(), R.id.action_nav_home_to_newsDetailsFragment));

        return binding.getRoot();
    }
}