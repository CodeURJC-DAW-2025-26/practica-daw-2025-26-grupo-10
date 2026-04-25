import type { AdminDashboardDTO, AdminStatisticsDTO } from "~/models/Admin";
import type { UserDTO } from "~/models/User";

export const API_BASE = "/api/v1/admin";



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

export async function getUsers(page: number = 0, size: number = 5): Promise<UserDTO[]> {
    const response = await fetch(`${API_BASE}/users?page=${page}&size=${size}`, {
        headers: getHeaders()
    });
    
    if (!response.ok) throw new Error("Error al obtener la lista de usuarios");
    
    return response.json();
}

export async function getUserById(id: string): Promise<UserDTO> {
    const response = await fetch(`${API_BASE}/users/${id}`, { headers: getHeaders() });
    if (!response.ok) throw new Error("Error al obtener el usuario");
    return response.json();
}

export async function updateUser(id: number, userData: UserDTO): Promise<UserDTO> {
    const response = await fetch(`${API_BASE}/users/${id}`, {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify(userData),
    });
    
    if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || "Error al actualizar");
    }
    return response.json();
}
