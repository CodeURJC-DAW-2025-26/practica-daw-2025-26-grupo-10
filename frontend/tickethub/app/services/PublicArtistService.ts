import type { ArtistBasic } from "~/models/ArtistBasic";
import type { Artist } from "~/models/Artist";
import { API_URL } from "./homeService";

interface PagedArtists {
    content: ArtistBasic[];
    last: boolean;
}

export const publicArtistService = {
    getAllArtists: async(): Promise<PagedArtists> => {
        const res = await fetch(`${API_URL}/public/artists?page=0&size=5&name=`);
        if (!res.ok) throw new Error("Error al obtener los artistas");
        const data = await res.json();
        return { content: data.content, last: data.last };
    },

    getArtistById: async(id: string): Promise<Artist> => {
        const res = await fetch(`${API_URL}/public/artists/${id}`);
        if (!res.ok) throw new Error("Error al obtener el artista");
        return await res.json();
    }
}