import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Random;

public class GenerateLMN {

   public static int l = 10;
   public static int m = 100;
   public static int n = 500;

   public static int instances = 10000;

   public static double labelNoise = 0.0;	// 0.05 for part 3
   public static double featureNoise = 0.0;	// 0.001 for part 3
   public static double irrelevantActive = 0.5;	// probability of irrelevant feature being active
   public static double labelRatioPos = 0.5;		// ratio of postive to negative labels

   public static long rngSeed = 239485235295L;
   //public static long rngSeed = System.currentTimeMillis();

   public static void generateInstances(String filePath) {
      try {
         BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
         Random rng = new Random(rngSeed);

         int positive = (int) Math.ceil((double) instances * labelRatioPos); 
         int negative = instances - positive;

         int current_pos = 0;
         int current_neg = 0;

         String newInstance;
         while ((current_pos < positive) || (current_neg < negative)) {
            if ((current_neg < negative) && ((current_pos >= positive) || (rng.nextDouble() >= labelRatioPos))) {
               newInstance = generateNegative(rng);
               current_neg++;
            } else {
               newInstance = generatePositive(rng);
               current_pos++;
            }
            writer.write(newInstance);
            writer.write("\n");
         }
         writer.flush();
         writer.close();
      } catch(Exception e) {
         System.out.println("Error generating instances!");
         System.exit(1);
      }
   }

   public static String generatePositive(Random rng) {
      int num_active = rng.nextInt(m-l+1) + l;
      StringBuilder sb = new StringBuilder();
      if (rng.nextDouble() < labelNoise)
         sb.append("-");
      else
         sb.append("+");
      sb.append("1");
      HashSet<Integer> active = generatePositions(rng, num_active, m);
      for (int i = 1; i <= m; i++) {
         double fNoise = rng.nextDouble();
         if ((active.contains(i) && (fNoise >= featureNoise)) ||
               (!active.contains(i) && (fNoise < featureNoise)))
            sb.append(" " + i + ":1");
      }
      for (int i = (m + 1); i <= n; i++) {
         double fNoise = rng.nextDouble();
         if (((fNoise < irrelevantActive) && (rng.nextDouble() >= featureNoise)) || 
               ((fNoise >= irrelevantActive) && (rng.nextDouble() < featureNoise)))
            sb.append(" " + i + ":1");
      }
      return sb.toString();
   }

   public static String generateNegative(Random rng) {
      int num_active = rng.nextInt(l);
      StringBuilder sb = new StringBuilder();
      //System.out.println(num_active);
      if (rng.nextDouble() < labelNoise)
         sb.append("+");
      else
         sb.append("-");
      sb.append("1");
      HashSet<Integer> active = generatePositions(rng, num_active, m);
      for (int i = 1; i <= m; i++) {
         double fNoise = rng.nextDouble();
         if ((active.contains(i) && (fNoise >= featureNoise)) ||
               (!active.contains(i) && (fNoise < featureNoise)))
            sb.append(" " + i + ":1");
      }
      for (int i = (m + 1); i <= n; i++) {
         double fNoise = rng.nextDouble();
         if (((fNoise < irrelevantActive) && (rng.nextDouble() >= featureNoise)) ||
               ((fNoise >= irrelevantActive) && (rng.nextDouble() < featureNoise)))
            sb.append(" " + i + ":1");
      }
      return sb.toString();
   }

   public static HashSet<Integer> generatePositions(Random rng, int a, int m) {
      HashSet<Integer> result = new HashSet<Integer>();      
      int remainder = a;
      int possible = m;
      for (int i = 1; i <= m; i++) {
         if (rng.nextDouble() < ((double) remainder / possible)) {
            result.add(i);
            remainder--;
         }
         possible--;
      }
      return result;
   }

   public static void main(String[] args) {
      /*
       * If args is valid use dynamic numbers else use the default set.
       */
      boolean toCreateNoisySet = false;
      int cleanSetSize = 0;
      if(args.length >= 4) {
         l = Integer.parseInt(args[0]);
         m = Integer.parseInt(args[1]);
         n = Integer.parseInt(args[2]);
         instances = Integer.parseInt(args[3]);
         toCreateNoisySet = Boolean.parseBoolean(args[4]);         
      }
      String trainingFileName = "training_instances.libsvm";
      String cleanFileName = "training_instances_clean.libsvm";
      if(toCreateNoisySet) {         
         int prevInstances = instances;
         double prevLabelNoise = labelNoise;
         double prevFeatureNoise = featureNoise;
         labelNoise = 0.05;
         featureNoise = 0.001;
         generateInstances(trainingFileName);
         labelNoise = prevLabelNoise;
         featureNoise = prevFeatureNoise;         
         cleanSetSize = Integer.parseInt(args[5]);
         instances = cleanSetSize;
         generateInstances(cleanFileName);
         instances = prevInstances;
      } else {
         generateInstances(trainingFileName);
      }
      
      OnlineLearning ol = new OnlineLearning(l, m, n, instances, trainingFileName);
      //int S = 791;
      ol.tuneOnPerceptron();
      ol.tuneOnWinnow();
      //ol.decideOnPerceptron();
      //ol.decideOnWinnow();
      //ol.convergeOnPerceptron(S);
      //ol.convergeOnWinnow(S);
      
      ol.batchOnPerceptron();
      ol.batchOnWinnow();
      ol.batchEvaluateOnPerceptron(cleanFileName);
      ol.batchEvaluateOnWinnow(cleanFileName);
      
      //System.out.println("Winnow");
      
      
   }
}
