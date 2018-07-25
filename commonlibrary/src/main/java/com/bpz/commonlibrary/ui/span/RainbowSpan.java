package com.bpz.commonlibrary.ui.span;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import com.bpz.commonlibrary.R;

import org.jetbrains.annotations.NotNull;

/**
 * 渐变颜色的文字
 */
public class RainbowSpan extends CharacterStyle implements UpdateAppearance {
    private final int[] colors;

    public RainbowSpan(@NotNull Context context) {
        colors = new int[]{};//context.getResources().getIntArray(R.array.rainbow);
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.setStyle(Paint.Style.FILL);
        Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors, null,
                Shader.TileMode.MIRROR);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
    }
}
