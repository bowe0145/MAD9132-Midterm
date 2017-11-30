package com.algonquincollege.bowe0145.hsvcolorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  A HSV Color Picker
 *  @author Ryan Bowes (bowe0145@algonquinlive.com)
 */

// Implementing the Seekbar change listener so I can use one method for all of them
public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    TextView colorSwatch;
    SeekBar hueSB;
    SeekBar saturationSB;
    SeekBar brightnessSB;

    float[] colorArray = new float[3];

    // When a bar button is clicked
    // I set them in the XML because I wanted to test it. Apparently there's no performance loss, so it seems ok
    public void clickBarButton(View button) {
        // Create a new array for the HSV which we will modify here
        float[] newColorArray = new float[3];
        // Get the color that was pressed (hex code to int)
        int color = ((ColorDrawable)button.getBackground()).getColor();

        // Set the swatch
        colorSwatch.setBackgroundColor(color);

        // Set the 3 sliders
        Color.colorToHSV(color, newColorArray);
        // The sliders go from 0-100, but the SV go from 0-1, so multiply by 100
        for (int i =1; i < newColorArray.length; i++) {
            newColorArray[i]*=100;
        }

        // Set the bars to the correct spots
        hueSB.setProgress((int) newColorArray[0]);
        saturationSB.setProgress((int) newColorArray[1]);
        brightnessSB.setProgress((int) newColorArray[2]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the controls
        colorSwatch = (TextView)findViewById(R.id.colorSwatch);
        hueSB = (SeekBar)findViewById(R.id.hueSB);
        saturationSB = (SeekBar)findViewById(R.id.saturationSB);
        brightnessSB = (SeekBar)findViewById(R.id.valueSB); // I find value really confusing

        // Set the listeners
        hueSB.setOnSeekBarChangeListener(this);
        saturationSB.setOnSeekBarChangeListener(this);
        brightnessSB.setOnSeekBarChangeListener(this);

        colorSwatch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showToast();
                return true;
            }
        });

        // Set the default color (black)
        colorArray[0] = 0;
        colorArray[1] = 0;
        colorArray[2] = 0;

        // Set the color swatch to the color
        colorSwatch.setBackgroundColor(Color.HSVToColor(colorArray));
    }

    // Validation
    Boolean isValidHue (float hue) {
        // The hue's domain range is: 0 to 359 degrees (inclusive)
        return hue >= 0 && hue <= 359;
    }
    Boolean isValidSaturation (float saturation) {
        // The saturation's domain range is: 0 to 100% (inclusive)
        return saturation >= 0 && saturation <= 100.0f;
    }
    Boolean isValidBrightness (float brightness) {
        // The value's domain range is: 0 to 100% (inclusive)
        return brightness >= 0 && brightness <= 100.0f;
    }

    // The draw for the swatch
    void Draw() {
        colorSwatch.setText(readableHSV());
        colorSwatch.setBackgroundColor(Color.HSVToColor(colorArray));
    }

    // Helper function to convert HSV to text (no parameter since I wasn't using one
    String readableHSV () {
        // Create a readable hsv
        String text = "";
        int h = (int)Math.floor(colorArray[0]);
        int s = (int)Math.floor(colorArray[1]*100);
        int v = (int)Math.floor(colorArray[2]*100);

        // Create the string
        text = "H: " + h + "° S: " + s + "% V: " + v + "%";

        return text;
    }

    void showToast() {
        // Display the string
        Toast.makeText(getApplicationContext(), readableHSV(), Toast.LENGTH_SHORT).show();
    }

    // The progress change listener that all the seek bars use
    @Override
    public void onProgressChanged(SeekBar SB, int progress, boolean b) {
        // Create a clone of the color array to modify
        float[] newColorArray = colorArray;

        // Check which 1 is being used and if its valid then set the correct piece of the array
        if (SB.equals(hueSB)) {
            if (isValidHue(progress)) {
                newColorArray[0] = progress;
            }
        } else if (SB.equals(saturationSB)) {
            if (isValidSaturation(progress)) {
                newColorArray[1] = (float)progress/100;
            }
        } else {
            if (isValidBrightness(progress)) {
                newColorArray[2] = (float)progress/100;
            }
        }

        // Set the color array to the new one with the updated info
        colorArray = newColorArray;
        // Draw the swatch
        Draw();
    }

    // The other required Seekbar methods
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
