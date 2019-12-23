package dhht.android.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AndroidXJ on 2019/12/19.
 */
public class Clock extends View {
    private Context mContext;
    private Paint mPaint;
    private Timer mTimer;
    private float mSecondDegree = 0;
    private float mMinDegree = 0;
    private float mHourDegree = 0;


    public Clock(Context context) {
        super(context);
        this.mContext = context;
    }

    public Clock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public void start() {
        Calendar c = Calendar.getInstance();//
        int hour = c.get(Calendar.HOUR_OF_DAY);//时
        int min = c.get(Calendar.MINUTE);//分
        int sec = c.get(Calendar.SECOND);//分
        mHourDegree = hour * 360 / 12;
        mMinDegree = min * 360 / 60;
        mSecondDegree = sec * 360 / 60;
//        Log.d(MainActivity.TAG, "时针=====" + hour);
//        Log.d(MainActivity.TAG, "分针=====" + min);
//        Log.d(MainActivity.TAG, "秒针=====" + sec);
//        Log.d(MainActivity.TAG, "时针角度=====" + mHourDegree);
//        Log.d(MainActivity.TAG, "分针角度=====" + mMinDegree);
//        Log.d(MainActivity.TAG, "秒针角度=====" + mSecondDegree);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mSecondDegree == 360) {
                    mSecondDegree = 0;
                }
                if (mMinDegree == 360) {
                    mMinDegree = 0;
                }
                if (mHourDegree == 360) {
                    mHourDegree = 0;
                }
                mSecondDegree = mSecondDegree + 6;//秒针
                mMinDegree = mMinDegree + 0.1f;//分针
                mHourDegree = mHourDegree + 1.0f / 240;//时针
                postInvalidate();
            }
        }, 1 * 1000, 1 * 1000);
    }


    private void initPaint() {
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //将坐标原点移到圆心处
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //画表盘
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(0, 0, getHeight() / 3, mPaint);
        //画圆心
        mPaint.setStrokeWidth(5);
        canvas.drawPoint(0, 0, mPaint);
        //设置苦度线宽度
        mPaint.setStrokeWidth(1);

        mPaint.setTextSize(25);
        canvas.save();
        for (int i = 0; i < 360; i++) {
            if (i % 30 == 0) {//长刻度
                mPaint.setStrokeWidth(3);
                mPaint.setColor(Color.BLUE);
                canvas.drawLine(getHeight() / 3 - 25, 0, getHeight() / 3, 0, mPaint);
            } else if (i % 6 == 0) {//中刻度
                mPaint.setStrokeWidth(2);
                mPaint.setColor(Color.BLACK);
                canvas.drawLine(getHeight() / 3 - 18, 0, getHeight() / 3, 0, mPaint);
            } else {
                mPaint.setStrokeWidth(1);
                mPaint.setColor(Color.BLACK);
                canvas.drawLine(getHeight() / 3 - 10, 0, getHeight() / 3, 0, mPaint);
            }
            canvas.rotate(1);
        }
        canvas.restore();

        canvas.save();
        for (int i = 0; i < 12; i++) {
            if (i == 0) {
                drawNum(canvas, 0, 12 + "", mPaint);
            } else {
                drawNum(canvas, 30, i + "", mPaint);
            }
        }
        canvas.restore();

        //画指针
        //秒针
        canvas.save();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        canvas.rotate(mSecondDegree);
        canvas.drawLine(0, 0, 0, -120, mPaint);
        canvas.restore();
        //分针
        canvas.save();
        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(3);
        canvas.rotate(mMinDegree);
        canvas.drawLine(0, 0, 0, -70, mPaint);
        canvas.restore();
        //时针
        canvas.save();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(5);
        canvas.rotate(mHourDegree);
        canvas.drawLine(0, 0, 0, -50, mPaint);
        canvas.restore();
    }

    /**
     * 数字
     *
     * @param canvas
     * @param degree
     * @param text
     * @param paint
     */
    private void drawNum(Canvas canvas, int degree, String text, Paint paint) {
        canvas.rotate(degree);
        canvas.drawText(text, -10, 50 - getHeight() / 3, mPaint);

    }
}
