export interface AdminStatisticsDTO {
    monthEventData: [string, string, number][]; // month, event, quantity
    rankingLabels: string[];
    rankingValues: number[];
    evolutionLabels: string[];
    evolutionValues: number[];
}