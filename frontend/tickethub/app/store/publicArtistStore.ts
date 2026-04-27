/**  This store exists for three reasons:
/* 1. Separation of concerns: the UI component only worries about rendering,
/*    while the store handles data and logic. If the search behavior changes,
/*    only the store needs to be updated.
/* 2. Centralized complex logic: search + pagination + loading state all interact
/*    with each other. When the user searches, the page resets to 0. When loading
/*    more, it remembers the current search query. The store manages all of this cleanly.
/* 3. Data accumulation: "load more" needs to remember the previous artists to append
/*    the new ones. The store keeps this state alive across re-renders.*/

import { create } from "zustand";
import type { ArtistBasic } from "~/models/ArtistBasic";

interface PublicArtistsState {
    artists: ArtistBasic[];
    search: string;
    page: number;
    hasMore: boolean;
    loading: boolean;
    reset: (initial: ArtistBasic[], isLast: boolean) => void;
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

    reset: (initial, isLast) =>
        set({ artists: initial, page: 0, hasMore: !isLast, search: "" }),

    setSearch: (query) => set({ search: query }),

    fetchBySearch: async (query) => {
        set({ loading: true });
        try {
            const res = await fetch(`/api/v1/public/artists?page=0&size=5&name=${encodeURIComponent(query)}`);
            if (!res.ok) throw new Error("Error al buscar artistas");
            const data = await res.json();
            const isLast = data.page.number >= data.page.totalPages - 1;

            set({ artists: data.content, page: 0, hasMore: !isLast });
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
            const res = await fetch(`/api/v1/public/artists?page=${nextPage}&size=5&name=${encodeURIComponent(search)}`);
            if (!res.ok) throw new Error("Error al cargar más artistas");
            const data = await res.json();
            const isLast = data.page.number >= data.page.totalPages - 1;
            set((state) => ({
                artists: [...state.artists, ...data.content],
                page: nextPage,
                hasMore: !isLast,
            }));
        } finally {
            set({ loading: false });
        }
    },
}));