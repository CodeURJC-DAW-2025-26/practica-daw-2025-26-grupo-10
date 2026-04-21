import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("layout/MainLayout.tsx", [
        index("routes/public/home.tsx"),
        route("/public/events", "routes/public/events.tsx"),
        route("/admin/events", "routes/admin/manage-events.tsx"),
        route("/public/events/:id", "routes/public/event.tsx"),
        route("/admin/events/create", "routes/admin/create-event.tsx"),
        route("/admin/events/edit/:id", "routes/admin/edit-event.tsx")
    ]),
] satisfies RouteConfig;
