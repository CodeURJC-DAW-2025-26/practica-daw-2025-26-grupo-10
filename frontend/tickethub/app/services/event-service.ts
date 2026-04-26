import type { ArtistBasic } from "~/models/ArtistBasic";
import { API_URL } from "./homeService";
import type { Event } from "~/models/Event";
import type { EventUpdate } from "~/models/EventUpdate";
import type { EventCreate } from "~/models/EventCreate";

export async function getArtists(): Promise<ArtistBasic[]> {
  const res = await fetch(`${API_URL}/public/artists?page=0&size=1000`);
  if (!res.ok) throw new Error("Error cargando artistas");
  
  const data = await res.json();

  return data.content || (Array.isArray(data) ? data : []);
}

export async function createEvent(formData: EventCreate): Promise<Event> {
  const res = await fetch(`${API_URL}/admin/events`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(formData),
  });
  if (!res.ok) throw new Error("Error creando el evento");
  return await res.json();
}
 
export async function updateEvent(eventID: number, formData: EventUpdate): Promise<Event> {
  const res = await fetch(`${API_URL}/admin/events/${eventID}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(formData),
  });
  if (!res.ok) throw new Error("Error actualizando el evento");
  return await res.json();
}

//Function to the event.tsx file
export async function getEvent(id: string): Promise<Event> {
  const res = await fetch(`${API_URL}/public/events/${id}`);
  if (!res.ok) throw new Error("Evento no encontrado");
  return await res.json();
}

export async function uploadEventImage(eventID: number, image: File): Promise<void> {
  const form = new FormData();
  form.append("image", image);

  const res = await fetch(`${API_URL}/admin/events/${eventID}/images`, {
    method: "POST",
    credentials: "include",
    body: form,
  });

  if (!res.ok) throw new Error("Error subiendo imagen");
}