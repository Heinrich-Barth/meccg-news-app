package github.heinrichbarth.meccgevents.ui.records;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.TrackRecord;
import github.heinrichbarth.meccgevents.databinding.FragmentTropyEntryBinding;
import github.heinrichbarth.meccgevents.ui.OnCardClickImpl;

public class TropyEntryFragment extends Fragment {

    private FragmentTropyEntryBinding binding;

    @NotNull
    private final TrackRecord record;
    private final TrackRecordsFragment parent;

    public TropyEntryFragment(@NotNull TrackRecordsFragment parent, @NotNull TrackRecord record)
    {
        this.record = record;
        this.parent = parent;
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

    private void removeAfterConfirmation()
    {
        if (getActivity() == null)
            return;

        new AlertDialog.Builder(getActivity())
                .setTitle(getText(R.string.record_delete_title))
                .setMessage(getText(R.string.record_delete_text))
                .setPositiveButton(getText(R.string.record_delete_yes), (DialogInterface dialog, int id) ->
                {
                    if (binding != null)
                        binding.recordEntryFrame.setVisibility(View.INVISIBLE);

                    parent.removeRecordEntry(record.getId());
                })
                .setNegativeButton(getText(R.string.record_delete_no), (DialogInterface dialog, int id) -> { })
                .setCancelable(false)
                .create()
                .show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentTropyEntryBinding.inflate(inflater, container, false);

        binding.recordDetailName.setText(record.getOpponentName());
        binding.recordDetailTime.setText(getDate());
        binding.recordDetailTpSelf.setText(record.getTournamentPoints());
        binding.recordDetailTpOpp.setText(record.getOpponentTournamentPoints());

        binding.recordDetailTpSelf.setText(record.getTournamentPoints());
        binding.recordDetailTpOpp.setText(record.getOpponentTournamentPoints());

        binding.recordEntryCard.setClickable(true);
        binding.recordEntryCard.setOnClickListener((View v) -> removeAfterConfirmation());
        ;
        if(getContext() == null)
            return binding.getRoot();

        final int colWon = ContextCompat.getColor(getContext(), R.color.game_won);
        final int colLost = ContextCompat.getColor(getContext(), R.color.game_lost);
        final int tpSelf = toInt(record.getTournamentPoints());
        final int tpOpp = toInt(record.getOpponentTournamentPoints());

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
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
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