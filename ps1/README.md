# MachineLearning

### process
#### This executable will normalize the data by using z-scale technique and in the next step will use label conditioned Mean/Median technique to impute missing values.

#### execute process by typing following command on your terminal
`./process {training data file} {testing data file}`

### run
#### This executable will calculate the k-nearest distance for each testing instance will all the training instances and will assign a majority label among those k training instances to the testing instance.

#### execute process by typing following command on your terminal
`./run k {training data file} {testing data file}`

#### Please have a look at Report.pdf for more details
Note: These executables use the jars in this repo internally. Please keep them together for successful execution.
