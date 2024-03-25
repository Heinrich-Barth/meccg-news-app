package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.NewsItem;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsDetailsBinding;

public class NewsDetailsFragment extends GenericDetailFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final String newsId = getDataId(savedInstanceState);
        final NewsItem item = DataRepository.get().getNewsById(newsId);
        if (item == null)
            return null;

        final FragmentNewsDetailsBinding binding = FragmentNewsDetailsBinding.inflate(inflater, container, false);

        binding.newsDetailsTitle.setText(item.getTitle());
        binding.newsDetailsDate.setText(item.getSubheadline());

        if (!item.hasText() || getActivity() == null)
            binding.newsDetailTextContainer.setVisibility(View.INVISIBLE);
        else
        {
            final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();

            item.getText().forEach(_item -> ft.add(R.id.news_detail_text_container, new TextblockFragment(_item.getTitle(), _item.getText())));

            ft.commit();
        }
        setActivityTitle(item.getTitle());
        return binding.getRoot();
    }
}