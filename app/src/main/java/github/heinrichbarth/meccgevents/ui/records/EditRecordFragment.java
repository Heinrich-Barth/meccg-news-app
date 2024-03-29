package github.heinrichbarth.meccgevents.ui.records;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.TrackRecord;
import github.heinrichbarth.meccgevents.databinding.FragmentEditRecordBinding;

public class EditRecordFragment extends DialogFragment {

    @Nullable
    private final TrackRecord data;
    @NotNull
    private final TrackRecordsFragment caller;

    private FragmentEditRecordBinding binding;

    @NotNull
    private static final String[] POINTS = {"0", "1", "2", "3", "4", "5", "6"};

    public EditRecordFragment (@NotNull TrackRecordsFragment caller, @Nullable TrackRecord data)
    {
        this.data = data;
        this.caller = caller;
    }

    private boolean isValidEntry(@NotNull TrackRecord elem)
    {
        return !elem.getOpponentName().isEmpty()
                && !elem.getTournamentPoints().isEmpty()
                && !elem.getOpponentTournamentPoints().isEmpty();
    }


    private void doSave(View view)
    {
        boolean bSave = false;
        if (data == null)
        {
            final TrackRecord elem = new TrackRecord();

            if (populateRecord(elem)) {
                DataRepository.get().addRecord(elem);
                bSave = true;
            }
        }
        else
            bSave = populateRecord(data);

        dismiss();

        if (bSave)
            caller.onSaveRecord();
    }

    private boolean populateRecord(@NotNull TrackRecord record)
    {
        record.setNotes(getText(binding.recordEditNotes));
        record.setOpponentName(getText(binding.recordEditOppName));

        record.setPoints(getText(binding.recordEditMyMps));
        record.setTournamentPoints(getText(binding.selectTpSelfLayout));
        record.setOpponentTournamentPoints(getText(binding.selectTpOppLayout));
        record.setOpponentPoints(getText(binding.recordEditOppMps));

        return isValidEntry(record);
    }

    private String getText(@Nullable TextInputLayout pInput)
    {
        if (pInput == null)
            return "";

        final EditText pEditText = pInput.getEditText();
        if (pEditText == null)
            return "";
        else
            return getText(pEditText);
    }

    private String getText(@NotNull EditText input)
    {
        CharSequence val = input.getText();
        return val == null ? "" : val.toString();
    }

    private void populateView(@NotNull TrackRecord record)
    {
        // TODO editing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditRecordBinding.inflate(inflater, container, false);

        binding.buttonEditrecordCancal.setClickable(true);
        binding.buttonEditrecordCancal.setOnClickListener((view) -> dismiss());

        binding.buttonEditrecordSave.setClickable(true);
        binding.buttonEditrecordSave.setOnClickListener(this::doSave);


        binding.selectTpSelf.setThreshold(1);
        binding.selectTpSelf.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, POINTS));

        binding.selectTpOpp.setThreshold(1);
        binding.selectTpOpp.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, POINTS));

        if (data != null)
            populateView(data);

        return binding.getRoot();
    }

}