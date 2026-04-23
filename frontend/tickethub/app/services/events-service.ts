import { API_URL } from "~/services/homeService";
import type Event from "~/models/Event";

const EVENTS_PUBLIC_URL = `${API_URL}/public/events`;

export async function getPublicEvent(id: string): Promise<Event> {
  const res = await fetch(`${EVENTS_PUBLIC_URL}/${id}`);
  if (!res.ok) throw new Error("Evento no encontrado");
  return await res.json();
}
