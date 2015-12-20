
public interface Instance
{
   public boolean hasMissingValue();
   public Attribute<EnumInterface> getStringValueByIndex(int attributeIndex);
   public Attribute<Double> getDoubleValueByIndex(int attributeIndex);
   public void replaceMissingDoubles(double... means);
   public void replaceMissingStrings(String... medians);
   public String getLabel();
   public double getL2Distance(Instance i);
   public void setPredictionLabel(String s);
   public String getPredictionLabel();
   public int getInstanceId();
}
