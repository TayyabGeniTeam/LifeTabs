package com.geniteam.lifetabs.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import org.opencv.core.Point;

/**
 * Created by Brian Dryja on 5/2/2016.
 */
public class Sample {

    private Bitmap image;
    private Point point1;
    private Point point2;
    private Point point3;
    private Point point4;
    private int sampleColor;
    private int percentageToCenter = 0;

    public Sample(Bitmap img, Point p1, Point p2, Point p3, Point p4) {
        image = img;
        point1 = p1;
        point2 = p2;
        point3 = p3;
        point4 = p4;

        sampleColor = getAllInteriorPoints();
    }

    public void setPercentToCenter(int percentage){
        percentageToCenter = percentage;
    }

    public int getPercentageToCenter(){
        return percentageToCenter;
    }

    public int getColor(){
        return sampleColor;
    }

    private int getAllInteriorPoints(){
        Path path = new Path();
        path.moveTo((float)point1.x, (float)point1.y);
        path.lineTo((float)point2.x, (float)point2.y);
        path.lineTo((float)point3.x, (float)point3.y);
        path.lineTo((float)point4.x, (float)point4.y);
        path.lineTo((float)point1.x, (float)point1.y);
        path.close();

        RectF rectF = new RectF();
        path.computeBounds(rectF, true);

        Region region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));

        //Color c = null;
        Point small = getSmallestPoint();
        Point large = getLargestPoint();
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        long pixelCount = 0;
        int pixel;

        //start at top of y and move down
        for (int y = (int)small.y; y <= (int)large.y; y++){

            //now lets go across x for each y
            for (int x = (int)small.x; x <= (int)large.x; x++){

                //if point is actually in our shape
                if(region.contains(x, y)){

                    //get the color of that point
                    pixel = image.getPixel(x, y);

                    //number of pixels added so far
                    pixelCount++;

                    //add it to our count for the average
                    redBucket += Math.pow(Color.red(pixel),2);
                    greenBucket += Math.pow(Color.green(pixel),2);
                    blueBucket += Math.pow(Color.blue(pixel),2);
                }

            }

        }

        double r = Math.sqrt(redBucket/pixelCount);
        double g = Math.sqrt(greenBucket/pixelCount);
        double b = Math.sqrt(blueBucket/pixelCount);

        //return the average color
        return Color.rgb( (int)r, (int)g, (int)b );
    }

    private Point getLargestPoint(){
        double largestX = point1.x;
        double largestY = point1.y;

        if(point2.x > largestX){
            largestX = point2.x;
        }

        if(point3.x > largestX){
            largestX = point3.x;
        }

        if(point4.x > largestX){
            largestX = point4.x;
        }

        if(point2.y > largestY){
            largestY = point2.y;
        }

        if(point3.y > largestY){
            largestY = point3.y;
        }

        if(point4.y > largestX){
            largestY = point4.y;
        }
        return new Point(largestX, largestY);
    }

    private Point getSmallestPoint(){
        double smallestX = point1.x;
        double smallestY = point1.y;

        if(point2.x < smallestX){
            smallestX = point2.x;
        }

        if(point3.x < smallestX){
            smallestX = point3.x;
        }

        if(point4.x < smallestX){
            smallestX = point4.x;
        }

        if(point2.y < smallestY){
            smallestY = point2.y;
        }

        if(point3.y < smallestY){
            smallestY = point3.y;
        }

        if(point4.y < smallestY){
            smallestY = point4.y;
        }
        return new Point(smallestX, smallestY);
    }
}
