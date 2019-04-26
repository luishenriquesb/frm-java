package mllib;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class ContadorDeLinhas {

	public static void main(String[] args) {
		
		String mode = "local";
		SparkConf conf = new SparkConf().setMaster(mode).setAppName("app name");

		JavaSparkContext sparkContext = new JavaSparkContext(conf);

		String path = ContadorDeLinhas.class.getClassLoader().getResource("hello_world.txt").toString();
		JavaRDD<String> javaRdd = sparkContext.textFile(path);

		long numeroLinhas = javaRdd.count();
		System.out.println(numeroLinhas);

		sparkContext.close();

	}

}
