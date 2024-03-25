package github.heinrichbarth.meccgevents.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.MainActivity;

public abstract class TopActionBarInteractionFragment extends Fragment
{
    protected void setActionBarVisible(boolean bIsVisible)
    {
        /*
        final ActionBar actionbar = getActionBar();
        if (actionbar == null)
            return;

        if (bIsVisible)
            actionbar.show();
        else
            actionbar.hide();
         */
    }

    protected void setActivityTitle(@NotNull String sText)
    {
        final ActionBar actionBar = sText.isEmpty() ? null : getActionBar();
        if (actionBar != null)
            actionBar.setTitle(sText);

        setActionBarVisible(true);
    }

    @Nullable
    private ActionBar getActionBar()
    {
        final FragmentActivity activity = getActivity();
        if(activity instanceof MainActivity)
            return ((MainActivity) getActivity()).getSupportActionBar();
        else
            return null;
    }
}
