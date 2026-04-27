import { create } from "zustand";
import type { Artist } from "~/models/Artist";

interface AdminArtistsState {
    artists: Artist[];
    reset: (artists: Artist[]) => void;
    deleteArtist: (id: number) => Promise<void>;
}

export const useAdminArtistsStore = create<AdminArtistsState>((set) => ({
    artists: [],

    // for setting the store with the initial data from the loader
    reset: (artists) => set({ artists }),

    deleteArtist: async (id) => {
        const res = await fetch(`/api/v1/admin/artists/${id}`, { method: "DELETE" });
        if (!res.ok) throw new Error("Error al eliminar el artista");
        set((state) => ({
            artists: state.artists.filter((a) => a.artistID !== id),
        }));
    },
}));