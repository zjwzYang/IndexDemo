package com.example.yangjie.indexdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * recyclerview的索引。
 *
 * @author yangjie 2017/12/5 新建.
 */
public class QuickIndexBar extends View {

    private List<String> letters = new ArrayList<>();
    private Paint paint;
    private Context context;
    private int mCellWidth;
    private int mCellHeight;
    private float mTextHeight;
    private int currentIndex  =-1;
    private OnLetterChangeListener onLetterChangeListener;

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    public void setLetters(String[] letters) {
        this.letters = Arrays.asList(letters);

        //重新绘制
        invalidate();
    }

    public void setCurrentIndex(String letter) {
        if (letter == null) {
            return;
        }
        for (int i = 0; i < letters.size(); i++) {
            if (letter.equals(letters.get(i))) {
                currentIndex = i;
            }
        }

        //重新绘制
        invalidate();
    }

    public interface OnLetterChangeListener{
        void onLetterChange(String letter);
        //手指抬起
        void onReset();
    }

    public QuickIndexBar(Context context) {
        this(context,null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setTextSize(dip2px(12));
        //消除锯齿
        paint.setAntiAlias(true);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        mTextHeight = (float) Math.ceil( fontMetrics.descent - fontMetrics.ascent);  //1.1---2   2.1--3

    }

    /**dp转px**/
    public static int dip2px(int dipValue) {
        float reSize = Resources.getSystem().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (letters.size() != 0) {
            mCellWidth = getMeasuredWidth();
            mCellHeight = getMeasuredHeight()/letters.size();
        }
    }

    /**
     * 绘制控件
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 字.画字();
        for (int i = 0; i < letters.size(); i++) {
            String letter = letters.get(i);
            float mTextWidth = paint.measureText(letter);
            float x =(mCellWidth- mTextWidth)*0.8f;
            float y = (mCellHeight + mTextHeight) *0.5f+mCellHeight*i;

            if (i == currentIndex){
                paint.setColor(Color.BLACK);
            }else{
                paint.setColor(Color.BLUE);
            }

            canvas.drawText(letter, x,y,paint);
        }
    }


    /**
     * 处理 按下 移动 手指抬起
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (letters.size() == 0) {
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int downY = (int) event.getY();

                //获取当前索引
                currentIndex = downY/mCellHeight;
                if (currentIndex<0||currentIndex>letters.size()-1){

                }else{
                    if (onLetterChangeListener != null){
                        onLetterChangeListener.onLetterChange(letters.get(currentIndex));
                    }
                }
                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) event.getY();
                //获取当前索引
                currentIndex = moveY/mCellHeight;
                if (currentIndex<0||currentIndex>letters.size()-1){

                }else{
                    if (onLetterChangeListener != null){
                        onLetterChangeListener.onLetterChange(letters.get(currentIndex));
                    }
                }

                //重新绘制
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                //表示手指抬起了
                if(onLetterChangeListener!= null){
                    onLetterChangeListener.onReset();
                }
                break;
        }

        return true;
    }
}
