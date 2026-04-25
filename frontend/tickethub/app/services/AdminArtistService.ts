import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { Artist } from "~/models/Artist";
import { API_BASE, getHeaders } from "./adminService";

export async function getAllArtists(): Promise<Artist[]> {
    const res = await fetch(`${API_BASE}/artists?page=0&size=1000&name=`, {
        headers: getHeaders() 
    });
    if (!res.ok) throw new Error("Error al obtener los artistas");
    const data = await res.json();
    return data.content || [];
}

export async function getArtistById(id: string): Promise<ArtistCreateUpdate> {
    const res = await fetch(`${API_BASE}/artists/${id}`, {
        headers: getHeaders()
    });
    if (!res.ok) throw new Error("Error al obtener el artista");
    return await res.json();
}

export async function createArtist(artist: ArtistCreateUpdate, image: File | null): Promise<Artist> {
    const formData = new FormData();

    const headers = getHeaders();
    delete (headers as any)["Content-Type"];

    formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
    if (image) formData.append("image", image);

    const res = await fetch(`${API_BASE}/artists`, {
        method: "POST",
        headers: headers,
        body: formData,
    });

    if (!res.ok) throw new Error("Error al crear el artista");
    return await res.json();
}

export async function updateArtist(id: string, artist: ArtistCreateUpdate, image: File | null): Promise<Artist> {
    const formData = new FormData();
    

    const headers = getHeaders();
    

    delete (headers as any)["Content-Type"];

    formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
    
    if (image) formData.append("image", image);

    const res = await fetch(`${API_BASE}/artists/${id}`, {
        method: "PUT",
        headers: headers,
        body: formData,
    });

    if (!res.ok) {
        const errorText = await res.text();
        console.error("Error en el servidor:", errorText);
        throw new Error("Error al actualizar el artista");
    }

    return await res.json();
}