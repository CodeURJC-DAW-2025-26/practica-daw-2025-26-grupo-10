import { API_URL } from "./homeService";
import type { SessionBasic } from "~/models/SessionBasic";

export async function getSessionsByEvent(eventID: string): Promise<SessionBasic[]> {
    const res = await fetch(`${API_URL}/public/sessions/event/${eventID}`);
    if (!res.ok) throw new Error("Error cargando las sesiones");
    return res.json();
}

export async function createSession(eventID: string, dateStr: string): Promise<SessionBasic> {
    const res = await fetch(`${API_URL}/admin/events/${eventID}/sessions`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
        body: JSON.stringify({ dateStr }),
    });
    if (!res.ok) throw new Error("Error creando la sesión");
    return res.json();
}

export async function updateSession(eventID: string, index: number, dateStr: string): Promise<SessionBasic> {
    const res = await fetch(`${API_URL}/admin/events/${eventID}/sessions/${index}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        credentials: "include",
        body: JSON.stringify({ dateStr }),
    });
    if (!res.ok) throw new Error("Error actualizando la sesión");
    return res.json();
}

export async function deleteSession(sessionID: number): Promise<void> {
    const res = await fetch(`${API_URL}/admin/sessions/${sessionID}`, {
        method: "DELETE",
        headers: {"Content-Type": "application/json"},
        credentials: "include"
    });
    if (!res.ok) throw new Error("Error eliminando la sesión");
}