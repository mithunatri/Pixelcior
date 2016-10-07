package com.andruids.photofilter.pixelcior;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.*;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by ahut on 10/4/16.
 */
public class SharpenFilterActivity extends Activity {

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

        Log.i("SharpenFilterActivity: ", "Set bmIn and bmOut done");

        ImageView in = (ImageView) findViewById(imageId);
        in.setImageBitmap(bmIn);

        ImageView out = (ImageView) findViewById(R.id.filterOut);

        Log.i("SharpenFilterActivity: ", "Image View set");

        float[] coeff = {0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, -1, 5, -1, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0};
        float[] coeff3 = {-2, -1, 0, -1, 1, 1, 0, 1, 2};
        convolve(bmIn, coeff3);

        out.setImageBitmap(bmOut);
        out.invalidate();

    }

    private Bitmap convolve(Bitmap original, float[] coefficients) {
        Bitmap bitmap = Bitmap.createBitmap(
                original.getWidth(), original.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(this);

        Allocation allocIn = Allocation.createFromBitmap(rs, original);
        Allocation allocOut = Allocation.createFromBitmap(rs, bitmap);

        ScriptIntrinsicConvolve3x3 convolution
                = ScriptIntrinsicConvolve3x3.create(rs, Element.U8_4(rs));
        convolution.setInput(allocIn);
        convolution.setCoefficients(coefficients);
        convolution.forEach(allocOut);

        allocOut.copyTo(bmOut);
        rs.destroy();
        return bitmap;
    }

}
