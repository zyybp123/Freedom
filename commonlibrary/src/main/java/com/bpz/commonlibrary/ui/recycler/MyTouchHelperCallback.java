package com.bpz.commonlibrary.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;

import com.bpz.commonlibrary.util.LogUtil;


public class MyTouchHelperCallback extends ItemTouchHelper.Callback {
    private TouchListener listener;
    private boolean canMove;
    private boolean canSwipe;


    public MyTouchHelperCallback(TouchListener listener, boolean canMove, boolean canSwipe) {
        this.canMove = canMove;
        this.canSwipe = canSwipe;
        this.listener = listener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
                ItemTouchHelper.START | ItemTouchHelper.END);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            //条目类型不一致，不予交换
            return false;
        }
        //得到拖动ViewHolder的position
        int fromPosition = viewHolder.getAdapterPosition();
        //得到目标ViewHolder的position
        int toPosition = target.getAdapterPosition();
        return null != listener && listener.onMove(fromPosition, toPosition);
    }

    /**
     * 是否支持长按拖拽，默认为true，表示支持长按拖拽
     * 对应长按移动位置功能
     * 也可以返回false，手动调用startDrag()方法启动拖拽
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return canMove;
    }

    /**
     * 是否支持任意位置触摸事件发生时启用滑动操作，默认为true，表示支持滑动
     * 对应滑动删除功能
     * 也可以返回false，手动调用startSwipe()方法启动滑动
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return canSwipe;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (listener != null) {
            listener.onSwipe(position);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        switch (actionState) {
            case MotionEvent.ACTION_DOWN:
                if (listener != null) {
                    listener.onTouchFinish(viewHolder, actionState);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e("移动");
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e("抬起");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtil.e("取消");
                break;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /*@Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动时改变Item的透明度
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
    }*/
}
