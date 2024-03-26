package github.heinrichbarth.meccgevents.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

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

    @NotNull
    static String fullDate(@NotNull String sDate)
    {
        final String[] parts = sDate.split("-");
        if (parts.length != 3)
            return sDate;

        return parts[2] + " " + getMonth(parts[1])  + " " + parts[0];
    }

    private static String getMonth(String sCandidate)
    {
        switch (sCandidate)
        {
            case "01": return "JAN";
            case "02": return "FEB";
            case "03": return "MAR";
            case "04": return "APR";
            case "05": return "MAY";
            case "06": return "JUN";
            case "07": return "JUL";
            case "08": return "AUG";
            case "09": return "SEP";
            case "10": return "OCT";
            case "11": return "NOV";
            case "12": return "DEC";
            default: return sCandidate;
        }
    }

}
