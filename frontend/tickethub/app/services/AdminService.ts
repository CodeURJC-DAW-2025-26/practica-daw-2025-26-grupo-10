import type { AdminStatisticsDTO } from "~/models/AdminStatistics";

export const API_URL_ADMIN = "/api/v1/admin";

export const adminService = {
    getStatistics: async(): Promise<AdminStatisticsDTO> => {
        const res = await fetch(`${API_URL_ADMIN}/statistics`);
        if (!res.ok) throw new Error("Error al obtener las estadísticas");
        return await res.json();
    },
};