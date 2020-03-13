import javax.xml.crypto.Data;

public class Comparison {
    public static void main(String[] args){

        DataSet dataset1 = new DataSet(1/5.316, 25);
        DataSet dataset2 = new DataSet(1/5.316, 25);

        dataset1.readFileAsPoints("KAKARLA_SAMHITH.csv");
        dataset2.readFileAsPoints("DOBERVICH_DAVID.csv");

        System.out.println(dataset1.getTotalDist());
        System.out.println(dataset2.getTotalDist());

        System.out.println(dataset1.getDistIn(50, 65));
        System.out.println(dataset2.getDistIn(50, 65));

        System.out.println(dataset1.getAvgSpeed(30, 40));
        System.out.println(dataset2.getAvgSpeed(30, 40));

        Point p1 = new Point(233, 308);
        System.out.println(dataset1.timeSpentInReigon(p1, 100));
        System.out.println(dataset2.timeSpentInReigon(p1, 100));




    }



}
