import type { Config } from "@react-router/dev/config";

export default {
  ssr: false,
  basename: (process.env.BASE_PATH ?? "/").replace(/\/$/, "") || "/",
} satisfies Config;
