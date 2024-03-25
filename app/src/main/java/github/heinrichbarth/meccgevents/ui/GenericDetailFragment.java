package github.heinrichbarth.meccgevents.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.MainActivity;

abstract class GenericDetailFragment extends TopActionBarInteractionFragment {

    protected String getDataId(@Nullable Bundle savedInstanceState)
    {
        final String id = getFromBundle(savedInstanceState);
        if (!id.isEmpty())
            return id;

        return getFromBundle(getArguments());
    }

    private String getFromBundle(@Nullable Bundle bundle)
    {
        return bundle == null ? "" : bundle.getString("id", "");
    }

    protected void openURL (String url) {
        if (url == null || url.isEmpty())
            return;

        try {
            Uri uriUrl = Uri.parse(url);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
        }
        catch (IllegalStateException exIngore)
        {
            /* ignore */
        }
    }

}
