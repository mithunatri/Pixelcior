package com.andruids.photofilter.pixelcior;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

        applyShadingFilter(bmIn, Color.GRAY);
        out.setImageBitmap(bmOut);
        out.invalidate();
    }

    public Bitmap applyShadingFilter(Bitmap source, int shadingColor) {
        // get image size
        int width = source.getWidth();
        int height = source.getHeight();
        int[] pixels = new int[width * height];
        // get pixel array from source
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int index = 0;
        // iteration through pixels
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                // get current index in 2D-matrix
                index = y * width + x;
                // AND
                pixels[index] &= shadingColor;
            }
        }
        // output bitmap
        bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmOut;
    }
}
