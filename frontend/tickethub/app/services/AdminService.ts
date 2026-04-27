import type { AdminDashboardDTO, AdminStatisticsDTO } from "~/models/Admin";
import type { User } from "~/models/User";
import { API_URL } from "./homeService";

export const API_BASE = `${API_URL}/admin`;

export async function getDashboard(): Promise<AdminDashboardDTO> {
    const response = await fetch(`${API_BASE}/dashboard`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) {
        throw new Response(null, { status: response.status });
    }
    return response.json();
}

export async function getStatistics(): Promise<AdminStatisticsDTO> {
    const response = await fetch(`${API_BASE}/statistics`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) {
        throw new Response(null, { status: response.status });
    }
    return response.json();
}

export async function getUsers(page: number = 0, size: number = 5): Promise<User[]> {
    const response = await fetch(`${API_BASE}/users?page=${page}&size=${size}`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });

    if (!response.ok) {
        throw new Response(null, { status: response.status });
    }

    return response.json();
}

export async function getUserById(id: string): Promise<User> {
    const response = await fetch(`${API_BASE}/users/${id}`, {
        headers: { "Content-Type": "application/json" },
        credentials: "include"
    });
    if (!response.ok) {
        throw new Response(null, { status: response.status });
    }
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
        throw new Response(null, { status: response.status });
    }
    return response.json();
}
