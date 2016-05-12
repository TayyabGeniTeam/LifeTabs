package com.geniteam.lifetabs.utils;

import android.graphics.Color;

/**
 * Created by Brian Dryja on 5/2/2016.
 */
public class CompLowCost {

    public CompLowCost() {

    }

    public static int getPercentDistance(int color1, int color2){
        double rDist = (Color.red(color1) + Color.red(color2)) / 2;
        double Dr = Color.red(color1) - Color.red(color2);
        double Dg = Color.green(color1) - Color.green(color2);
        double Db = Color.blue(color1) - Color.blue(color2);
        double DC = Math.sqrt( (2 + (rDist)/(256)) * Math.pow(Dr,2) + 4 * Math.pow(Dg,2) + ( 2 + (255 - rDist)/(256) ) * Math.pow(Db,2) );

        return (int) Math.round(Math.abs((DC/765)-1)*100);
    }

    public static int getPercentDistanceCurve(int color1, int color2){
        double rDist = (Color.red(color1) + Color.red(color2)) / 2;
        double Dr = Color.red(color1) - Color.red(color2);
        double Dg = Color.green(color1) - Color.green(color2);
        double Db = Color.blue(color1) - Color.blue(color2);
        double DC = Math.sqrt( (2 + (rDist)/(256)) * Math.pow(Dr,2) + 4 * Math.pow(Dg,2) + ( 2 + (255 - rDist)/(256) ) * Math.pow(Db,2) );

        int result;

        //the tolerance below can be lowered to make the comp more sensitive. The default to compare
        //all colors is 765
        int tolerance = 200;
        if (DC < tolerance) {
            result = (int) Math.round(Math.abs((DC / tolerance) - 1) * 100);
        }else{
            result = 0;
        }

        return result;
    }

    public static int getPercentBasicRGBLinear(int color1, int color2){
        int diffRed   = Math.abs(Color.red(color1)   - Color.red(color2));
        int diffGreen = Math.abs(Color.green(color1) - Color.green(color2));
        int diffBlue  = Math.abs(Color.blue(color1)  - Color.blue(color2));

        float pctDiffRed   = (float)diffRed   / 255;
        float pctDiffGreen = (float)diffGreen / 255;
        float pctDiffBlue   = (float)diffBlue  / 255;

        float perc = (pctDiffRed + pctDiffGreen + pctDiffBlue) / 3 * 100 ;

        perc = 100 - perc;

        return (int)Math.round(perc);
    }

    public static int getPercentBasicGLinear(int color1, int color2){
        int diffGreen = Math.abs(Color.green(color1) - Color.green(color2));

        float pctDiffGreen = (float)diffGreen / 255;

        pctDiffGreen = 100 - pctDiffGreen ;

        return (int)Math.round(pctDiffGreen);
    }
}
