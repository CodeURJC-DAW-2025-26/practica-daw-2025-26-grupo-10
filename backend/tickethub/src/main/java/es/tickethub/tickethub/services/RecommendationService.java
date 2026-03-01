package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import es.tickethub.tickethub.entities.Event;

@Service
public class RecommendationService {

    private static final double TOLERANCE = 0.5; //it could be significantly increased when we have more events in the DB
    /**
     * Recommends events based on the client's preference vector and event vectors.
     * @param clientService Service providing the client's feature vector
     * @param serverService Service providing the events and their feature vectors
     * @param topN Maximum number of recommendations to return
     * @param applyTolerance Whether to filter recommendations below the TOLERANCE threshold
     * @return List of recommended events
     */
    public List<Event> recommendEvents(ClientRecommendationService clientService,
                                        ServerRecommendationService serverService,
                                        int topN,
                                        boolean applyTolerance) {
        double[] clientVector = clientService.getClientVector();
        List<Event> events = serverService.getEvents();
        List<double[]> eventVectors = serverService.getEventVectors();
        List<Double> similarities = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            similarities.add(cosineSimilarity(clientVector, eventVectors.get(i)));
        }

        QuickSort sorted = new QuickSort(events, similarities);
        List<Event> sortedEvents = sorted.getSortedEvents();
        List<Double> sortedSimilarities = sorted.getSortedSimilarities();

        List<Event> recommended = new ArrayList<>();
        for (int i = 0; i < sortedEvents.size() && recommended.size() < topN; i++) {
            if ((!applyTolerance) || (sortedSimilarities.get(i) >= TOLERANCE)) {
                recommended.add(sortedEvents.get(i));
            }
        }

        return recommended;
    }
    
    // Compute cosine similarity between two vectors (the angle between the user and the event)
    private double cosineSimilarity(double[] v1, double[] v2) {
        double scalarProduct = 0.0;
        double normV1 = 0.0;
        double normV2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            scalarProduct += v1[i] * v2[i];
            normV1 += v1[i] * v1[i];
            normV2 += v2[i] * v2[i];
        }

        return scalarProduct / (Math.sqrt(normV1) * Math.sqrt(normV2));
    }
}