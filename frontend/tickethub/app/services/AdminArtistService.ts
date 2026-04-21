import axios from "axios";
import type { ArtistCreateUpdate } from "~/models/ArtistCreateUpdate";
import type { Artist } from "~/models/Artist";
import { API_URL_ADMIN } from "./AdminService";

export const adminArtistService = {
    getAllArtists: async() : Promise<Artist[]> => {
        const res = await axios.get(`${API_URL_ADMIN}/artists`, {
            params: { page: 0, size: 5, name: "" },
        });
        return res.data.content as Artist[];
    },

    getArtistById: async(id: string): Promise<ArtistCreateUpdate> => {
        const res = await axios.get(`${API_URL_ADMIN}/artists/${id}`);
        return res.data.content as ArtistCreateUpdate;
    },
    
    createArtist: async(artist: ArtistCreateUpdate, image: File | null) => {
        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
        if (image) formData.append("image", image);

        return axios.post(`${API_URL_ADMIN}/artists`, formData, {headers: { "Content-Type": "multipart/form-data" }})
    },
    
    updateArtist: async(id: string, artist: ArtistCreateUpdate, image: File | null) => {
        const formData = new FormData();
        formData.append("data", new Blob([JSON.stringify(artist)], { type: "application/json" }));
        if (image) formData.append("image", image);

        return axios.put(`${API_URL_ADMIN}/artists/${id}`, formData, {headers: { "Content-Type": "multipart/form-data" }})
    }
}
