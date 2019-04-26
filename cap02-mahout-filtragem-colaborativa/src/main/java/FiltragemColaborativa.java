import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class FiltragemColaborativa {

	public static void main(String[] args) throws IOException, TasteException {

		String urlArquivo = FiltragemColaborativa.class.getClassLoader().getResource("u.data").getPath();

		File file = new File(urlArquivo);

		FileDataModel fileDataModel = new FileDataModel(file);

		int quantVizinhos = 10;
		int totalRecomendacoes = 5;

		long userId = 7;
		
		
		
		//Filtragem colaborativa baseado em usuário
		
		UserSimilarity similarity = new PearsonCorrelationSimilarity(fileDataModel);
		UserNeighborhood neighborhood = new NearestNUserNeighborhood(quantVizinhos, similarity, fileDataModel);
		GenericUserBasedRecommender userRecommender = new GenericUserBasedRecommender
				(fileDataModel, neighborhood,
				similarity);

		

		List<RecommendedItem> recommendations = userRecommender.recommend(userId, totalRecomendacoes);

		for (RecommendedItem item : recommendations)
			System.out.println(item);

		
		//Filtragem colaborativa baseado em item

		
		ItemSimilarity similarityItem = new TanimotoCoefficientSimilarity(fileDataModel);

		GenericItemBasedRecommender itemRecommender = new GenericItemBasedRecommender(fileDataModel,
				similarityItem);

		recommendations  = itemRecommender.recommend(userId, totalRecomendacoes);
		
		for (RecommendedItem item : recommendations)
			System.out.println(item);
	}

}
