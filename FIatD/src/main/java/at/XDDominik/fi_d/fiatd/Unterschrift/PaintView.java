package at.XDDominik.fi_d.fiatd.Unterschrift;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaintView extends View implements View.OnTouchListener {
    private static final String TAG = "PaintView";

    List<Point> points = new ArrayList<Point>();
    Paint paint;
    Path path;
    UnterschKunde context;
    String kname, kdatum,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKname() {
        return kname;
    }

    public void setKname(String kname) {
        this.kname = kname;
    }

    public String getKdatum() {
        return kdatum;
    }

    public void setKdatum(String kdatum) {
        this.kdatum = kdatum;
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(context instanceof UnterschKunde)
            this.context = (UnterschKunde)context;
        else
            Toast.makeText(context, "No Main", Toast.LENGTH_SHORT).show();
        cn();
    }
    public PaintView(UnterschKunde context) {
        super(context);
        this.context = context;
        cn();
    }
    public void cn(){
        setFocusable(true);
        setFocusableInTouchMode(true);
        path = new Path();
        this.setOnTouchListener(this);
        this.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);

        setupPaint();

        TableLayout tl1 = new TableLayout(context);
        TableRow r1 = new TableRow(context);

        Button ab = new Button(context);
        ab.setText("Abbrechen");
        ab.setTextSize(30);

        Button save = new Button(context);
        save.setText("Abbrechen");
        save.setTextSize(30);

        r1.addView(ab);
        r1.addView(save);

        tl1.addView(r1);
    }

    public void setupPaint(){
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(path,paint);
        for(int ii = 0 ; ii < points.size();ii++)
            canvas.drawCircle(points.get(ii).x,points.get(ii).y,1,paint);
    }

    public boolean onTouch(View view, MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if(false){
                //can.drawPath(path, paint);
                //path.reset();
                    Point point = new Point();
                    point.x = event.getX();
                    point.y = event.getY();
                    points.add(point);
                }
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }
    public void save(){
        //TODO Fehlt noch
        try {
            Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            this.draw(canvas);
            FileOutputStream fos = this.context.openFileOutput(type + "_"+kname+"_"+kdatum+",jpg", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clear(){
        points.clear();
        path = new Path();
        setupPaint();
        invalidate();
    }
}

class Point {
    float x, y;

    @Override
    public String toString() {
        return x + ", " + y;
    }
}