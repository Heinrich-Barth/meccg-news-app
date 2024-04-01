package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.databinding.FragmentTextblockBinding;

public class TextblockFragment extends Fragment {

    @NotNull
    private final String title;

    @NotNull
    private final String text;

    public TextblockFragment(@NotNull String sTitle, @NotNull String sText)
    {
        title = sTitle;
        text = sText;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentTextblockBinding binding = FragmentTextblockBinding.inflate(inflater, container, false);

        setTextOrHide(binding.textblockTitle, title);
        setTextOrHide(binding.textblockText, text);

        return binding.getRoot();
    }

    private void setTextOrHide(TextView pTextView, @NotNull String text)
    {
        if (text.isEmpty())
            pTextView.setVisibility(View.INVISIBLE);
        else
            pTextView.setText(text);
    }
}