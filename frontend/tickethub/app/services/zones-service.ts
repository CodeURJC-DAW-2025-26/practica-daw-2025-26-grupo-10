import type Zone from "~/models/Zone";
import type ZoneCreate from "~/models/ZoneCreate";
import { API_URL } from "./homeService";

const EVENTS_URL = `${API_URL}/admin/events`;

export async function getZones(eventId: string): Promise<Zone[]> {
  const res = await fetch(`${API_URL}/public/events/${eventId}/zones`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) {
    throw new Response(null, { status: res.status });
  }
  return await res.json();
}

export async function getZone(eventId: string, zoneId: string): Promise<Zone> {
  const res = await fetch(`${API_URL}/public/events/${eventId}/zones/${zoneId}`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) {
    throw new Response(null, { status: res.status });
  }
  return await res.json();
}

export async function createZone(eventId: string, data: ZoneCreate): Promise<Zone> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });
  if (!res.ok) {
    throw new Response(null, { status: res.status });
  }
  return await res.json();
}

export async function updateZone(eventId: string, zoneId: string, data: ZoneCreate): Promise<Zone> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones/${zoneId}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(data),
  });
  if (!res.ok) {
    throw new Response(null, { status: res.status });
  }
  return await res.json();
}

export async function deleteZone(eventId: string, zoneId: number): Promise<void> {
  const res = await fetch(`${EVENTS_URL}/${eventId}/zones/${zoneId}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) {
    throw new Response(null, { status: res.status });
  }
}
