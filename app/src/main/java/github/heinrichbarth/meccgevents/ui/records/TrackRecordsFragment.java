package github.heinrichbarth.meccgevents.ui.records;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

import github.heinrichbarth.meccgevents.R;
import github.heinrichbarth.meccgevents.data.DataRepository;
import github.heinrichbarth.meccgevents.data.EventItem;
import github.heinrichbarth.meccgevents.data.TrackRecord;
import github.heinrichbarth.meccgevents.databinding.FragmentSlideshowBinding;
import github.heinrichbarth.meccgevents.databinding.FragmentTrackRecordsBinding;
import github.heinrichbarth.meccgevents.ui.TopActionBarInteractionFragment;
import github.heinrichbarth.meccgevents.ui.events.EventFragment;

public class TrackRecordsFragment extends TopActionBarInteractionFragment {

    private FragmentTrackRecordsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final TrackRecordsFragment instance = this;

        binding = FragmentTrackRecordsBinding.inflate(inflater, container, false);

        final FloatingActionButton fab = binding.floatingActionButton;
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final FragmentActivity activity = getActivity();
                if (activity == null)
                {
                    Snackbar.make(getView(), "Cannot obtain activity...", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    return;
                }

                final Bundle bundle = new Bundle();
                bundle.putBoolean("notAlertDialog", true);
                bundle.putBoolean("notAlertDialog", true);

                final EditRecordFragment dialogFragment = new EditRecordFragment(instance,null);
                dialogFragment.setArguments(bundle);

                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                final Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                dialogFragment.show(ft, "dialog");
            }
        });

        setActivityTitle(getString(R.string.menu_trackrecords));
        changeToolbarImage(R.drawable.trackrecord_background);
        refreshResults();
        return binding.getRoot();
    }

    @androidx.annotation.Nullable
    private Context getActivityContext()
    {
        return getActivity() == null ? null : getActivity().getBaseContext();
    }

    void onSaveRecord()
    {
        DataRepository.get(getActivityContext()).saveRecords();

        refreshResults();

        final View view = getView();
        if (view != null)
            Snackbar.make(getView(), getText(R.string.tp_saved), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    void refreshResults()
    {
        @NotNull List<TrackRecord> records = DataRepository.get().getRecords();
        if (records.isEmpty())
            return;

        binding.trackRecordsList.removeAllViews();

        final FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        for (TrackRecord item : records)
            ft.add(R.id.track_records_list, new TropyEntryFragment(item));

        ft.commit();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    static interface IEditRecordCallback
    {
        public void onResult(boolean bSaved, @Nullable TrackRecord pData);
    }

}