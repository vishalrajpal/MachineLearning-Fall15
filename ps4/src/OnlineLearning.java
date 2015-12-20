import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class OnlineLearning
{
   private static int HELDOUT_SIZE;
   private int L;
   private int M;
   private int N;
   private int noOfInstances;
   private File trainingInstancesFile;
   private List<TrainingInstance> instances;
   private Set<Integer> D1;
   private Set<Integer> D2;
   boolean isD1D2Generated;
   private Random random;
   private Perceptron perceptron;
   private Winnow winnow;
   private String cleanFileName;
   private List<TrainingInstance> cleanInstances;
   public OnlineLearning(int L, int M, int N, int noOfInstances, String trainingFile) {
      this.L = L;
      this.M = M;
      this.N = N;
      this.noOfInstances = noOfInstances;
      this.trainingInstancesFile = new File(trainingFile);
      this.instances = Utilities.readTrainingFile(this.trainingInstancesFile);
      this.random = new Random();
      HELDOUT_SIZE = (int)((10.0/100.0) * noOfInstances);
      isD1D2Generated = false;
      perceptron = new Perceptron(instances, this.N);
      winnow = new Winnow(instances, this.N);
   }
   
   public void tuneOnPerceptron() {
      generateTuningPositions();
      perceptron.tune(D1, D2);
   }
   
   public void decideOnPerceptron() {
      perceptron.decide();
   }
   
   public void tuneOnWinnow() {
      generateTuningPositions();
      winnow.tune(D1, D2);
   }
   
   public void decideOnWinnow() {
      winnow.decide();
   }
   
   public void convergeOnPerceptron(int S) {
      perceptron.converge(S);
   }
   
   public void convergeOnWinnow(int S) {
      winnow.converge(S);
   }
   
   public void batchOnPerceptron() {
      perceptron.batch();
   }
   
   public void batchOnWinnow() {
      winnow.batch();
   }
   
   public void batchEvaluateOnPerceptron(String cleanFileName) {
      generateCleanInstances(cleanFileName); 
      perceptron.batchEvaluate(this.cleanInstances);
   }
   
   public void batchEvaluateOnWinnow(String cleanFileName) {
      generateCleanInstances(cleanFileName);
      winnow.batchEvaluate(this.cleanInstances);
   }
   
   private void generateCleanInstances(String cleanFileName) {
      if(this.cleanFileName==null || !this.cleanFileName.equals(cleanFileName)) {
         File cleanFile = new File(cleanFileName);
         this.cleanInstances = Utilities.readTrainingFile(cleanFile);
      }
   }
   private void generateTuningPositions() {
      if(!isD1D2Generated) {
         isD1D2Generated = true;
         this.D1 = GenerateLMN.generatePositions(this.random, HELDOUT_SIZE, noOfInstances);
         this.D2 = GenerateLMN.generatePositions(this.random, HELDOUT_SIZE, noOfInstances);
      }
   }
   
}
