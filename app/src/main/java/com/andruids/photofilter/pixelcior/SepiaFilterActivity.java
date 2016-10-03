package com.andruids.photofilter.pixelcior;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.*;
import android.util.Log;
import android.widget.ImageView;

import com.android.rs.sepia.ScriptC_sepia;

/**
 * Created by mithun on 10/1/16.
 */
public class SepiaFilterActivity extends Activity {

    private RenderScript renderScript;
    private Allocation inAllocation;
    private Allocation outAllocation;
    private ScriptC_sepia  script;

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

        Log.i("SepiaFilterActivity: ", "Set bmIn and bmOut done");

        ImageView in = (ImageView) findViewById(imageId);
        in.setImageBitmap(bmIn);

        ImageView out = (ImageView) findViewById(R.id.filterOut);
        out.setImageBitmap(bmOut);
        Log.i("SepiaFilterActivity: ", "Image View set");


        inAllocation = Allocation.createFromBitmap(renderScript, bmIn,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());

        invoke_script_sepia();

        outAllocation.copyTo(bmOut);

        out.invalidate();

   }

   void invoke_script_sepia(){
       float depth = 0.2f;
       float intensity = 0.05f;

       script = new ScriptC_sepia(renderScript);

       script.set_depth(depth);
       script.set_intensity(intensity);
       script.set_gIn(inAllocation);
       script.set_gOut(outAllocation);
       script.set_gScript(script);

       script.invoke_sepia();
       Log.i("SepiaFilterActivity: ", "Script invocation complete");
   }

}
