import { create } from 'zustand';
import { login, logout, me, signup } from '~/services/auth-service';
import type { User } from '~/models/User';

interface AuthState {
    user: User | null;
    error: string | null;
    isAuthenticated: boolean;
    eventsSearch: string;
    setEventsSearch: (q: string) => void;
    logout: () => Promise<void>;
    login: (email: string, password: string) => Promise<any>;
    signup: (name: string, surname: string, username: string, email: string, password: string, repeatPassword: string) => Promise<any>;
    refreshUser: () => Promise<void>;
}

export const useStore = create<AuthState>((set) => ({
    user: null,
    error: null,
    isAuthenticated: false,
    eventsSearch: "",

    setEventsSearch: (q) => set({ eventsSearch: q }),

    logout: async () => {
        set({ error: null });
        try {
            await logout();
            set({ user: null, isAuthenticated: false });
        } catch (error) {
            console.error("Error al cerrar sesión en el servidor", error);
        }
    },

    login: async (email: string, password: string) => {
        set({ error: null });
        try {
            await login(email, password)
            const usuario = await me();
            set({ user: usuario, isAuthenticated: true })

        } catch (error) {
            const mensaje = error instanceof Error ? error.message : "Error desconocido al iniciar sesión";
            set({ error: mensaje, isAuthenticated: false });
            throw error;
        }
    },

    signup: async (name: string, surname: string, username: string, email: string, password: string, passwordConfirmation: string) => {
        set({ error: null });
        try {
            await signup(name, surname, username, email, password, passwordConfirmation);
            const usuario = await me();
            set({ user: usuario, isAuthenticated: true })
        } catch (error) {
            const mensaje = error instanceof Error ? error.message : "Error desconocido al registrarse";
            set({ error: mensaje, isAuthenticated: false });
            throw error;
        }
    },

    refreshUser: async () => {
        try {
            const usuarioActualizado = await me();
            set({ user: usuarioActualizado });
        } catch (error) {
            console.error("Error al refrescar el usuario", error);
        }
    }

}));