package github.heinrichbarth.meccgevents.ui.records;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;

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

    public EditRecordFragment (@NotNull TrackRecordsFragment caller, @Nullable TrackRecord data)
    {
        this.data = data;
        this.caller = caller;
    }

    private void doSave(View view)
    {
        if (data == null)
        {
            final TrackRecord elem = new TrackRecord();
            populateRecord(elem);
            DataRepository.get().addRecord(elem);
        }
        else
            populateRecord(data);

        dismiss();
        caller.onSaveRecord();
    }

    private void populateRecord(@NotNull TrackRecord record)
    {
        record.setNotes(getText(binding.recordEditNotes));
        record.setOpponentName(getText(binding.recordEditOppName));

        record.setPoints(getText(binding.recordEditMyMps));
        record.setTournamentPoints(getText(binding.recordEditMyTp));
        record.setOpponentTournamentPoints(getText(binding.recordEditOppTp));
        record.setOpponentPoints(getText(binding.recordEditOppMps));
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

        if (data != null)
            populateView(data);

        return binding.getRoot();
    }

}