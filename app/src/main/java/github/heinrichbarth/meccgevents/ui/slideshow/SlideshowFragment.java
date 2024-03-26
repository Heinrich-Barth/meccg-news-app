package github.heinrichbarth.meccgevents.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.databinding.FragmentSlideshowBinding;
import github.heinrichbarth.meccgevents.ui.TopActionBarInteractionFragment;

public class SlideshowFragment extends TopActionBarInteractionFragment {

    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        showWebViewContent(binding.textAbout, "/about/content.html");
        setActivityTitle(getString(R.string.menu_slideshow));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}