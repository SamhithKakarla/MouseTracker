import processing.core.PApplet;

import java.io.*;
import java.util.ArrayList;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 4500;  // TODO:  Change this value to match video
    DataSet dataset;
    int frameCount = 0;
    int rCount;
    int cCount;
    int whitePixels = 1;
    private float centerx, centery;
    ArrayList<Point> centerList = new ArrayList<>();


    ThesholdFilter threshold = new ThesholdFilter();

    public MouseTracker() {
        dataset = new DataSet(5.316, 25);  // TODO:  feel free to change the constructor
    }

    @Override
    public DImage processImage(DImage img) {

        rCount = 0;
        cCount = 0;
        whitePixels = 1;
        frameCount++;
        if (frameCount < MAX_FRAMES) {

            short[][] pixels = img.getBWPixelGrid();

            for (int r = 0; r < pixels.length; r++) {
                for (int c  = 0; c < pixels[0].length; c++) {
                    if((r - 230)*(r - 230) + (c - 310)*(c - 310) > 205*205){
                        pixels[r][c] = 255;
                    }
                    if(pixels[r][c] > 100){
                        pixels[r][c] = 0;
                    }else{
                        pixels[r][c] = 255;
                    }
                }
            }

            for (int r = 0; r < pixels.length; r++) {
                for (int c = 0; c < pixels[0].length; c++) {
                    if(pixels[r][c] == 255){
                        rCount = r + rCount;
                        cCount = c + cCount;
                        whitePixels++;
                        centerx = (float)(cCount/whitePixels);
                        centery = (float)(rCount/whitePixels);
                    }
                }
            }


            centerList.add(new Point(rCount/whitePixels, cCount/whitePixels));
            for (int i = 0; i < centerList.size(); i++) {
                pixels[centerList.get(i).getRow()][centerList.get(i).getCol()] = 220;
                pixels[centerList.get(i).getRow() + 1][centerList.get(i).getCol()] = 220;
                pixels[centerList.get(i).getRow() - 1][centerList.get(i).getCol()] = 220;
                pixels[centerList.get(i).getRow()][centerList.get(i).getCol() + 1] = 220;
                pixels[centerList.get(i).getRow()][centerList.get(i).getCol() - 1] = 220;
            }


            displayInfo(dataset);
            img.setPixels(pixels);
            return img;

//        1).  Filter the image to isolate mouse
//        2).  Extract information about the mouse
//        3).  Load information into dataset.


        } else if(frameCount == MAX_FRAMES + 1){     // If last frame, output CSV data
            // display info if you wish
            outputCSVData(dataset);         // output data to csv file, if you wish
        }

        return img;
    }

    private void outputCSVData(DataSet dataset) {
        //dataset.outputCSVData("C:\Users\skakarla085\IdeaProjects\AnimalTracker\Data.txt");
    }

    private void displayInfo(DataSet dataset) {

        System.out.println(dataset.getTotalDist() + " at frame " + centerList.size());
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.stroke(255,0,0);
        window.ellipse(centerx,centery,10,10);
    }

}

