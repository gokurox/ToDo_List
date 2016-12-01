package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {
    private TextView mUUID;
    private TextView mTitle;
    private TextView mTimestamp;
    private TextView mDescription;

    private int mPosition;
    private ListManager mListManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);

        mUUID = (TextView) view.findViewById(R.id.uuid);
        mTitle = (TextView) view.findViewById(R.id.title);
        mTimestamp = (TextView) view.findViewById(R.id.timestamp);
        mDescription = (TextView) view.findViewById(R.id.description);

        Bundle args = getArguments();
        assert (args != null);

        mPosition = args.getInt(LocalTags.ITEM_POSITION);
        mListManager = ListManager.getListManagerInstance(getActivity().getApplicationContext());
        ToDoTask task = mListManager.getTask(mPosition);

        mUUID.setText(task.getUuid().toString());
        mTitle.setText(task.getTitle());
        mTimestamp.setText(task.getChronology());
        mDescription.setText(task.getDescription());

        return view;
    }
}
