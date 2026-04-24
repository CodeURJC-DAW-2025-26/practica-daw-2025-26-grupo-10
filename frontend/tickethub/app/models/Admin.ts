export interface MonthEventData {
    0: string; // Month
    1: string; // Event
    2: number; // Data
}

export interface AdminDashboardDTO {
    activeEvents: number;
    numberTickets: number;
    numberUsers: number;
    numberAdmins: number;
}

export interface AdminStatisticsDTO {
    monthEventData: MonthEventData[];
    rankingLabels: string[];
    rankingValues: number[];
    evolutionLabels: string[];
    evolutionValues: number[];
}