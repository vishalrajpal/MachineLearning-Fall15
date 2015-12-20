
public class LensesInstance implements Instance
{
   private int age;
   private int prescription;
   private int astigmatic;
   private int tearRate;
   private int label;
   private String predictionLabel;
   private int instanceId;
   public LensesInstance(String[] data, int instanceId) {
      if(data.length != 5) {
         System.exit(1);
      }
      this.instanceId = instanceId;
      this.age = Integer.parseInt(data[0]);
      this.prescription = Integer.parseInt(data[1]);
      this.astigmatic = Integer.parseInt(data[2]);
      this.tearRate = Integer.parseInt(data[3]);
      this.label = Integer.parseInt(data[4]);
   }

   @Override
   public boolean hasMissingValue()
   {
      return false;
   }

   @Override
   public Attribute<EnumInterface> getStringValueByIndex(int attributeIndex)
   { return null;}

   @Override
   public Attribute<Double> getDoubleValueByIndex(int attributeIndex)
   { return null;}

   @Override
   public void replaceMissingDoubles(double... means)
   {}

   @Override
   public void replaceMissingStrings(String... medians)
   {}

   @Override
   public String getLabel()
   {
      return Integer.toString(label);
   }

   public int getInstanceId(){
      return this.instanceId;
   }
   
   @Override
   public double getL2Distance(Instance i)
   {
      double result = 0;
      LensesInstance l = (LensesInstance)i;
      result = l.getAge()==this.getAge() ? 0 : 1;
      result += l.getPrescription()==this.getPrescription() ? 0 : 1;
      result += l.getAstigmatic()==this.getAstigmatic() ? 0 : 1;
      result += l.getTearRate()==this.getTearRate() ? 0 : 1;
      return result;
   }
   
   public void setPredictionLabel(String s) {
      this.predictionLabel = s;
   }

   public int getAge()
   {
      return age;
   }

   public int getPrescription()
   {
      return prescription;
   }

   public int getAstigmatic()
   {
      return astigmatic;
   }

   public int getTearRate()
   {
      return tearRate;
   }

   public String getPredictionLabel()
   {
      return predictionLabel;
   }

   @Override
   public String toString()
   {
      return age + "," + prescription + "," + astigmatic + "," + tearRate
            + "," + label;
   }
   
   
   
}
