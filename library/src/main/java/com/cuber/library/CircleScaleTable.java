package com.cuber.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.ArrayList;

public class CircleScaleTable extends View implements OnTouchListener {

    private float r1, r2;
    private float totalValue = 60;
    private float totalDegree = 360;
    private float UnitDegree;
    private float rotationAngle = 0;
    private float scale_range_degree=15;
    private float point_length = 50;
    private float centerX, centerY;
    private float cur_degree = 0;
    private int longCalibration = 5;
    private float cur_quadrant = 1;
    private float last_quadrant = 1;
    private int count = 1, last_value;

    private ArrayList<Point> points;
    private Paint p_dark, p_light, p_line;
    private OnValueChangeListener mListener;

    public interface OnValueChangeListener {

        public void onValueChange(int value);
    }

    public CircleScaleTable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mListener = listener;
    }

    public void setParameter(float r1, float totalDegree, int totalValue, int longCalibration, float rotationAngle) {
        this.r1 = r1;
        r2 = r1 + point_length;
        if (totalDegree > 360) {
            this.totalDegree = 360;
        } else if (totalDegree < 0) {
            this.totalDegree = 0;
        } else {
            this.totalDegree = totalDegree;
        }

        if (totalDegree > 360) {
            this.rotationAngle = 360;
        } else if (totalDegree < 0) {
            this.rotationAngle = 0;
        } else {
            this.rotationAngle = rotationAngle;
        }

        this.longCalibration = longCalibration;
        this.totalValue = totalValue;
        init();
    }

    public void setPoint_length(float point_length) {
        this.point_length = point_length;
    }

    public void setPaint_dark(int a, int r, int g, int b, int strokeWidth) {
        p_dark.setARGB(a, r, g, b);
        p_dark.setStrokeWidth(strokeWidth);
    }

    public void setPaint_light(int a, int r, int g, int b, int strokeWidth) {
        p_light.setARGB(a, r, g, b);
        p_light.setStrokeWidth(strokeWidth);
    }

    public void setPaint_arc(int a, int r, int g, int b, int strokeWidth) {
        p_line.setARGB(a, r, g, b);
        p_line.setStrokeWidth(strokeWidth);
    }

    private void setPaint() {
        p_dark = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                this.setStyle(Style.STROKE);
                this.setARGB(255, 200, 200, 200);
                this.setStrokeWidth(5);
            }
        };

        p_light = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                this.setStyle(Style.STROKE);
                this.setARGB(255, 0, 0, 0);
                this.setStrokeWidth(5);
            }
        };
        p_line = new Paint(Paint.ANTI_ALIAS_FLAG) {
            {
                this.setStyle(Style.STROKE);
                this.setARGB(255, 0, 0, 0);
                this.setStrokeWidth(5);
            }
        };

    }

    @SuppressLint("WrongCall")
    public void init() {
        this.UnitDegree = totalDegree / totalValue;
        // this.scale_range_degree = UnitDegree * 5;
        this.setOnTouchListener(this);
        getPoints();
        setPaint();
        invalidate();
    }

    private void getPoints() {
        points = new ArrayList<>();

        float temp_degree = rotationAngle;

        for (int i = 0; i < totalValue + 1; i++) {
            Point p = new Point();

            if (temp_degree >= 360) {
                p.setdegree(temp_degree -= 360);
            } else {
                p.setdegree(temp_degree);
            }

            if (i % longCalibration == 0) {
                p.setx1y1((float) (r1 * Math.cos(temp_degree * Math.PI / 180) * 0.8f), (float) (r1 * Math.sin(temp_degree * Math.PI / 180)) * 0.8f);
                p.setx2y2((float) (r2 * Math.cos(temp_degree * Math.PI / 180) * 1.1f), (float) (r2 * Math.sin(temp_degree * Math.PI / 180)) * 1.1f);
            } else {

                p.setx1y1((float) (r1 * Math.cos(temp_degree * Math.PI / 180)), (float) (r1 * Math.sin(temp_degree * Math.PI / 180)));
                p.setx2y2((float) (r2 * Math.cos(temp_degree * Math.PI / 180)), (float) (r2 * Math.sin(temp_degree * Math.PI / 180)));
            }

            p.set_no(i);
            points.add(p);
            temp_degree += this.UnitDegree;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX() - centerX;
        float y = event.getY() - centerY;
        float temp_degree = (float) (Math.atan(y / x) * 180.0 / Math.PI);

        //( + , + )
        if (x >= 0 && y >= 0) {
            temp_degree = (float) (Math.atan(y / x) * 180.0 / Math.PI) - rotationAngle;
        }
        //( - , + )
        if (x < 0 && y >= 0) {
            temp_degree = (float) (Math.atan(y / x) * 180.0 / Math.PI) + 180 - rotationAngle;
        }
        //( - , - )
        if (x < 0 && y < 0) {
            temp_degree = (float) (Math.atan(y / x) * 180.0 / Math.PI) + 180 - rotationAngle;
        }
        //( + , - )
        if (x >= 0 && y < 0) {
            temp_degree = (float) (Math.atan(y / x) * 180.0 / Math.PI) + 360 - rotationAngle;
        }

        if (temp_degree < 0) {
            temp_degree += 360;
        }
        // Log.i("DEBUG", "temp_degree:" + temp_degree);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //quadrant 1
                if (0 <= temp_degree && temp_degree < totalDegree / 4 * 1) {
                    cur_degree = temp_degree;
                    cur_quadrant = last_quadrant = count = 1;
                }

                //quadrant 2
                else if (totalDegree / 4 * 1 <= temp_degree && temp_degree < totalDegree / 4 * 2) {
                    cur_degree = temp_degree;
                    cur_quadrant = last_quadrant = count = 2;
                }

                //quadrant 3
                else if (totalDegree / 4 * 2 <= temp_degree && temp_degree < totalDegree / 4 * 3) {
                    cur_degree = temp_degree;
                    cur_quadrant = last_quadrant = count = 3;
                }

                //quadrant 4
                else if (totalDegree / 4 * 3 <= temp_degree && temp_degree < totalDegree) {
                    cur_degree = temp_degree;
                    cur_quadrant = last_quadrant = count = 4;
                }

                //out side
                else {
                    break;
                }
                calPoint();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:

                if (temp_degree > totalDegree)
                    temp_degree = totalDegree - 1;

                //quadrant 1
                if (0 <= temp_degree && temp_degree < totalDegree / 4 * 1) {
                    cur_quadrant = 1;

                    if (last_quadrant == 4 && count != 0) {
                        count = 5;
                        cur_degree = totalDegree - 1;
                    }
                }

                //quadrant 2
                else if (totalDegree / 4 * 1 <= temp_degree && temp_degree < totalDegree / 4 * 2) {
                    cur_quadrant = 2;
                }

                //quadrant 3
                else if (totalDegree / 4 * 2 <= temp_degree && temp_degree < totalDegree / 4 * 3) {
                    cur_quadrant = 3;
                }

                //quadrant 4
                else if (totalDegree / 4 * 3 <= temp_degree && temp_degree < totalDegree) {
                    cur_quadrant = 4;

                    if (last_quadrant == 1 && count != 5) {
                        count = 0;
                        cur_degree = 0;
                    }
                }

                //quadrant 0 or 5
                else {
                    if (last_quadrant == 1 && count != 5) {
                        cur_quadrant = 4;
                        count = 0;
                        cur_degree = 0;
                    }
                    if (last_quadrant == 4 && count != 0) {
                        cur_quadrant = 1;
                        count = 5;
                        cur_degree = totalDegree - 1;
                    }
                }

                if (count != 5 && count != 0) {
                    count += cur_quadrant - last_quadrant;
                }


                if (count == 0) {
                    if (last_quadrant == 4 && cur_quadrant == 1) {
                        cur_degree = temp_degree;
                        count = 1;
                    }
                } else if (count == 1) {
                    cur_degree = temp_degree;
                } else if (count == 2) {
                    cur_degree = temp_degree;
                } else if (count == 3) {
                    cur_degree = temp_degree;
                } else if (count == 4) {
                    cur_degree = temp_degree;
                } else if (count == 5) {
                    if (last_quadrant == 1 && cur_quadrant == 4) {
                        cur_degree = temp_degree;
                        count = 4;
                    }
                }

                last_quadrant = cur_quadrant;

                Log.i("DEBUG", "count:" + count + ", cur_degree:" + cur_degree);
                calPoint();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return true;
    }

    private void calPoint() {

        float point_degree;

        for (int i = 0; i < points.size(); i++) {

            point_degree = points.get(i).getDegree() - rotationAngle;

            if (point_degree < 0) {
                point_degree += 360;
            }
            if (cur_degree - scale_range_degree <= point_degree && point_degree < cur_degree + scale_range_degree) {
                float temp_x1 = points.get(i).getx1();
                float temp_y1 = points.get(i).gety1();
                float temp_x2 = points.get(i).getx2();
                float temp_y2 = points.get(i).gety2();

                float scale1;
                float scale2;

                if (point_degree <= cur_degree) {
                    scale1 = 1 + Math.abs(Math.abs(cur_degree - point_degree) - scale_range_degree) * 0.003f;
                    scale2 = 1 + Math.abs(Math.abs(cur_degree - point_degree) - scale_range_degree) * 0.01f;

                    points.get(i).setx1y1(temp_x1 * scale1, temp_y1 * scale1);
                    points.get(i).setx2y2(temp_x2 * scale2, temp_y2 * scale2);
                } else {
                    scale1 = 1 + Math.abs(Math.abs(cur_degree - point_degree) - scale_range_degree) * 0.003f;
                    scale2 = 1 + Math.abs(Math.abs(cur_degree - point_degree) - scale_range_degree) * 0.006f;

                    points.get(i).setx1y1(temp_x1 * scale1, temp_y1 * scale1);
                    points.get(i).setx2y2(temp_x2 * scale2, temp_y2 * scale2);
                }
            }

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = canvas.getClipBounds();
        centerX = rect.centerX();
        centerY = rect.centerY();

        for (int i = 0; i < points.size(); i++) {

            float temp_degree = points.get(i).getDegree() - rotationAngle;

            if (temp_degree < 0) {
                temp_degree += 360;
            }

            if (temp_degree > cur_degree + 1) {

                canvas.drawLine(points.get(i).getx1() + centerX, points.get(i).gety1() + centerY, points.get(i).getx2() + centerX, points.get(i).gety2() + centerY, p_dark);

            } else {
                canvas.drawLine(points.get(i).getx1() + centerX, points.get(i).gety1() + centerY, points.get(i).getx2() + centerX, points.get(i).gety2() + centerY, p_light);
            }

        }

        RectF rectF = new RectF((centerX - r1 + 15), (centerY - r1 + 15), (centerX + r1 - 15), (centerY + r1 - 15));

        canvas.drawArc(rectF, rotationAngle, cur_degree, false, p_line);
        getPoints();

        // callback
        int value = (int) (totalValue * cur_degree / totalDegree);
        if (last_value != value) {
            mListener.onValueChange(value);
            last_value = value;
        }

        super.onDraw(canvas);
    }


    public class Point {

        private float x1, y1;
        private float x2, y2;
        private float degree;
        private int _no;

        public Point() {

        }

        public void setx1y1(float x1, float y1) {
            this.x1 = x1;
            this.y1 = y1;
        }

        public void setx2y2(float x2, float y2) {
            this.x2 = x2;
            this.y2 = y2;
        }

        public void setdegree(float degree) {
            this.degree = degree;
        }

        public void set_no(int _no) {
            this._no = _no;
        }

        public float getx1() {
            return this.x1;
        }

        public float gety1() {
            return this.y1;
        }

        public float getx2() {
            return this.x2;
        }

        public float gety2() {
            return this.y2;
        }

        public float getDegree() {
            return this.degree;
        }

//        public int get_no() {
//            return this._no;
//        }

    }
}
