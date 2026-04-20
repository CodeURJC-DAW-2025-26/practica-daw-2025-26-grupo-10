const BASE_URL = "/api/v1/admin/events";

export interface Zone {
  id: number;
  name: string;
  capacity: number;
  price: number;
  eventId: number;
  selected: boolean;
}

export interface ZoneCreateDTO {
  name: string;
  capacity: number;
  price: number;
}

function url(eventId: string, zoneId?: string): string {
  const base = `${BASE_URL}/${eventId}/zones`;
  return zoneId ? `${base}/${zoneId}` : base;
}

export async function getZones(eventId: string): Promise<Zone[]> {
  const res = await fetch(url(eventId));
  if (!res.ok) throw new Error("Error al obtener zonas");
  return res.json();
}

export async function getZone(eventId: string, zoneId: string): Promise<Zone> {
  const res = await fetch(url(eventId, zoneId));
  if (!res.ok) throw new Error("Zona no encontrada");
  return res.json();
}

export async function createZone(eventId: string, data: ZoneCreateDTO): Promise<Zone> {
  const res = await fetch(url(eventId), {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al crear zona");
  return res.json();
}

export async function updateZone(eventId: string, zoneId: string, data: ZoneCreateDTO): Promise<Zone> {
  const res = await fetch(url(eventId, zoneId), {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Error al actualizar zona");
  return res.json();
}

export async function deleteZone(eventId: string, zoneId: number): Promise<void> {
  const res = await fetch(url(eventId, String(zoneId)), { method: "DELETE" });
  if (!res.ok) throw new Error("Error al eliminar zona");
}
