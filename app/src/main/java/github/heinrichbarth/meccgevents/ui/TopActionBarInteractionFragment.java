package github.heinrichbarth.meccgevents.ui;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.R;

public abstract class TopActionBarInteractionFragment extends Fragment
{
    protected void setActivityTitle(@NotNull String sText)
    {
        final CollapsingToolbarLayout actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(sText);
    }

    protected void setRefreshCallbackEvent(@Nullable final ISwipeRefreshListener callback)
    {
        final FragmentActivity activity = getActivity();
        final SwipeRefreshLayout layout = activity == null ? null : activity.findViewById(R.id.swipe_refresh_layout);
        if (layout == null)
            return;

        layout.setOnRefreshListener(() -> {
            try {
                Log.i("TopActionBarInteractionFragment", "REFRESH ACTION");
                if (callback != null)
                    callback.onPerformRefreshAction();
            }
            finally {
                layout.setRefreshing(false);
            }
        });
    }

    protected void showWebViewContent(@NotNull WebView webView, @NotNull String sFile)
    {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl("file:///android_asset" + sFile);
    }

    @Nullable
    private CollapsingToolbarLayout getActionBar()
    {
        final FragmentActivity activity = getActivity();
        return activity == null ? null : activity.findViewById(R.id.collapsing_toolbar);
    }

    protected void changeToolbarImage(int imageId)
    {
        final FragmentActivity activity = getActivity();
        final ImageView image = activity == null ? null : activity.findViewById(R.id.collapsing_toolbar_image);
        if (image != null)
            image.setImageResource(imageId);
    }

    public interface ISwipeRefreshListener {

        public void onPerformRefreshAction();
    }

}
