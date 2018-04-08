package com.example.husain.assignment3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by husain on 2/25/2018.
 */

public class CircleView extends View {

    Paint paint;
    boolean running = true;
    private boolean swipeInProgress = false;
    private List<Circle> circleList = new ArrayList<>();
    private VelocityTracker velocityTracker;
    private float xCenter, yCenter;
    private float xVelocity, yVelocity;
    private float canvasHeight,canvasWidth;

    public CircleView(Context context) {
        super(context);
         }

    public CircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
        Iterator<Circle> circleListIterator = circleList.iterator();
        while (circleListIterator.hasNext()) {
            Circle circle = circleListIterator.next();
            xCenter = circle.getxStart();
            yCenter = circle.getyStart();
            canvas.drawCircle(circle.getxStart(), circle.getyStart(), circle.getRadius(), paint);
        }
    }

    private boolean handleActionUp(MotionEvent event){
        if (!swipeInProgress) return false;
        running = false;
        invalidate();
        return true;
    }

    private boolean handleActionDown(MotionEvent event) {
        velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        swipeInProgress = true;
        if(circleList.size() >= 15) return true;
        final Circle circle = new Circle(0,0,0);
        circle.setxStart(event.getX());
        circle.setyStart(event.getY());
        new Thread(new Runnable() {
            public void run() {
                while(running){
                    try {
                        Thread.sleep(75);
                        circle.setRadius(circle.getRadius() + 10);
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        circleList.add(circle);
        invalidate();
        return true;
    }

    private boolean handleActionMove (MotionEvent event)
    {
        velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(10);

        Iterator<Circle> circleListIterator = circleList.iterator();
        while (circleListIterator.hasNext()) {
            final Circle circleTmp = circleListIterator.next();
            float distance = (float) (Math.sqrt(Math.pow((circleTmp.getxStart() - event.getX()), 2)
                    + Math.pow((circleTmp.getyStart() - event.getY()), 2)));
            if(distance < circleTmp.getRadius()){
                new Thread(new Runnable() {
                    float xVelocity = velocityTracker.getXVelocity();
                    float yVelocity = velocityTracker.getYVelocity();
                    public void run() {
                        while(!xLimit(circleTmp) && !yLimit(circleTmp)){
                            try {
                                Thread.sleep(50);
                                circleTmp.setxStart(circleTmp.getxStart() + xVelocity);
                                circleTmp.setyStart(circleTmp.getyStart() + yVelocity);
                                postInvalidate();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        }
        invalidate();
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        float x = event.getX();
        float y = event.getY();
        switch (actionCode) {
            case MotionEvent.ACTION_DOWN:
                running = true;
                return handleActionDown(event);
            case MotionEvent.ACTION_MOVE:
                return handleActionMove(event);
            case MotionEvent.ACTION_UP:
                return handleActionUp(event);
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                velocityTracker.clear();
                swipeInProgress = false;
                return false;
        }
        return false;
    }

    private boolean xLimit(Circle circle)
    {
        float x = circle.getxStart();
        if (x - circle.getRadius() <= 0) {
            return true;
        }
        if (x + circle.getRadius() >= canvasWidth)
        {
            return true;
        }
        return false;
    }

    private boolean yLimit(Circle circle)
    {
        float y = circle.getyStart();
        if (y - circle.getRadius() <= 0) {
            return true;
        }
        if (y + circle.getRadius() >= canvasHeight){
            return true;
        }
        return false;
    }

}
