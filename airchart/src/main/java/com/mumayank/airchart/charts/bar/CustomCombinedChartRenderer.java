package com.mumayank.airchart.charts.bar;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.renderer.CombinedChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class CustomCombinedChartRenderer extends CombinedChartRenderer {

    public CustomCombinedChartRenderer(CombinedChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        mValuePaint.setColor(color);
        c.save();
        if (Float.parseFloat(valueText) > 0) {
            int length = 0;
            for (int i = 0; i < valueText.length(); i++) {
                char character = valueText.charAt(i);
                if (character == '.') {
                    continue;
                }
                length++;
            }
            int space = 5 * length;
            c.rotate(-90f, x + 10, y - space);
            c.drawText(valueText, x + 20, y - space, mValuePaint);
        } else {
            c.rotate(-90f, x + 10, y);
            c.drawText(valueText, x - (valueText.length() * 8), y, mValuePaint);
        }
        c.restore();
    }
}
