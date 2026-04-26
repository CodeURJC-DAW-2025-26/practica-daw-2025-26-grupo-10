import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { Artist } from "~/models/Artist";
import { API_BASE } from "./adminService";

export async function getAllArtists(): Promise<Artist[]> {
    const res = await fetch(`${API_BASE}/artists?page=0&size=1000&name=`, {
        headers: {"Content-Type": "application/json"},
        credentials: "include"
    });
    if (!res.ok) throw new Error("Error al obtener los artistas");
    const data = await res.json();
    return data.content || [];
}

export async function getArtistById(id: string): Promise<ArtistCreateUpdate> {
    const res = await fetch(`${API_BASE}/artists/${id}`, {
        headers: {"Content-Type": "application/json"},
        credentials: "include"
    });
    if (!res.ok) throw new Error("Error al obtener el artista");
    return await res.json();
}

export async function createArtist(artist: ArtistCreateUpdate, image: File | null): Promise<Artist> {
  const res = await fetch(`${API_BASE}/artists`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(artist),
  });
  if (!res.ok) throw new Error("Error al crear el artista");
  const created = await res.json();

  if (image) {
    const form = new FormData();
    form.append("image", image);
    await fetch(`${API_BASE}/artists/${created.artistID}/image`, {
      method: "POST",
      credentials: "include",
      body: form,
    });
  }

  return created;
}

export async function updateArtist(id: string, artist: ArtistCreateUpdate, image: File | null): Promise<Artist> {
  const res = await fetch(`${API_BASE}/artists/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify(artist),
  });
  if (!res.ok) throw new Error("Error al actualizar el artista");
  const updated = await res.json();

  if (image) {
    const form = new FormData();
    form.append("image", image);
    await fetch(`${API_BASE}/artists/${id}/image`, {
      method: "PUT",
      credentials: "include",
      body: form,
    });
  }

  return updated;
}