package github.heinrichbarth.meccgevents.ui;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.R;

class OnCardClickImpl implements View.OnClickListener
{
    private final String id;
    private final int actionId;

    OnCardClickImpl(@NotNull String id, int actionId)
    {
        this.id = id;
        this.actionId = actionId;
    }

    @Override
    public void onClick(View v)
    {
        final Bundle bundle = new Bundle();
        bundle.putString("id", id);
        Navigation.findNavController(v).navigate(actionId, bundle);
    }
}