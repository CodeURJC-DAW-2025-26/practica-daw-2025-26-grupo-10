import type { AdminDashboardDTO, AdminStatisticsDTO } from "~/models/Admin";
import type { User } from "~/models/User";
import { API_URL } from "./homeService";

export const API_BASE = `${API_URL}/admin`;

export async function getDashboard(): Promise<AdminDashboardDTO> {
    const response = await fetch(`${API_BASE}/dashboard`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) throw new Error("Error al obtener datos del dashboard");
    return response.json();
}

export async function getStatistics(): Promise<AdminStatisticsDTO> {
    const response = await fetch(`${API_BASE}/statistics`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) throw new Error("Error al obtener estadísticas");
    return response.json();
}

export async function getUsers(page: number = 0, size: number = 5): Promise<User[]> {
    const response = await fetch(`${API_BASE}/users?page=${page}&size=${size}`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });

    if (!response.ok) throw new Error("Error al obtener la lista de usuarios");

    return response.json();
}

export async function getUserById(id: string): Promise<User> {
    const response = await fetch(`${API_BASE}/users/${id}`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) throw new Error("Error al obtener el usuario");
    return response.json();
}

export async function updateUser(id: number, userData: User): Promise<User> {
    const response = await fetch(`${API_BASE}/users/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(userData),
    });

    if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || "Error al actualizar");
    }
    return response.json();
}
