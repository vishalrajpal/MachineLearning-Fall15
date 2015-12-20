import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class CreditAuthorityDataFile implements DataFile
{
   private File file;
   private List<Instance> instances;
   private List<Instance> mergedInstances;
   private String dataType;
   private boolean toPredict;
   
   public CreditAuthorityDataFile(File file, String dataType, boolean toPredict) {
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
         while( (line = brFile1.readLine()) != null){
            data = line.split(",");
            instances.add(new CAInstance(data, currentInstanceId++));
         }
         brFile1.close();
         this.instances = instances;
         this.mergedInstances = new ArrayList<Instance>();
         this.mergedInstances.addAll(instances);
      } catch (Exception e) {
         System.out.print("Unable to create data file:" + e);
         System.exit(1);
      }
   }
   
   public List<Instance> getInstances()
   {
      return instances;
   }
   
   public List<Instance> getMergedInstances()
   {
      return mergedInstances;
   }
   
   public void mergeInstances(List<Instance> instances) {
      this.mergedInstances.addAll(instances);
   }
   
   public void replaceMissingValues() {
      replaceMissingDoubleValues();
      replaceMissingCategoricalFeatures();
   }
   
   public void normalizeDoubleValues() {
      normalizeAttribute(1);
      normalizeAttribute(2);
      normalizeAttribute(7);
      normalizeAttribute(10);
      normalizeAttribute(13);
      normalizeAttribute(14);
   }
   
   public void calculateKNearestDistance(Instance newInstance, int k) {
      Map<Instance, Double> map = new HashMap<Instance, Double>();
      for(Instance i : this.instances) {
         map.put(i,i.getL2Distance(newInstance));
      }
      
      SortedMap<Instance, Double> sortedMap = new TreeMap<>(new ValueComparator(map));
      sortedMap.putAll(map);
      Map<String, Integer> labelMap = new HashMap<String, Integer>();
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
   
   public void calculateKNearestDistanceWith(DataFile trainingFile, int k) {
      for(Instance i : instances) {
         trainingFile.calculateKNearestDistance(i, k);
      }
   }
   
   private void normalizeAttribute(int attributeIndex) {
      double mean = getMean(attributeIndex);
      double standardDeviation = calculateStandardDeviation(attributeIndex, mean);
      Attribute<Double> value;
      double zScaleValue;
      
      for(Instance i : this.mergedInstances) {
         value = i.getDoubleValueByIndex(attributeIndex);
         if(!value.isMissingValue()) {
            zScaleValue = (value.getValue() - mean)/standardDeviation;
            value.setValue(zScaleValue);
         }
      }
   }
   
    
   
   private void replaceMissingDoubleValues() {
      double[] positiveMeans = new double[6];
      double[] negativeMeans = new double[6];
      
      positiveMeans[0] = getLabelConditionedMean(1, "+");
      positiveMeans[1] = getLabelConditionedMean(2, "+");
      positiveMeans[2] = getLabelConditionedMean(7, "+");
      positiveMeans[3] = getLabelConditionedMean(10, "+");
      positiveMeans[4] = getLabelConditionedMean(13, "+");
      positiveMeans[5] = getLabelConditionedMean(14, "+");
      
      negativeMeans[0] = getLabelConditionedMean(1, "-");
      negativeMeans[1] = getLabelConditionedMean(2, "-");
      negativeMeans[2] = getLabelConditionedMean(7, "-");
      negativeMeans[3] = getLabelConditionedMean(10, "-");
      negativeMeans[4] = getLabelConditionedMean(13, "-");
      negativeMeans[5] = getLabelConditionedMean(14, "-");
      
      for(Instance i : mergedInstances) {
         if(i.hasMissingValue()) {
            if(i.getLabel().equals("+")) {
               i.replaceMissingDoubles(positiveMeans);
            }else {
               i.replaceMissingDoubles(negativeMeans);
            }
         }
      }
   }
   
   private void replaceMissingCategoricalFeatures() {
      String[] medians = new String[9];
      medians[0] = getMedian(0);
      medians[1] = getMedian(3);
      medians[2] = getMedian(4);
      medians[3] = getMedian(5);
      medians[4] = getMedian(6);
      medians[5] = getMedian(8);
      medians[6] = getMedian(9);
      medians[7] = getMedian(11);
      medians[8] = getMedian(12);
      for(Instance i : this.mergedInstances) {
         if(i.hasMissingValue()) {
            i.replaceMissingStrings(medians);
         }
         
      }
   }
   
   private double getMean(int attributeIndex) {
      double sum = 0;
      int noOfProperValues = 0;
      Attribute<Double> value;
      for(Instance i : mergedInstances) {
         value = i.getDoubleValueByIndex(attributeIndex);
         if(!value.isMissingValue()) {
            sum += value.getValue().doubleValue();
            noOfProperValues++;
         }
      }
      return sum/noOfProperValues;
   }
   
   private double calculateStandardDeviation(int attributeIndex, double mean) {
      double standardDeviation = 0;
      int properValues = 0;
      Attribute<Double> attribute;
      for(Instance i : mergedInstances) {
         attribute = i.getDoubleValueByIndex(attributeIndex);
         if(!attribute.isMissingValue()) {
            properValues++;
            standardDeviation += Math.pow(attribute.getValue() - mean, 2);
         }
      }
      return Math.sqrt((1.0/properValues)*standardDeviation);
      
      
   }
   
   private double getLabelConditionedMean(int attributeIndex, String label) {
      double sum = 0;
      int noOfProperValues = 0;
      Attribute<Double> value;
      for(Instance i : mergedInstances) {
         value = i.getDoubleValueByIndex(attributeIndex);
         if(!value.isMissingValue() && i.getLabel().equals(label)) {
            sum += value.getValue().doubleValue();
            noOfProperValues++;
         }
      }
      return sum/noOfProperValues;
   }
   
   private String getMedian(int attributeIndex) {
      List<Attribute<EnumInterface>> list = new ArrayList<Attribute<EnumInterface>>();
      Attribute<EnumInterface> value;
      for(Instance i : mergedInstances) {
         value = i.getStringValueByIndex(attributeIndex);
         if(value!=null) {
            list.add(value);
         }
      }
      Collections.sort(list, new Comparator<Attribute<EnumInterface>>(){
         @Override
         public int compare(Attribute<EnumInterface> o1,
               Attribute<EnumInterface> o2)
         {
            return o1.getValue().getOrder() - o2.getValue().getOrder();
         }
         
      });
      
      return list.get((int)Math.ceil(list.size()/2) - 1).getValue().getEnumValue();
   }
   
   public void writeToFile() throws Exception {
      File file = new File("crx."+dataType+".processed");
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
