import type { EventPublic } from "~/models/Event";

const API_URL = "/api/v1/public/events";

export async function getPublicEvent(id: string): Promise<EventPublic> {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Evento no encontrado");
  return await res.json();
}
