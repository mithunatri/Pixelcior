package com.andruids.photofilter.pixelcior;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by rachna on 10/4/16.
 */
public class ShadingFilterActivity extends Activity {

    private Bitmap bmIn;
    private Bitmap bmOut;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter_main);

        Intent intent = getIntent();

        int drawableId = Integer.parseInt(intent.getStringExtra("DRAWABLE_ID"));
        int imageId = Integer.parseInt(intent.getStringExtra("IMAGE_RESOURCE_ID"));

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        bmIn = BitmapFactory.decodeResource(getResources(), drawableId, options);
        bmOut = BitmapFactory.decodeResource(getResources(), drawableId, options);

        Log.i("ShadingFilterActivity: ", "Set bmIn and bmOut done");

        ImageView in = (ImageView) findViewById(imageId);
        in.setImageBitmap(bmIn);

        ImageView out = (ImageView) findViewById(R.id.filterOut);

        Log.i("ShadingFilterActivity: ", "Image View set");

        applyShadingFilter(bmIn, Color.MAGENTA);
        out.setImageBitmap(bmOut);
        out.invalidate();
    }

    public Bitmap applyShadingFilter(Bitmap original, int shadingColor) {

        Bitmap bitmap = Bitmap.createBitmap(original.getWidth(),
                original.getHeight(), Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(this);

        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(shadingColor, 0));
        canvas.drawBitmap(original, 0, 0, paint);

        allocOut.copyTo(bmOut);
        rs.destroy();

        return bitmap;
    }
}
