import axios from "axios";
import type { AdminStatisticsDTO } from "~/models/AdminStatistics";

export const API_URL_ADMIN = "/api/v1/admin";

export const adminService = {
    getStatistics: async (): Promise<AdminStatisticsDTO> => {
        const res = await axios.get(`${API_URL_ADMIN}/statistics`);
        return res.data as AdminStatisticsDTO;
    },
};