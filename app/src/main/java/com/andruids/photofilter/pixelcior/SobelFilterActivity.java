package com.andruids.photofilter.pixelcior;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.Type;
import android.util.Log;
import android.widget.ImageView;

import com.android.rs.sobel.ScriptC_sobel;

/**
 * Created by sunny on 10/3/16.
 */
public class SobelFilterActivity extends Activity {

    private static final String TAG = "SobelFilterActivity";

    private RenderScript renderScript;
    private Allocation inAllocation;
    private Allocation temp;
    private Allocation outAllocation;
    private ScriptC_sobel  script;

    private Bitmap bmIn;
    private Bitmap bmOut;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter_main);

        Intent intent = getIntent();

        int drawableId = Integer.parseInt(intent.getStringExtra("DRAWABLE_ID"));
        int imageId = Integer.parseInt(intent.getStringExtra("IMAGE_RESOURCE_ID"));

        renderScript = RenderScript.create(this);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        bmIn = BitmapFactory.decodeResource(getResources(), drawableId, options);
        bmOut = BitmapFactory.decodeResource(getResources(), drawableId, options);

        //bmIn = toGrayscale(bmIn);

        Log.i(TAG, "Set bmIn and bmOut done");

        ImageView in = (ImageView) findViewById(imageId);
        in.setImageBitmap(bmIn);

        Log.i(TAG, "width : " + bmIn.getWidth() + ", height : " + bmIn.getHeight());

        ImageView out = (ImageView) findViewById(R.id.filterOut);
        out.setImageBitmap(bmOut);
        Log.i(TAG, "Image View set");


        inAllocation = Allocation.createFromBitmap(renderScript, bmIn,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);

        outAllocation = Allocation.createTyped(renderScript, inAllocation.getType(),
                Allocation.USAGE_SCRIPT);

        invoke_script_sobel(in.getWidth(), in.getHeight());

        outAllocation.copyTo(bmOut);

        out.invalidate();

    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    void invoke_script_sobel(int width, int height){

        Log.i(TAG, "width : " + width + ", height : " + height);


        script = new ScriptC_sobel(renderScript);

        script.set_width(width);
        script.set_height(height);
        script.set_gIn(inAllocation);
        script.set_gOut(outAllocation);
        script.set_gScript(script);

        script.forEach_SobelFirstPass(inAllocation, outAllocation);
        script.forEach_SobelSecondPass(inAllocation, outAllocation);

//
//       script.invoke_sobel();
        Log.i("SepiaFilterActivity: ", "Script invocation complete");
    }

}
