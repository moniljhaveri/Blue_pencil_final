package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Typeface;



public class editorpage extends Activity {
    private static String logtag = "editor page";
    //String[] filterresources;
    //Spinner filterButton;
    Bitmap BW;
    Bitmap editBitmap;
    ImageView editImage;
    int picnum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorpage);

        setContentView(R.layout.activity_editorpage);
        editImage = (ImageView) findViewById(R.id.image);
        Uri viewUri = getIntent().getData();
        try {
            editBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), viewUri);
            editImage.setImageBitmap(editBitmap);

            //break; //may need
        } catch (Exception e){
            Toast.makeText(editorpage.this, "failed to load", Toast.LENGTH_LONG).show();
            Log.e(logtag, e.toString());
        }
        Button backButton = (Button) findViewById(R.id.back_button); //The back button
        backButton.setOnClickListener(editorListener);
        Typeface futuraMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "Futura-Medium.ttf");
        backButton.setTypeface(futuraMedium);

        Button Greenify = (Button) findViewById(R.id.green_filter);
        Greenify.setOnClickListener(editorListener);
        Greenify.setTypeface(futuraMedium);


        Button Filter1= (Button) findViewById(R.id.filter_button);
        Filter1.setOnClickListener(editorListener);
        Filter1.setTypeface(futuraMedium);

        Button InvertButton = (Button) findViewById(R.id.invert_filter);
        InvertButton.setOnClickListener(editorListener);
        InvertButton.setTypeface(futuraMedium);

        Button Sepia = (Button) findViewById(R.id.sepia_filter);
        Sepia.setOnClickListener(editorListener);
        Sepia.setTypeface(futuraMedium);

        Button Bluetrace = (Button) findViewById(R.id.blue_filter);
        Bluetrace.setOnClickListener(editorListener);
        Bluetrace.setTypeface(futuraMedium);

        Button Redden = (Button) findViewById(R.id.red_filter);
        Redden.setOnClickListener(editorListener);
        Redden.setTypeface(futuraMedium);

        Button Save = (Button) findViewById(R.id.save_button);
        Save.setOnClickListener(editorListener);
        Save.setTypeface(futuraMedium);
//
    }

    private View.OnClickListener editorListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case (R.id.back_button):
               {
                   Intent intent = new Intent(editorpage.this, MainActivity.class);
                   startActivity(intent);
                   break;
               }
               case (R.id.filter_button):
               {
                   BW = ChangeGrey(editBitmap,2,1.0,1.0,0.0);
                   editImage.setImageBitmap(BW);
                   break;
               }
               case (R.id.invert_filter):
               {
                   BW = InvertColor(editBitmap);
                   editImage.setImageBitmap(BW);
                   break;
               }
                case (R.id.sepia_filter):
                {
                    BW = createSepiaToningEffect(editBitmap, 50, 1.5, 0.95, 0.12);
                    editImage.setImageBitmap(BW);
                    break;
                }
               case (R.id.green_filter):
               {
                   BW = Greenify(editBitmap);
                   editImage.setImageBitmap(BW);
                   break;
               }
                case (R.id.blue_filter): {
                    BW = Bluetrace(editBitmap);
                    editImage.setImageBitmap(BW);
                    break;
                }
                case (R.id.red_filter): {
                    BW = Redden(editBitmap);
                    editImage.setImageBitmap(BW);
                    break;
                }
                case (R.id.save_button):{
                    saveBitmap(BW);
                    break;
                }
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editorpage, menu);
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

    public static Bitmap ChangeGrey(Bitmap src, int depth, double red, double green, double blue) { //Need to cite this
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap finalBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        final double grayScale_Red = 0.3;
        final double grayScale_Green = 0.59;
        final double grayScale_Blue = 0.11;

        int channel_aplha, channel_red, channel_green, channel_blue;
        int pixel;
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                channel_aplha = Color.alpha(pixel);
                channel_red = Color.red(pixel);
                channel_green = Color.green(pixel);
                channel_blue = Color.blue(pixel);
                channel_blue = channel_green = channel_red = (int)(grayScale_Red * channel_red + grayScale_Green * channel_green + grayScale_Blue * channel_blue);
                channel_red += (depth * red);

                if(channel_red > 255)
                {
                    channel_red = 255;
                }
                channel_green += (depth * green);
                if(channel_green > 255)
                {
                    channel_green = 255;
                }
                channel_blue += (depth * blue);
                if(channel_blue > 255)
                {
                    channel_blue = 255;
                }
                finalBitmap.setPixel(x, y, Color.argb(channel_aplha, channel_red, channel_green, channel_blue));
            }
        }
        return finalBitmap;
    }

    public static Bitmap InvertColor(Bitmap src) { //android newbie site this, also optimize this
        // create new bitmap with the same settings as source bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final bitmap
        return bmOut;

    }

    public static Bitmap Greenify(Bitmap src){
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 70) {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor) + (255 - Color.green(pixelColor)) / 6;
                    B = Color.blue(pixelColor);
                } else if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 120) {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor) + (255 - Color.green(pixelColor)) / 4;
                    B = Color.blue(pixelColor);
                } else {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);
                }
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }



        // return final bitmap
        return bmOut;


    }

    public static Bitmap Bluetrace(Bitmap src){
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 70) {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor) + (255 - Color.green(pixelColor)) / 6;
                } else if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 120) {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor) + (255 - Color.green(pixelColor)) / 4;
                } else {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);
                }
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }



        // return final bitmap
        return bmOut;


    }

    public static Bitmap Redden(Bitmap src){
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = src.getHeight();
        int width = src.getWidth();

        // scan through every pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // get one pixel
                pixelColor = src.getPixel(x, y);
                // saving alpha channel
                A = Color.alpha(pixelColor);
                // inverting byte for each R/G/B channel
                if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 70) {
                    R = Color.red(pixelColor) + (255 - Color.green(pixelColor)) / 6;
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);
                } else if ((Color.red(pixelColor) + Color.green(pixelColor) + Color.blue(pixelColor)) > 120) {
                    R = Color.red(pixelColor) + (255 - Color.green(pixelColor)) / 4;
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);
                } else {
                    R = Color.red(pixelColor);
                    G = Color.green(pixelColor);
                    B = Color.blue(pixelColor);
                }
                // set newly-inverted pixel to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }



        // return final bitmap
        return bmOut;


    }


    public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // constant grayscale
        final double GS_RED = 0.3;
        final double GS_GREEN = 0.59;
        final double GS_BLUE = 0.11;
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                // get color on each channel
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // apply grayscale sample
                B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

                // apply intensity level for sepid-toning on each channel
                R += (depth * red);
                if(R > 255) { R = 255; }

                G += (depth * green);
                if(G > 255) { G = 255; }

                B += (depth * blue);
                if(B > 255) { B = 255; }

                // set new pixel color to output image
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }
    public void saveBitmap(Bitmap source){
        String pic_name = "picture_"  + String.valueOf(picnum);
        try{
            MediaStore.Images.Media.insertImage(getContentResolver(),source,pic_name, null);
            Toast.makeText(editorpage.this, "Saved Picture", Toast.LENGTH_LONG).show();
            picnum++;
        }catch (Exception e){
            Toast.makeText(editorpage.this, "Save failed", Toast.LENGTH_LONG).show();

        }
        //String P = saveBitmap(BW, imagename)



    }

}



