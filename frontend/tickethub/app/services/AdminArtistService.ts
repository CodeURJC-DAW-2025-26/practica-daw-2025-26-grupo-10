import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { Artist } from "~/models/Artist";
import { API_BASE } from "./adminService";
import { API_URL } from "./homeService";

export async function getAllArtists(): Promise<Artist[]> {
  const res = await fetch(`${API_BASE}/artists?page=0&size=1000&name=`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) throw new Error("Error al obtener los artistas");
  const data = await res.json();
  return data.content || [];
}

export async function getArtistById(id: string): Promise<Artist> {
  const res = await fetch(`${API_URL}/public/artists/${id}`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include"
  });
  if (!res.ok) throw new Error("Error al obtener el artista");
  return await res.json();
}

export async function createArtist(artist: ArtistCreateUpdate, image: File | null = null): Promise<Artist> {
  const form = new FormData();
  form.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
  if (image) {
    form.append("image", image);
  }

  const res = await fetch(`${API_BASE}/artists`, {
    method: "POST",
    credentials: "include",
    body: form,
  });
  if (!res.ok) throw new Error("Error al crear el artista");
  const created = await res.json();
  return created;
}

export async function updateArtist(id: string, artist: ArtistCreateUpdate): Promise<Artist> {
  const res = await fetch(`${API_BASE}/artists/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(artist),
  });
  if (!res.ok) throw new Error("Error al actualizar el artista");
  const updated = await res.json();
  return updated;
}

export async function deleteArtistImage(artistID: number): Promise<void> {
  const res = await fetch(`${API_BASE}/artists/${artistID}/image`, {
    method: "DELETE",
    credentials: "include",
  });
  if (!res.ok) throw new Error("Error eliminando imagen del artista");
}

export async function postArtistImage(artistID: number, image: File): Promise<void> {
  const form = new FormData();
  form.append("image", image);

  const res = await fetch(`${API_BASE}/artists/${artistID}/image`, {
    method: "POST",
    credentials: "include",
    body: form
  });
  if (!res.ok) throw new Error("Error subiendo imagen del artista");
}
export async function updateArtistImage(artistID: number, image: File): Promise<void> {
  const form = new FormData();
  form.append("image", image);

  const res = await fetch(`${API_BASE}/artists/${artistID}/image`, {
    method: "PUT",
    credentials: "include",
    body: form,
  });
  if (!res.ok) throw new Error("Error actualizando imagen del artista");
}