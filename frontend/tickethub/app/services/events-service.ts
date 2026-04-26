import type { EventBasic } from "~/models/EventBasic";
import { API_URL } from "./homeService";

export async function getEventsPublic(
  page: number,
  size: number,
  artist: string,
  category: string,
  date: string
): Promise<EventBasic[]> {
  const params = new URLSearchParams();
  params.set("page", page.toString());
  params.set("size", size.toString());
  if (artist) params.set("artist", artist);
  if (category) params.set("category", category);
  if (date) params.set("date", date);
 
  const res = await fetch(`${API_URL}/public/events?${params.toString()}`);
  if (!res.ok) throw new Error("Error cargando eventos");
  const data = await res.json()
  return data.content;
}

export async function getEventsAdmin(): Promise<EventBasic[]> {
  const res = await fetch(`${API_URL}/admin/events`, {
    credentials: "include"
  });
  if (!res.ok) throw new Error("Error cargando eventos");
  const data = await res.json()
  return data.content;
}
 
export async function getCategories(): Promise<string[]> {
  const res = await fetch(`${API_URL}/public/events/categories`);
  if (!res.ok) throw new Error("Error cargando categorías");
  return await res.json();
}

//Functions to the manage-events.tsx
export async function deleteEvent(eventID: number): Promise<void> {
  const res = await fetch(`${API_URL}/admin/events/${eventID}`, {
    method: "DELETE",
    credentials: "include"
  });
  if (!res.ok) throw new Error("Error eliminando el evento");
}
