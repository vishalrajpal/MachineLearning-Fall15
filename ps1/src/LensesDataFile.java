import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class LensesDataFile implements DataFile
{

   private File file;
   private String dataType;
   private List<Instance> instances;
   private boolean toPredict;
   public LensesDataFile(File file, String dataType, boolean toPredict) {
      this.file = file;
      this.dataType = dataType;
      this.toPredict = toPredict;
      readInstances();
   }
   
   private void readInstances() {
      try {
         FileInputStream streamFile1 = new FileInputStream(file);
         BufferedReader brFile1 = new BufferedReader(new InputStreamReader(streamFile1));
         String[] data;
         String line = "";
         List<Instance> instances = new ArrayList<Instance>();
         int currentInstanceId = 1;
         while((line = brFile1.readLine()) != null){
            data = line.split(",");
            instances.add(new LensesInstance(data, currentInstanceId++));
         }
         brFile1.close();
         this.instances = instances;
      } catch (Exception e) {
         System.out.print("Unable to create data file:" + e);
         System.exit(1);
      }
   }
   
   @Override
   public List<Instance> getInstances()
   {
      return instances;
   }

   @Override
   public List<Instance> getMergedInstances()
   {
      return null;
   }

   @Override
   public void mergeInstances(List<Instance> instances)
   {}

   @Override
   public void replaceMissingValues()
   {}

   @Override
   public void normalizeDoubleValues()
   {}

   @Override
   public void calculateKNearestDistance(Instance newInstance, int k)
   {
      Map<Instance, Double> map = new LinkedHashMap<Instance, Double>();
      for(Instance i : this.instances) {
         map.put(i,i.getL2Distance(newInstance));
      }
      
      SortedMap<Instance, Double> sortedMap = new TreeMap<>(new ValueComparator(map));
      sortedMap.putAll(map);
      Map<String, Integer> labelMap = new LinkedHashMap<String, Integer>();
      int i = 1;
      String currentLabel;
      for(Map.Entry<Instance, Double> entry : sortedMap.entrySet()) {
         currentLabel = entry.getKey().getLabel();
         if(labelMap.containsKey(currentLabel)) {
            labelMap.put(currentLabel, labelMap.get(currentLabel) + 1);
         } else {
            labelMap.put(currentLabel, 1);
         }
         i++;
         if(i>k) {
            break;
         }
      }
      
      String maxLabel = null;
      int maxCount = Integer.MIN_VALUE;
      for(Map.Entry<String, Integer> labelEntry : labelMap.entrySet()) {
         if(labelEntry.getValue()>maxCount) {
            maxCount = labelEntry.getValue();
            maxLabel = labelEntry.getKey();
         }
      }
      newInstance.setPredictionLabel(maxLabel);
   }

   @Override
   public void calculateKNearestDistanceWith(DataFile trainingFile, int k)
   {
      for(Instance i : instances) {
         trainingFile.calculateKNearestDistance(i, k);
      }
   }

   public void writeToFile() throws Exception {
      File file = new File("lenses."+dataType);
      BufferedWriter br;;
      if(toPredict) {
         br = new BufferedWriter(new PrintWriter(System.out));
      } else {
         file.delete();
         br = new BufferedWriter(new PrintWriter(file));
      }
      
      for(Instance i : this.instances) {
         if(toPredict) {
            br.write(i.getInstanceId()+",");
         }
         br.write(i.toString());
         if(toPredict) {
            br.write(",");
            br.write(i.getPredictionLabel());
         }
         br.write(System.lineSeparator());
      }
      br.flush();
      br.close();
   }
   
   
}
