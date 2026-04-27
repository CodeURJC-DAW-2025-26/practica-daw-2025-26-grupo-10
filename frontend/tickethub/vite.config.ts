import { reactRouter } from "@react-router/dev/vite";
import { defineConfig } from "vite";
import tsconfigPaths from "vite-tsconfig-paths";

export default defineConfig(({ mode }) => ({
  base: mode === "production" ? "/new/" : "/",
  plugins: [reactRouter(), tsconfigPaths()],
  server: {
    proxy: {
      '/api': {
        target: 'https://localhost:8443',
        changeOrigin: true,
        secure: false,
      },
      '/images': {
        target: 'https://localhost:8443',
        changeOrigin: true,
        secure: false,
      }
    }
  }
}));