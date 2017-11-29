package com.algonquincollege.bowe0145.hsvcolorpicker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Arrays;

public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    TextView colorSwatch;
    SeekBar hueSB;
    SeekBar saturationSB;
    SeekBar brightnessSB;

    float[] colorArray = new float[3];

    public void clickBarButton(View button) {
        float[] newColorArray = new float[3];
        int color = ((ColorDrawable)button.getBackground()).getColor();

        // Set the swatch
        colorSwatch.setBackgroundColor(color);

        // Set the 3 sliders
        Color.colorToHSV(color, newColorArray);
        for (int i =1; i < newColorArray.length; i++) {
            newColorArray[i]*=100;
        }

        hueSB.setProgress((int) newColorArray[0]);
        saturationSB.setProgress((int) newColorArray[1]);
        brightnessSB.setProgress((int) newColorArray[2]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Controls
        colorSwatch = (TextView)findViewById(R.id.colorSwatch);
        hueSB = (SeekBar)findViewById(R.id.hueSB);
        saturationSB = (SeekBar)findViewById(R.id.saturationSB);
        brightnessSB = (SeekBar)findViewById(R.id.valueSB); // I find value really confusing

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

        colorArray[0] = 0;
        colorArray[1] = 0;
        colorArray[2] = 0;

        colorSwatch.setBackgroundColor(Color.HSVToColor(colorArray));
    }

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

    void Draw() {
        colorSwatch.setText(readableHSV());
        colorSwatch.setBackgroundColor(Color.HSVToColor(colorArray));
    }

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

    @Override
    public void onProgressChanged(SeekBar SB, int progress, boolean b) {
        float[] newColorArray = colorArray;

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

        colorArray = newColorArray;
        Draw();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
