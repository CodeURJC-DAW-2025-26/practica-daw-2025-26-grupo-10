import { reactRouter } from "@react-router/dev/vite";
import { defineConfig } from "vite";
import tsconfigPaths from "vite-tsconfig-paths";

export default defineConfig({
  plugins: [reactRouter(), tsconfigPaths()],
  base: process.env.BASE_PATH ?? "/",
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
});
