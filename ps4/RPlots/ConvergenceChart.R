perceptronWOMargin <- read.csv("./WithoutMarginPerceptronConvergence.csv", header = FALSE, sep = ",")
perceptronWOMargindataFrame <- data.frame(perceptronWOMargin[1],perceptronWOMargin[2])
plot(perceptronWOMargin, type="o", col="red",  xaxt="n", yaxt="n", xlab = "Dimensionality", ylab = "No Of Mistakes", main="Convergence/Mistakes Chart")

axis(1, at = seq(40, 200, by = 40))
axis(2, at = seq(0, 1000, by = 25))

perceptronWithMargin <- read.csv("./WithMarginPerceptronConvergence.csv", header = FALSE, sep = ",")
par(new = TRUE)
plot(perceptronWithMargin, type="o", xaxt="n", yaxt="n", col="green", xlab = "", ylab = "")

winnowWOMargin <- read.csv("./WithoutMarginWinnowConvergence.csv", header = FALSE, sep = ",")
par(new = TRUE)
plot(winnowWOMargin, type="o", xaxt="n", yaxt="n", col="blue", xlab = "", ylab = "")

winnowWithMargin <- read.csv("./WithMarginWinnowConvergence.csv", header = FALSE, sep = ",")
par(new = TRUE)
plot(winnowWithMargin, type="o", xaxt="n", yaxt="n", col="purple", xlab = "", ylab = "", axes = F)