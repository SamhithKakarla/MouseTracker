import org.omg.CORBA.MARSHAL;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataSet {
    private double cmPerPixel;
    private double FPS;
    private ArrayList<Point> centerList = new ArrayList<>();

    public DataSet(double cmPerPixel, double FPS){
        this.cmPerPixel = cmPerPixel;
        this.FPS = FPS;
    }

    public void addCenter(Point p){
        centerList.add(p);
    }

    public Point getMouseLoc(double time){
        if(time < 0) return new Point(0, 0);
        return centerList.get((int)(time * FPS));
    }

    public double getMouseSpeed(double time){
        if(time < 0) return 0;
        Point p1 = getMouseLoc(time - (time/FPS));
        Point p2 = getMouseLoc(time + (time/FPS));
        double dist = calcDist(p1, p2);
        return (dist/2)*(cmPerPixel/FPS);
    }

    public double getTotalDist(){
        double distance = 0;
        for (int i = 0; i < centerList.size() - 1; i++) {
            distance += calcDist(centerList.get(i), centerList.get(i + 1));
        }

        return distance * cmPerPixel;
    }

    public double getDistIn(double time1, double time2){
        if(time2 < time1) return 0;
        if (time1 < 0 || time2 < 0) return 0;
        double dist = 0;
        for (int i = (int)(time1*FPS); i < (int)(time2*FPS)-1; i++) {
            dist += calcDist(centerList.get(i), centerList.get(i + 1));
        }

        return dist*cmPerPixel;
    }

    public double getAvgSpeed(double time1, double time2){
        if(time2 < time1) return 0;
        if (time1 < 0 || time2 < 0) return 0;
        return (getDistIn(time1/FPS, time2/FPS)/(time2 - time1)) ;
    }

    public double timeSpentInReigon(Point Center, double radius){
        if(radius < 0) return 0;
        double counter = 0;
        for (int i = 0; i < centerList.size(); i++) {
            if(calcDist(centerList.get(i), Center) <= radius){
                counter ++;
            }
        }

        return counter/FPS;
    }
    public ArrayList<Integer> FramesInReigon(Point Center, double radius) {
        if(radius < 0) return null;
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < centerList.size(); i++) {
            if (calcDist(centerList.get(i), Center) <= radius) {
                times.add(i);
            }
        }

        return times;
    }

    private double calcDist(Point a, Point b) {
        return Math.sqrt((a.getRow() - b.getRow())*(a.getRow() - b.getRow()) + (a.getCol() - b.getCol())*(a.getCol() - b.getCol()));
    }

    public void readFileAsPoints(String filepath){
        try(Scanner scanner = new Scanner(new File(filepath))){

            while(scanner.hasNext()){
                String line = scanner.nextLine();
                String[] loc = line.split(",");
                centerList.add(new Point(Integer.parseInt(loc[0]), Integer.parseInt(loc[1])));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void outputCSVData(String filepath, DataSet dataset) {
        try (FileWriter f = new FileWriter(filepath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter p = new PrintWriter(b);){

            for (int i = 0; i < dataset.centerList.size(); i++) {
                p.println(dataset.centerList.get(i).getCol() + "," + dataset.centerList.get(i).getRow());
            }


        } catch (IOException error) {
            System.err.println("There was a problem writing to the file : " + filepath);
            error.printStackTrace();
        }

    }

}
