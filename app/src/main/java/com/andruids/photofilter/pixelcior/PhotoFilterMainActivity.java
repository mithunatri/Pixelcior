package com.andruids.photofilter.pixelcior;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.util.Log;

public class PhotoFilterMainActivity extends AppCompatActivity {

    static final String TAG = PhotoFilterMainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_filter_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Pixelcior Photo Filter App");
        }
   /*     FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo_filter_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showFilterOptions(View view){

        Log.d(TAG,"Photo Clicked");

        ImageView image = (ImageView)view;
        String imageName = String.valueOf(image.getTag());
        final int imageId = image.getId();
        final int drawableId = getResources().getIdentifier(imageName,"drawable", getPackageName());

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item){

                applyFilter(item.getTitle().toString(), imageId, drawableId);

                return true;
            }

        });

        popupMenu.show();
    }

    private void applyFilter(String title, int id, int drawableId){

        switch(title) {

            case "Sepia":
                Log.i(TAG, "Sepia Filter Clicked");
                Intent intent = new Intent(this, SepiaFilterActivity.class);
                intent.putExtra("IMAGE_RESOURCE_ID", String.valueOf(id));
                intent.putExtra("DRAWABLE_ID", String.valueOf(drawableId));
                startActivity(intent);
                break;
            case "Sobel":
                Log.i(TAG, "Sobel Filter Clicked");

                Intent sobelIntent = new Intent(this, SobelFilterActivity.class);
                sobelIntent.putExtra("IMAGE_RESOURCE_ID", String.valueOf(id));
                sobelIntent.putExtra("DRAWABLE_ID", String.valueOf(drawableId));

                startActivity(sobelIntent);

                break;
            case "Sharpen":
                Log.i(TAG, "Sharpen Filter Clicked");
                Intent intentForSharpen = new Intent(this, SharpenFilterActivity.class);
                intentForSharpen.putExtra("IMAGE_RESOURCE_ID", String.valueOf(id));
                intentForSharpen.putExtra("DRAWABLE_ID", String.valueOf(drawableId));
                startActivity(intentForSharpen);
                break;
            case "Shading":
                Log.i(TAG, "Shading Filter Clicked");
                Intent intentForShading = new Intent(this, ShadingFilterActivity.class);
                intentForShading.putExtra("IMAGE_RESOURCE_ID", String.valueOf(id));
                intentForShading.putExtra("DRAWABLE_ID", String.valueOf(drawableId));
                startActivity(intentForShading);
                break;
            default:
                Log.i(TAG, "Nothing was clicked.");
                break;
        }
    }

}

