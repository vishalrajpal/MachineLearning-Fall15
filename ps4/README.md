# Details to run the algorithm
## OnlineLearning: A Wrapper class to all the algorithms. See below for more information
### Create object of OnlineLearning class
```java
//instances: the number of instances to generate
//trainingFileName: The file containing generated training instances
OnlineLearning ol = new OnlineLearning(l, m, n, instances, trainingFileName);
```

### Tuning on different Algorithms, which will give best parameters and accuracy on D2
```java      
ol.tuneOnPerceptron();
ol.tuneOnWinnow();
```

### Decide on different Algorithms, which will evaluate on the entire testing set.
```java
ol.decideOnPerceptron();
ol.decideOnWinnow();
```

### Convergence Tests on different Algorithms
```java
int S = 791;
ol.convergeOnPerceptron(S);
ol.convergeOnWinnow(S);      
```

### Batch Evaluation on different Algorithms
```java
ol.batchOnPerceptron();
ol.batchOnWinnow();
ol.batchOnNaiveBayes();
ol.batchEvaluateOnNaiveBayes(cleanFileName);//cleanFileName is the file containing testing instances.
ol.batchEvaluateOnPerceptron(cleanFileName);
ol.batchEvaluateOnWinnow(cleanFileName);
```