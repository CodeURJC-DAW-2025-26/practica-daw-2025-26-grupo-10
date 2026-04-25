import type { UserBasic } from "~/models/UserBasic";
import { API_URL } from "./homeService";
import type { AuthResponse } from "~/models/AuthResponse";

export async function login(email: string, password: string): Promise<AuthResponse> {

    const response = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        try {
            const errorData = await response.json();
            throw new Error(errorData.message || errorData.error || "Error en la petición");
        } catch (e) {
            throw new Error(e instanceof Error ? e.message : `Error del servidor: ${response.status}`);
        }
    }

    const data: AuthResponse = await response.json();
    return data;
}

export async function me(): Promise<UserBasic> {
    const response = await fetch(`${API_URL}/users/me`, {
        method: "GET",
        credentials: "include"
    });
    if (response.status === 200) {
        const data = await response.json();
        return data as UserBasic;
    }

    throw new Error(response.statusText);
}

export async function signup(name: string, surname: string, username: string, email: string, password: string, passwordConfirmation: string): Promise<AuthResponse> {

    const response = await fetch(`${API_URL}/auth/signup`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, surname, username, email, password, passwordConfirmation })
    });

    if (!response.ok) {
        try {
            const errorData = await response.json();
            throw new Error(errorData.message || errorData.error || "Error en la petición");
        } catch (e) {
            throw new Error(e instanceof Error ? e.message : `Error del servidor: ${response.status}`);
        }
    }

    const data: AuthResponse = await response.json();
    return data;
}

export async function logout(): Promise<void> {
    const response = await fetch(`${API_URL}/auth/logout`, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" }
    })

    if (!response.ok) {
        throw new Error(response.statusText);
    }
}

