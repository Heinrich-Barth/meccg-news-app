package github.heinrichbarth.meccgevents.ui;

import android.graphics.Color;
import android.util.Log;
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
    private static final String TAG = "TopActionBarInteractionFragment";

    protected void setActivityTitle(@NotNull String sText)
    {
        final CollapsingToolbarLayout actionBar = getActionBar();
        if (actionBar != null)
            actionBar.setTitle(sText);
    }

    protected void onRefreshCallbackEvent()
    {
        /* allow overwrite */
    }

    @Override
    public void onStart() {
        super.onStart();

        final FragmentActivity activity = getActivity();
        if (activity == null)
        {
            Log.w(TAG, "Cannot get activity to set refresh callback");
            return;
        }
        final SwipeRefreshLayout layout = activity.findViewById(R.id.swipe_refresh_layout);
        if (layout == null) {
            Log.w(TAG, "Cannot get SwipeRefreshLayout");
            return;
        }

        layout.setOnRefreshListener(() -> {
            try {
                onRefreshCallbackEvent();
            }
            catch (RuntimeException ex)
            {
                Log.e(TAG, ex.getMessage(), ex);
            }
            finally
            {
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
