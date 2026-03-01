package es.tickethub.tickethub.services;

import java.util.List;
import es.tickethub.tickethub.entities.Event;

public class QuickSort {

    private final List<Event> events;
    private final List<Double> similarities;

    public QuickSort(List<Event> events, List<Double> similarities) {
        this.events = events;
        this.similarities = similarities;
        // this comprobation is just in case, I think it could be erased
        if (events != null && similarities != null && !events.isEmpty() && !similarities.isEmpty()) {
            quickSortEvents(0, events.size() - 1);
        }
    }

    public List<Event> getSortedEvents() {
        return events;
    }

    public List<Double> getSortedSimilarities() {
        return similarities;
    }

    private void quickSortEvents(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSortEvents(low, pi - 1);
            quickSortEvents(pi + 1, high);
        }
    }

    private int partition(int low, int high) {
        double pivot = similarities.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (similarities.get(j) >= pivot) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        Event tmpEvent = events.get(i);
        events.set(i, events.get(j));
        events.set(j, tmpEvent);

        double tmpSim = similarities.get(i);
        similarities.set(i, similarities.get(j));
        similarities.set(j, tmpSim);
    }
}