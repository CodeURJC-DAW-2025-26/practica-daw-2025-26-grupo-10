package es.tickethub.tickethub.dto;

import java.util.List;

public record AdminStatisticsDTO(
    List<Object[]> monthEventData,
    List<String> rankingLabels,
    List<Number> rankingValues,
    List<String> evolutionLabels,
    List<Number> evolutionValues
) {}