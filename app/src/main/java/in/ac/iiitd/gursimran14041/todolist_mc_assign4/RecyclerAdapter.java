package in.ac.iiitd.gursimran14041.todolist_mc_assign4;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Gursimran Singh on 09-11-2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ListManager mListManger;

    public RecyclerAdapter(Context context) {
        this.mListManger = ListManager.getListManagerInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ToDoTask task = mListManger.getTask(position);
        holder.title.setText(task.getTitle());
    }

    @Override
    public int getItemCount() {
        return mListManger.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
        }
    }
}

class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mClickListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
        mClickListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mClickListener != null) {
                    mClickListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mClickListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }
}
