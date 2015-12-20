import java.io.File;


public class ProcessCreditAuthority
{
   public static void main(String[] args) throws Exception {
      if(args.length != 2) {
         System.exit(1);
      }
      File trainingFile = new File(args[0]);
      File testingFile = new File(args[1]);
      DataFile trainingDataFile = DataFiles.createDataFile("CA", trainingFile, "training", false);
      DataFile testingDataFile = DataFiles.createDataFile("CA", testingFile, "testing", false);
      trainingDataFile.mergeInstances(testingDataFile.getInstances());
      trainingDataFile.normalizeDoubleValues();
      trainingDataFile.replaceMissingValues();
      trainingDataFile.writeToFile();
      testingDataFile.writeToFile();
      
   }
}
