package mllib;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.ml.evaluation.RegressionEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class RegressaoLinear {

	public static void main(String[] args) {

		SparkSession sparkSession = SparkSession.builder().appName("Exemplo de regressão linear")
				.config("spark.master", "local").getOrCreate();

		// Alteração do nivel log do contexto spark
		sparkSession.sparkContext().setLogLevel("ERROR");

		String pathTrainingData = RegressaoLinear.class.getClassLoader().getResource("boston_housing/train.csv")
				.toString();

		Dataset<Row> trainingData = sparkSession.read().option("header", "true").option("inferSchema", "true")
				.csv(pathTrainingData);

		String pathTestData = RegressaoLinear.class.getClassLoader().getResource("boston_housing/test.csv")
				.toString();

		Dataset<Row> testData = sparkSession.read().option("header", "true").option("inferSchema", "true")
				.csv(pathTestData);

	/*	trainingData.show(5);
		trainingData.printSchema();*/

		/*List<String> colunas = Arrays.asList(trainingData.columns());
		for (String c : colunas) {
			double cor = trainingData.stat().corr(c, "medv");
			System.out.println("correlação entre target e " + c + ": " + cor);
		}
*/
		
		VectorAssembler vectorAssembler = new VectorAssembler().setInputCols(new String[]
				{ "crim", "zn", "indus",
				"chas", "nox", "rm", "age", "dis", "rad", "tax", "ptratio", "black", "lstat" })
				.setOutputCol("features");
		
		

		trainingData = vectorAssembler.transform(trainingData);
		testData = vectorAssembler.transform(testData);
		
		
		LinearRegression linearRegresion = new LinearRegression()
                .setLabelCol("medv")
                .setFeaturesCol("features")
                .setMaxIter(50)
                .setRegParam(0.3)
                .setElasticNetParam(0.8);
		
		LinearRegressionModel linearRegresionModel = linearRegresion.fit(trainingData);
		
		
		LinearRegressionTrainingSummary trainingSummary = linearRegresionModel.summary();
		System.out.println("RMSE: " + trainingSummary.rootMeanSquaredError());
		System.out.println("r2: " + trainingSummary.r2());
		
		/*
		Dataset<Row> predicoes = linearRegresionModel.transform(testData);
		predicoes.select("ID","medv","prediction").show(10);


		RegressionEvaluator regressionEvaluate = new RegressionEvaluator()
		                .setLabelCol("medv")
		                .setMetricName("rmse");

		System.out.println("RMSE: " + regressionEvaluate.evaluate(predicoes));
		System.out.println("Final da validação");*/
		

	}

}
