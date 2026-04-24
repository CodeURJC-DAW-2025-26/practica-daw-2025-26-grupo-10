import { create } from "zustand";
import axios from "axios";
import type { Artist } from "~/models/Artist";

interface AdminArtistsState {
    artists: Artist[];
    reset: (artists: Artist[]) => void;
    deleteArtist: (id: number) => Promise<void>;
}

export const useAdminArtistsStore = create<AdminArtistsState>((set, get) => ({
    artists: [],

    // for setting the store with the initial data from the loader
    reset: (artists) => set({ artists }),

    deleteArtist: async (id) => {
        await axios.delete(`/api/v1/admin/artists/${id}`);
        set((state) => ({
            artists: state.artists.filter((a) => a.artistID !== id),
        }));
    },
}));