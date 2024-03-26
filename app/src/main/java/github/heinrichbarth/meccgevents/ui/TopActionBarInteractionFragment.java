package github.heinrichbarth.meccgevents.ui;

import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.MainActivity;
import github.heinrichbarth.meccgevents.R;

public abstract class TopActionBarInteractionFragment extends Fragment
{
    protected void setActivityTitle(@NotNull String sText)
    {
        final CollapsingToolbarLayout actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(sText);
    }

    @Nullable
    private CollapsingToolbarLayout getActionBar()
    {
        final FragmentActivity activity = getActivity();
        return activity == null ? null : activity.findViewById(R.id.collapsing_toolbar);
    }
}
