package github.heinrichbarth.meccgevents.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.databinding.FragmentGalleryBinding;
import github.heinrichbarth.meccgevents.ui.TopActionBarInteractionFragment;

public class GalleryFragment extends TopActionBarInteractionFragment {

    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        showWebViewContent(binding.textResource, "/resources/content.html");
        setActivityTitle(getString(R.string.menu_gallery));
        changeToolbarImage(R.drawable.resources_background);
        setRefreshCallbackEvent(null);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}