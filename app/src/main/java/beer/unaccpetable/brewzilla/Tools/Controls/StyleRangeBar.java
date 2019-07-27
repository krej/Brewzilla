package beer.unaccpetable.brewzilla.Tools.Controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import beer.unaccpetable.brewzilla.R;

public class StyleRangeBar extends View {
    private Paint m_p;
    private RectF m_r;
    private Rect m_RectSquare;
    private TextPaint m_tp;

    private int m_iPositiveColor;
    private int m_iNegativeColor;
    private int m_iTextColor;
    private int m_iCornerRoundX, m_iCornerRoundY;
    private int m_iRangeCircleColor;
    private float m_fBarMin, m_fBarMax; //start and end of bar

    private float m_fRangeMin, m_fRangeMax; //start and end of range (where the red meets green)

    private boolean m_bInvalidate = true;

    private float m_fValue;
    private int m_iValueColor;

    public StyleRangeBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        m_p = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_r = new RectF();
        m_RectSquare = new Rect();
        m_tp = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.StyleRangeBar, 0, 0);
        try {
            m_iPositiveColor = a.getInteger(R.styleable.StyleRangeBar_positiveColor, Color.GREEN);
            m_iNegativeColor = a.getInteger(R.styleable.StyleRangeBar_negativeColor, Color.RED);
            m_iTextColor = a.getInteger(R.styleable.StyleRangeBar_textColor, Color.BLACK);
            m_iCornerRoundX = a.getInteger(R.styleable.StyleRangeBar_cornerRoundX, 20);
            m_iCornerRoundY = a.getInteger(R.styleable.StyleRangeBar_cornerRoundY, 20);
            m_iRangeCircleColor = a.getInteger(R.styleable.StyleRangeBar_rangeCircleColor, Color.BLACK);
            m_fBarMax = a.getFloat(R.styleable.StyleRangeBar_barMax, 100f);
            m_fBarMin = a.getFloat(R.styleable.StyleRangeBar_barMin, 0f);
            m_iValueColor = a.getInteger(R.styleable.StyleRangeBar_valueColor, Color.parseColor("#ffa500"));
        } finally {
            a.recycle();
        }

    }

    @Override
    protected void onDraw(Canvas c) {
        float w = (float)getMeasuredWidth();
        int h = getMeasuredHeight();

        int iBarTop = 35;
        int iBarBottom = 10;

        float fBarLength = (m_fBarMax - m_fBarMin);
        float xMinPos = (m_fRangeMin / fBarLength) * w;
        float xMaxPos = (m_fRangeMax / fBarLength) * w;
        float xValuePos = (m_fValue / fBarLength) * w;

        m_tp.setColor(m_iTextColor);

        //Draw out of range background
        m_r.set(0, iBarTop, w - 10, iBarBottom);
        m_p.setColor(m_iNegativeColor);
        m_p.setShadowLayer(10, 5, 5, Color.BLACK);
        m_p.setAlpha(100);
        c.drawRoundRect(m_r, m_iCornerRoundX, m_iCornerRoundY, m_p);
        m_p.setShadowLayer(0, 0, 0, Color.WHITE);
        m_p.setAlpha(255);

        //Draw in range background
        m_p.setColor(m_iPositiveColor);
        m_r.set(xMinPos, iBarTop, xMaxPos, iBarBottom);
        m_RectSquare.set((int)xMinPos, iBarTop, (int)xMaxPos, iBarBottom);
        //m_p.setAlpha(200);
        //c.drawRect(m_RectSquare, m_p);
        m_p.setAlpha(255);
        m_p.setShadowLayer(5, 1, 1, Color.BLACK);
        c.drawRoundRect(m_r, m_iCornerRoundX, m_iCornerRoundY, m_p);
        m_p.setShadowLayer(0, 0, 0, Color.WHITE);

        //draw min circle
        /*float iX = xMinPos;
        if (iX < getX())
            iX += 20;
        m_p.setColor(m_iRangeCircleColor);
        c.drawCircle(iX, 20, 20, m_p);
        m_p.setColor(Color.WHITE);
        m_p.setTextSize(25);
        c.drawText("45", iX - 14, 27, m_p);
        */

        m_p.setColor(Color.BLACK);
        m_p.setTextSize(25);
        c.save();
        //c.rotate(45);
        m_p.setShadowLayer(5, 2, 2, Color.WHITE);
        c.drawText("45", xMinPos - 35, 32, m_p);
        c.restore();

        //draw max circle
        //c.drawCircle(xMaxPos, 20, 20, m_p);
        c.drawText("70", xMaxPos + 5, 32, m_p);

        //draw value box
        m_r.set(xValuePos, iBarTop + 10, xValuePos + 15, iBarBottom - 10);
        m_p.setColor(m_iValueColor);
        m_p.setShadowLayer(10, 5, 5, Color.BLACK);
        c.drawRoundRect(m_r, m_iCornerRoundX, m_iCornerRoundY, m_p);
        m_p.setShadowLayer(0, 0, 0, Color.WHITE);

        //draw label
        m_tp.setTextSize(30);
        //c.drawText(m_sLabel, 0, 20, m_tp);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, 70);
    }

    public void setRangeMin(float min) {
        m_fRangeMin = min;
        if (m_bInvalidate) invalidate();
    }

    public void setRangeMin(double min) {
        setRangeMin((float)min);
    }

    public void setRangeMax(float max) {
        m_fRangeMax = max;
        if (m_bInvalidate) invalidate();
    }

    public void setRangeMax(double max) {
        setRangeMax((float)max);
    }

    public void setRange(double min, double max) {
        m_bInvalidate = false; //only redraw once
        setRangeMin(min);
        m_bInvalidate = true;
        setRangeMax(max);
    }

    public void setValue(double value) {
        m_fValue = (float)value;
        invalidate();
    }

}
