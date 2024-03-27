package github.heinrichbarth.meccgevents.ui.news;

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

import java.util.List;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsListBinding;
import github.heinrichbarth.meccgevents.ui.GenericDetailFragment;

public class NewsListFragment extends GenericDetailFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final FragmentNewsListBinding binding = FragmentNewsListBinding.inflate(inflater, container, false);

        @NotNull List<NewsItem> vpNews = DataRepository.get().getNews(0);

        final FragmentActivity activity = vpNews.isEmpty() ? null : getActivity();
        if (activity != null) {
            final FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            for (NewsItem item : vpNews)
                ft.add(R.id.news_list_layout, new NewsFragment(item, R.id.action_newsListFragment_to_newsDetailsFragment));

            ft.commit();
        }

        setActivityTitle(getString(R.string.menu_news_list));
        changeToolbarImage(R.drawable.toolbar_image_news);
        return binding.getRoot();
    }
}