package github.heinrichbarth.meccgevents.ui.records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    private boolean isValidEntry(@NotNull TrackRecord elem)
    {
        return !elem.getOpponentName().isEmpty()
                && !elem.getTournamentPoints().isEmpty()
                && !elem.getOpponentTournamentPoints().isEmpty();
    }

    @NotNull
    private String[] createPointArray(int nMax)
    {
        final String[] arr = new String[nMax+1];
        for (int i = 0; i <= nMax; i++)
            arr[i] = "" + i;

        return arr;
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
        record.setOpponentName(getText(binding.recordEditOppName));

        record.setPoints(getText(binding.recordEditMyMps));
        record.setTournamentPoints(getText(binding.selectTpSelfLayout));
        record.setOpponentTournamentPoints(getText(binding.selectTpOppLayout));
        record.setOpponentPoints(getText(binding.recordEditOppMps));
        record.setEventName(getText(binding.recordEditEventDropdown));

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

    private List<String> getEventNames()
    {
        final List<String> vsEvents = DataRepository.get().getEventNames();
        final List<String> vsResult = new ArrayList<>(vsEvents.size() + 2);
        vsResult.add(getString(R.string.game_type_standard));
        vsResult.add(getString(R.string.game_type_dreamcards));
        if (!vsEvents.isEmpty()) {
            vsResult.add(getString(R.string.game_type_choose));
            vsResult.addAll(vsEvents);
        }

        return vsResult;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditRecordBinding.inflate(inflater, container, false);

        binding.buttonEditrecordCancal.setClickable(true);
        binding.buttonEditrecordCancal.setOnClickListener((view) -> dismiss());

        binding.buttonEditrecordSave.setClickable(true);
        binding.buttonEditrecordSave.setOnClickListener(this::doSave);

        final String[] arrTPs = createPointArray(7);
        final String[] arrMps = createPointArray(50);

        binding.selectTpSelf.setThreshold(1);
        binding.selectTpSelf.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, arrTPs));

        binding.selectTpOpp.setThreshold(1);
        binding.selectTpOpp.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, arrTPs));

        binding.recordEditMyMps.setThreshold(1);
        binding.recordEditMyMps.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, arrMps));

        binding.recordEditOppMps.setThreshold(1);
        binding.recordEditOppMps.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, arrMps));

        binding.recordEditEventDropdown.setThreshold(1);
        binding.recordEditEventDropdown.setAdapter(new ArrayAdapter<>(getContext(), R.layout.layout_tournament_points, getEventNames()));

        if (data != null)
            populateView(data);

        return binding.getRoot();
    }

}