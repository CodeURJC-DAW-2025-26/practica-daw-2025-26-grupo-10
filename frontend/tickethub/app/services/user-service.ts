import { API_URL } from "./homeService"
import type { PurchaseBasic } from "../models/PurchaseBasic";
import type { ChangePasswordBasic, ChangeProfileBasic } from "../models/UserBasic";
import type { AuthResponse } from "../models/AuthResponse"
export interface PurchasePaginatedResponse {
    content: PurchaseBasic[];
    hasNext: boolean;
}

export async function getPurchases(page: number = 0): Promise<PurchasePaginatedResponse> {

    const response = await fetch(`${API_URL}/public/purchases/me?page=${page}`, {
        method: "GET",
        credentials: "include"
    });

    if (response.ok) {
        const data = await response.json();
        return data as PurchasePaginatedResponse;
    }

    throw new Error("Error al obtener el historial de compras");
}

export async function changePassword(changePasswordBasic: ChangePasswordBasic): Promise<string> {
    const response = await fetch(`${API_URL}/clients/me/password`, {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(changePasswordBasic)
    });

    if (!response.ok) {
        const errorData: AuthResponse = await response.json();
        throw new Error(errorData.message || "Error al cambiar la contraseña");
    }

    return "Contraseña actualizada correctamente";
}

export async function changeProfile(changeProfileBasic: ChangeProfileBasic): Promise<string> {
    const response = await fetch(`${API_URL}/clients/me`, {
        method: "PUT",
        credentials: "include",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(changeProfileBasic)
    });

    if (!response.ok) {
        const errorData: AuthResponse = await response.json();
        throw new Error(errorData.message || "Error al actualizar el perfil");
    }

    return "Perfil actualizado correctamente";
}

export async function getProfileFormInformation(): Promise<ChangeProfileBasic> {

    const response = await fetch(`${API_URL}/clients/me`, {
        method: "GET",
        credentials: "include"
    });

    if (response.ok) {
        const data = await response.json();
        return data as ChangeProfileBasic;
    }

    throw new Error("Error al obtener la información del perfil");
}

export async function changeProfileImage(userID: number, imageFile: File): Promise<void> {
    const formData = new FormData();
    formData.append("image", imageFile);
    const response = await fetch(`${API_URL}/users/${userID}/image`, {
        method: "PUT",
        credentials: "include",

        body: formData
    });
    if (!response.ok) {
        throw new Error("Error al subir la imagen de perfil");
    }
}