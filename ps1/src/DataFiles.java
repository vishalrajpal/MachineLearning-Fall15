import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


public abstract class DataFiles
{
   private static NumberFormat formatter = new DecimalFormat("#0.00");
   public static DataFile createDataFile(String type, File file, String dataType, boolean toPredict) {
      DataFile newDataFile = null;
      switch(type) {
      case "CA": {
         newDataFile = new CreditAuthorityDataFile(file, dataType, toPredict);
         break;
      }
      case "Lenses":{
         newDataFile = new LensesDataFile(file, dataType, toPredict);
         break;
      }
      }
      return newDataFile;
   }

   public static String getAccuracy(List<Instance> instances){
      
      double total = 0;
      double counter = 0;
      for(Instance instance : instances) {
         total++;
         if(instance.getPredictionLabel().equals(instance.getLabel())) {
            counter++;
         }
      }
      return formatter.format(counter/total);
   }
}
