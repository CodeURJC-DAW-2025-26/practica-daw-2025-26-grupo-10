import type Zone from "~/models/Zone";
import type ZoneCreate from "~/models/ZoneCreate";
import { API_BASE } from "./adminService";

const EVENTS_URL = `${API_BASE}/events`;

export async function getZones(eventId: string): Promise<Zone[]> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones`);
  if (!res.ok) throw new Error("Error al obtener zonas");
  return await res.json();
}

export async function getZone(eventId: string, zoneId: string): Promise<Zone> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones/${zoneId}`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) throw new Error("Zona no encontrada");
  return await res.json();
}

export async function createZone(eventId: string, data: ZoneCreate): Promise<Zone> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear zona");
  return await res.json();
}

export async function updateZone(eventId: string, zoneId: string, data: ZoneCreate): Promise<Zone> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones/${zoneId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar zona");
  return await res.json();
}

export async function deleteZone(eventId: string, zoneId: number): Promise<void> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones/${zoneId}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) throw new Error("Error al eliminar zona");
}
