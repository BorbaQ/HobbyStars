package com.example.hackheroes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineView extends View {

    public static class Node {
        public float x, y;
        public boolean active;

        public Node(float x, float y, boolean active) {
            this.x = x;
            this.y = y;
            this.active = active;
        }
    }

    private final Paint paint = new Paint();
    private final List<Node> nodes = new ArrayList<>();

    public LineView(Context ctx) {
        super(ctx);
        init();
    }

    public LineView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        init();
    }

    private void init() {
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(6f);
        paint.setAntiAlias(true);
    }

    public void setNodes(List<Node> points) {
        nodes.clear();
        nodes.addAll(points);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < nodes.size() - 1; i++) {
            Node a = nodes.get(i);
            Node b = nodes.get(i + 1);

            if (!a.active || !b.active)
                break;
            canvas.drawLine(a.x, a.y, b.x, b.y, paint);
        }
    }
}
