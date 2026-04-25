import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { Artist } from "~/models/Artist";
import { API_BASE } from "./adminService";

export const adminArtistService = {
    getAllArtists: async() : Promise<Artist[]> => {
        const res = await fetch(`${API_BASE}/artists?page=0&size=5&name=`);
        if (!res.ok) throw new Error("Error al obtener los artistas");
        const data = await res.json();
        return data.content as Artist[];
    },

    getArtistById: async(id: string): Promise<ArtistCreateUpdate> => {
        const res = await fetch(`${API_BASE}/artists/${id}`);
        if (!res.ok) throw new Error("Error al obtener el artista");
        const data = await res.json();
        return data.content as ArtistCreateUpdate;
    },
    
    createArtist: async(artist: ArtistCreateUpdate, image: File | null) => {
        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
        if (image) formData.append("image", image);

        const res = await fetch (`${API_BASE}/artists`, {
            method: "POST",
            body: formData,
        });

        if (!res.ok) throw new Error("Error al crear el artista");
        const data = await res.json();
        return data.content as ArtistCreateUpdate;
    },
    
    updateArtist: async(id: string, artist: ArtistCreateUpdate, image: File | null) => {
        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
        if (image) formData.append("image", image);

        const res = await fetch(`${API_BASE}/artists/${id}`, {
            method: "PUT",
            body: formData,
        });

        if (!res.ok) throw new Error("Error al actualizar el artista");
        const data = await res.json();
        return data.content as ArtistCreateUpdate;
    }
}
