package net.wecodelicious.automazo.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Bhaskar on 29/08/02016.
 */
public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private OnItemClickListener mListener;

    public interface OnItemClickListener
    {
        void onItemClicked(View view, int position);
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        this.mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    GestureDetector mGestureDetector ;



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClicked(childView,rv.getChildAdapterPosition(childView));
            }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

