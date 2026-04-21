import axios from "axios";
import type { ArtistBasic } from "~/models/ArtistBasic";
import type { Artist } from "~/models/Artist";
import { API_URL } from "./homeService";

interface PagedArtists {
    content: ArtistBasic[];
    last: boolean;
}

export const publicArtistService = {
    getAllArtists: async() : Promise<PagedArtists> => {
        const res = await axios.get(`${API_URL}/public/artists`, {
            params: { page: 0, size: 5, name: "" },
        });

        return { content: res.data.content, last: res.data.last };
    },

    getArtistById: async(id: string) : Promise<Artist> => {
        const res = await axios.get(`${API_URL}/public/artists/${id}`);

        return res.data as Artist;
    }
}