import { create } from "zustand";
import axios from "axios";

export interface PublicArtist {
    artistID: number;
    artistName: string;
}

interface PublicArtistsState {
    artists: PublicArtist[];
    search: string;
    page: number;
    hasMore: boolean;
    loading: boolean;
    reset: (initial: PublicArtist[], isLast: boolean) => void;
    setSearch: (query: string) => void;
    fetchBySearch: (query: string) => Promise<void>;
    loadMore: () => Promise<void>;
}

export const usePublicArtistsStore = create<PublicArtistsState>((set, get) => ({
    artists: [],
    search: "",
    page: 0,
    hasMore: true,
    loading: false,

    // for setting the store with the initial data from the loader
    reset: (initial, isLast) =>
        set({ artists: initial, page: 0, hasMore: !isLast, search: "" }),

    setSearch: (query) => set({ search: query }),

    fetchBySearch: async (query) => {
        set({ loading: true });
        try {
            const res = await axios.get("/api/v1/public/artists", {
                params: { page: 0, size: 5, name: query },
            });
            set({ artists: res.data.content, page: 0, hasMore: !res.data.last });
        } finally {
            set({ loading: false });
        }
    },

    loadMore: async () => {
        const { loading, page, search } = get();
        if (loading) return;
        set({ loading: true });
        try {
            const nextPage = page + 1;
            const res = await axios.get("/api/v1/public/artists", {
                params: { page: nextPage, size: 5, name: search },
            });
            set((state) => ({
                artists: [...state.artists, ...res.data.content],
                page: nextPage,
                hasMore: !res.data.last,
            }));
        } finally {
            set({ loading: false });
        }
    },
}));