package beer.unaccpetable.brewzilla.Tools.Controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;

public class StyleSeekBar extends android.support.v7.widget.AppCompatSeekBar {

    TextPaint m_tp = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);

    public StyleSeekBar (Context context) {
        super(context);
    }

    public StyleSeekBar (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public StyleSeekBar (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int thumb_x = (int) (( (double)this.getProgress()/this.getMax() ) * (double)this.getWidth());
        float middle = (float) (this.getHeight());


        m_tp.setColor(Color.BLACK);
        m_tp.setTextSize(40);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        c.drawText(""+this.getProgress(), thumb_x, 40, m_tp);

        Paint p = new Paint();


        Rect r = new Rect();
        int y = 63;
        r.set(0, y, (int)(0.2f * getWidth()), y+5);
        //p.setColor(Color.parseColor("#ffa500"));
        p.setColor(Color.RED);
        c.drawRect(r, p);

        p.setColor(Color.parseColor("#32CD32"));
        r.set((int)(0.2f * getWidth()), y, (int)(0.8f * getWidth()), y + 5);
        c.drawRect(r, p);

//        p.setColor(Color.parseColor("#ffa500"));
        p.setColor(Color.RED);
        //m_tp.setTextSize(15);

        r.set((int)(0.8f * getWidth()), y, getWidth(), y + 5);
        c.drawRect(r, p);

        p.setColor(Color.BLUE);
        c.drawCircle(thumb_x - 200, 65, 10, p);
        m_tp.setTextSize(25);
        c.drawText("40", thumb_x - 200, 40, m_tp);

        p.setColor(Color.BLUE);
        c.drawCircle(thumb_x + 200, 65, 10, p);
        m_tp.setTextSize(25);
        c.drawText("70", thumb_x + 200, 40, m_tp);

        m_tp.setTextSize(40);
        c.drawText("IBUs", 0, 40, m_tp);
    }
}
