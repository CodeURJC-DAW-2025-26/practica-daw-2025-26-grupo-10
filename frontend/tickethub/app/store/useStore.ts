import { create } from 'zustand';

interface AuthState {
    user: any | null; // Ese any habrá que cambiarlo por la interfaz de usuario, es un truño
    isAuthenticated: boolean;
    eventsSearch: string;
    setEventsSearch: (q: string) => void;
    setUser: (user: any) => void;
    logout: () => Promise<void>;
}

export const useStore = create<AuthState>((set) => ({
    user: null,
    isAuthenticated: false,
    setUser: (user) => set({ user, isAuthenticated: !!user }),
    eventsSearch: "",

    setEventsSearch: (q) => set({eventsSearch: q}),

    logout: async () => {
        try {
            await fetch('/api/v1/auth/logout', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }
            });
        } catch (error) {
            console.error("Error al cerrar sesión en el servidor", error);
        } finally {
            set({ user: null, isAuthenticated: false });
        }
    },
}));