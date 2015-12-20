import java.util.List;


public interface DataFile
{
   public List<Instance> getInstances();
   public List<Instance> getMergedInstances();
   public void mergeInstances(List<Instance> instances);
   public void replaceMissingValues();
   public void normalizeDoubleValues();
   public void calculateKNearestDistance(Instance newInstance, int k);
   public void calculateKNearestDistanceWith(DataFile trainingFile, int k);
   public void writeToFile() throws Exception;
}
