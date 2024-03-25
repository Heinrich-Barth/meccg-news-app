package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.databinding.FragmentNewsBinding;
import github.heinrichbarth.meccgevents.databinding.FragmentSpaceBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpaceFragment extends Fragment {

    public SpaceFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final FragmentSpaceBinding binding = FragmentSpaceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}