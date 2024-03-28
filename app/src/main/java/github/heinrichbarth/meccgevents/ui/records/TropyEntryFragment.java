package github.heinrichbarth.meccgevents.ui.records;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.TrackRecord;
import github.heinrichbarth.meccgevents.databinding.FragmentTropyEntryBinding;

public class TropyEntryFragment extends Fragment {

    @NotNull
    private final TrackRecord record;

    public TropyEntryFragment(@NotNull TrackRecord record)
    {
        this.record = record;
    }

    private String getDate()
    {
        try {
            final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());
            return dateFormat.format(new Date(record.getTime())).toUpperCase(Locale.ENGLISH);
        }
        catch (RuntimeException exIgnore)
        {
            /* ignore */
        }

        return "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FragmentTropyEntryBinding binding = FragmentTropyEntryBinding.inflate(inflater, container, false);

        binding.recordDetailName.setText(record.getOpponentName());
        binding.recordDetailTime.setText(getDate());
        binding.recordDetailTpSelf.setText(record.getTournamentPoints());
        binding.recordDetailTpOpp.setText(record.getOpponentTournamentPoints());

        final int tpSelf = toInt(record.getTournamentPoints());
        final int tpOpp = toInt(record.getOpponentTournamentPoints());

        int colWon = ContextCompat.getColor(getContext(), R.color.game_won);
        int colLost = ContextCompat.getColor(getContext(), R.color.game_lost);

        binding.recordDetailTpSelf.setText(record.getTournamentPoints());
        binding.recordDetailTpOpp.setText(record.getOpponentTournamentPoints());

        if (tpSelf > tpOpp)
        {
            binding.recordDetailTpSelfCard.setCardBackgroundColor(colWon);
            binding.recordDetailTpOppCard.setCardBackgroundColor(colLost);
        }
        else if (tpSelf < tpOpp)
        {
            binding.recordDetailTpSelfCard.setCardBackgroundColor(colLost);
            binding.recordDetailTpOppCard.setCardBackgroundColor(colWon);
        }

        return binding.getRoot();
    }

    private int toInt(@NotNull String value)
    {
        try {
            if (!value.isEmpty())
                return Integer.parseInt(value);
        }
        catch (NumberFormatException exIgnore)
        {
            /* ignore */
        }
        return 0;
    }
}