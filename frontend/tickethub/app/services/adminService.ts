import type { AdminDashboardDTO, AdminStatisticsDTO } from "~/models/Admin";

const API_BASE = "/api/v1/admin";



const getHeaders = () => ({
    "Authorization": `Bearer ${localStorage.getItem("token")}`,
    "Content-Type": "application/json",
});

export async function getDashboard(): Promise<AdminDashboardDTO> {
    const response = await fetch(`${API_BASE}/dashboard`, { headers: getHeaders() });
    if (!response.ok) throw new Error("Error al obtener datos del dashboard");
    return response.json();
}

export async function getStatistics(): Promise<AdminStatisticsDTO> {
    const response = await fetch(`${API_BASE}/statistics`, { headers: getHeaders() });
    if (!response.ok) throw new Error("Error al obtener estadísticas");
    return response.json();
}