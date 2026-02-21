package es.tickethub.tickethub.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import es.tickethub.tickethub.entities.Event;

@Service
public class RecommendationService {
/* Este comentario lo pongo en español porque sino veo a más de uno que lee esto y no sabe ni por donde le caen...
    Lo que hago en esta clase es esencialmente calcular el ángulo entre un vector (el pibardo) y otro vector. Ese
    segundo vector es un elemento de los chorrocientos que tenga el sistema (una lista de eventos).
    Se hace en R^3, si quisieramos "complicarlo" sería meter más dimensiones.
    Ese valor de 0,95 es nuestra tolerancia, cuando haces el producto escalar de dos vectores que sea 1 te dice que son l.d.
    Que sean l.d en este caso quiere decir que son compatibles, pero como es muy difícil clavarlo le doy cierta tolerancia.
*/
    private static final double TOLERANCE = 0.95;

    public List<Event> recommendEvents(ClientRecommendationService clientService,
                                    ServerRecommendationService serverService,
                                    int topN, //top N = number of elements you want to show
                                    boolean applyTolerance) {

        double[] clientVector = clientService.getClientVector();
        List<Event> events = serverService.getEvents();
        List<double[]> eventVectors = serverService.getEventVectors();

        //Calculate the similitudes list
        List<Double> similarities = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            similarities.add(cosineSimilarity(clientVector, eventVectors.get(i)));
        }

        QuickSort sorted = new QuickSort(events, similarities);
        List<Event> sortedEvents = sorted.getSortedEvents();
        List<Double> sortedSimilarities = sorted.getSortedSimilarities();

        // TOLERANCE is used here
        List<Event> recommended = new ArrayList<>();
        for (int i = 0; i < sortedEvents.size() && recommended.size() < topN; i++) {
            if ((!applyTolerance) || (sortedSimilarities.get(i) >= TOLERANCE)) {
                recommended.add(sortedEvents.get(i));
            }
        }
        return recommended;
    }
/**
 * Calcula la similitud entre dos vectores (usando al coseno).
 * ¿por qué coseno?
 * Nos interesa comparar la dirección de los vectores, es decir, la proporción entre sus componentes,
 * no su magnitud absoluta. Esto permite detectar clientes con patrones de preferencias similares
 * aunque paguen distinto o tengan edades distintas.
 *   similitud(v1, v2) = (v1 · v2) / (||v1|| * ||v2||)
 * donde:
 *   v1 · v2 = sumatorio de v1[i] * v2[i]  (producto escalar)
 *   ||v1|| = sqrt(sumatorio de v1[i]^2)   (norma Euclídea)
 *   ||v2|| = sqrt(sumatorio de v2[i]^2)
 * El resultado está en [-1, 1], donde:
 *   1  → vectores perfectamente alineados (máxima similitud)
 *   0  → vectores ortogonales (sin relación)
 *  -1  → vectores opuestos
 */
    private double cosineSimilarity(double[] v1, double[] v2) {
        double escalarProduct = 0.0;
        double normV1 = 0.0;
        double normV2 = 0.0;

        for (int i = 0; i < v1.length; i++) {
            escalarProduct += v1[i] * v2[i];
            normV1 += v1[i] * v1[i];
            normV2 += v2[i] * v2[i];
        }

        return escalarProduct / (Math.sqrt(normV1) * Math.sqrt(normV2));
    }
}