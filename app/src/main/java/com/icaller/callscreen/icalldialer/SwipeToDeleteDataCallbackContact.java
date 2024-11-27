package com.icaller.callscreen.icalldialer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public abstract class SwipeToDeleteDataCallbackContact extends ItemTouchHelper.SimpleCallback {
    public static  int BUTTON_WIDTH = 140;
    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Iterator it = SwipeToDeleteDataCallbackContact.this.buttons.iterator();
            while (it.hasNext() && !((UnderlayButton) it.next()).onClick(motionEvent.getX(), motionEvent.getY())) {
            }
            return true;
        }
    };
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override 
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (SwipeToDeleteDataCallbackContact.this.swipedPos < 0) {
                return false;
            }
            Point point = new Point((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
            View view2 = SwipeToDeleteDataCallbackContact.this.recyclerView.findViewHolderForAdapterPosition(SwipeToDeleteDataCallbackContact.this.swipedPos).itemView;
            Rect rect = new Rect();
            view2.getGlobalVisibleRect(rect);
            if (motionEvent.getAction() == 0 || motionEvent.getAction() == 1 || motionEvent.getAction() == 2) {
                if (rect.top >= point.y || rect.bottom <= point.y) {
                    SwipeToDeleteDataCallbackContact.this.recoverQueue.add(Integer.valueOf(SwipeToDeleteDataCallbackContact.this.swipedPos));
                    SwipeToDeleteDataCallbackContact.this.swipedPos = -1;
                    SwipeToDeleteDataCallbackContact.this.recoverSwipedItem();
                } else {
                    SwipeToDeleteDataCallbackContact.this.gestureDetector.onTouchEvent(motionEvent);
                }
            }
            return false;
        }
    };
    private List<UnderlayButton> buttons = new ArrayList();
    private Map<Integer, List<UnderlayButton>> buttonsBuffer = new HashMap();
    private Queue<Integer> recoverQueue = new LinkedList<Integer>() {
        public boolean add(Integer num) {
            if (contains(num)) {
                return false;
            }
            return super.add(num);
        }
    };

    
    public interface UnderlayButtonClickListener {
        void onClick(int i);
    }

    @Override
    public float getSwipeEscapeVelocity(float f) {
        return f * 0.1f;
    }

    @Override 
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return this.swipeThreshold;
    }

    @Override 
    public float getSwipeVelocityThreshold(float f) {
        return f * 5.0f;
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> list);

    @Override 
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        return false;
    }

    public SwipeToDeleteDataCallbackContact(Context context, RecyclerView recyclerView) {
        super(0, 4);
        this.recyclerView = recyclerView;
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        this.recyclerView.setOnTouchListener(this.onTouchListener);
        attachSwipe();
    }

    @Override 
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        int adapterPosition = viewHolder.getAdapterPosition();
        int i2 = this.swipedPos;
        if (i2 != adapterPosition) {
            this.recoverQueue.add(Integer.valueOf(i2));
        }
        this.swipedPos = adapterPosition;
        if (this.buttonsBuffer.containsKey(Integer.valueOf(adapterPosition))) {
            this.buttons = this.buttonsBuffer.get(Integer.valueOf(this.swipedPos));
        } else {
            this.buttons.clear();
        }
        this.buttonsBuffer.clear();
        this.swipeThreshold = this.buttons.size() * 0.5f * 140.0f;
        recoverSwipedItem();
    }

    @Override 
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        float f3;
        int adapterPosition = viewHolder.getAdapterPosition();
        View view = viewHolder.itemView;
        if (adapterPosition < 0) {
            this.swipedPos = adapterPosition;
            return;
        }
        if (i != 1 || f >= 0.0f) {
            f3 = f;
        } else {
            List<UnderlayButton> arrayList = new ArrayList<>();
            if (!this.buttonsBuffer.containsKey(Integer.valueOf(adapterPosition))) {
                instantiateUnderlayButton(viewHolder, arrayList);
                this.buttonsBuffer.put(Integer.valueOf(adapterPosition), arrayList);
            } else {
                arrayList = this.buttonsBuffer.get(Integer.valueOf(adapterPosition));
            }
            List<UnderlayButton> list = arrayList;
            float size = ((list.size() * f) * 140.0f) / view.getWidth();
            drawButtons(canvas, view, list, adapterPosition, size);
            f3 = size;
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, f3, f2, i, z);
    }

    public synchronized void recoverSwipedItem() {
        while (!this.recoverQueue.isEmpty()) {
            int intValue = this.recoverQueue.poll().intValue();
            if (intValue > -1) {
                this.recyclerView.getAdapter().notifyItemChanged(intValue);
            }
        }
    }

    private void drawButtons(Canvas canvas, View view, List<UnderlayButton> list, int i, float f) {
        float right = view.getRight();
        float size = (f * (-1.0f)) / list.size();
        for (UnderlayButton underlayButton : list) {
            float f2 = right - size;
            underlayButton.onDraw(canvas, new RectF(f2, view.getTop(), right, view.getBottom()), i);
            right = f2;
        }
    }

    public void attachSwipe() {
        new ItemTouchHelper(this).attachToRecyclerView(this.recyclerView);
    }

    
    public static class UnderlayButton {
        private UnderlayButtonClickListener clickListener;
        private RectF clickRegion;
        private int color;
        private Context context;
        private int imageResId;
        private int pos;
        private String text;

        public UnderlayButton(Context context, String str, int i, int i2, UnderlayButtonClickListener underlayButtonClickListener) {
            this.text = str;
            this.imageResId = i;
            this.color = i2;
            this.clickListener = underlayButtonClickListener;
            this.context = context;
        }

        public boolean onClick(float f, float f2) {
            RectF rectF = this.clickRegion;
            if (rectF == null || !rectF.contains(f, f2)) {
                return false;
            }
            this.clickListener.onClick(this.pos);
            return true;
        }

        public void onDraw(Canvas canvas, RectF rectF, int i) {
            Paint paint = new Paint();
            paint.setColor(this.color);
            canvas.drawRect(rectF, paint);
            paint.setColor(-1);
            Rect rect = new Rect();
            float height = rectF.height();
            float width = rectF.width();
            paint.setTextAlign(Paint.Align.LEFT);
            String str = this.text;
            paint.getTextBounds(str, 0, str.length(), rect);
            canvas.drawText(this.text, rectF.left + (((width / 2.0f) - (rect.width() / 2.0f)) - rect.left), rectF.top + (((height / 2.0f) + (rect.height() / 2.0f)) - rect.bottom), paint);
            if (this.imageResId != 0) {
                paint.measureText(this.text);
                Bitmap drawableToBitmap = SwipeToDeleteDataCallbackContact.drawableToBitmap(ContextCompat.getDrawable(this.context, this.imageResId));
                canvas.drawBitmap(drawableToBitmap, (rectF.centerX() - (drawableToBitmap.getWidth() / 2.0f)) + 10.0f, (rectF.centerY() - (drawableToBitmap.getHeight() / 2.0f)) + 10.0f, (Paint) null);
            }
            this.clickRegion = rectF;
            this.pos = i;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
