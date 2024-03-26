package github.heinrichbarth.meccgevents.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        setActivityTitle(getString(R.string.menu_gallery));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}