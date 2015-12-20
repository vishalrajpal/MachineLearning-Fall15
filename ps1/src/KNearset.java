import java.io.File;


public class KNearset
{
   public static void main(String[] args) throws Exception {
      if(args.length != 3) {
         System.exit(1);
      }
      int k = Integer.parseInt(args[0]);
      File trainingFile = new File(args[1]);
      File testingFile = new File(args[2]);
      DataFile trainingDataFile;
      DataFile testingDataFile;
      if(args[1].contains("lenses")) {
         trainingDataFile = DataFiles.createDataFile("Lenses", trainingFile, "training", true);
         testingDataFile = DataFiles.createDataFile("Lenses", testingFile, "testing", true);
      } else {
         trainingDataFile = DataFiles.createDataFile("CA", trainingFile, "training", true);
         testingDataFile = DataFiles.createDataFile("CA", testingFile, "testing", true);
      }
      testingDataFile.calculateKNearestDistanceWith(trainingDataFile, k);
     // System.out.println(DataFiles.getAccuracy(testingDataFile.getInstances()));
      testingDataFile.writeToFile();
   }
}
