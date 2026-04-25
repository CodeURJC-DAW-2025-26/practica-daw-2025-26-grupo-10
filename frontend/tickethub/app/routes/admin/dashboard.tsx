import { Link } from "react-router";
import { useEffect, useState } from "react";
import { getDashboard } from "~/services/adminService";
import type { AdminDashboardDTO } from "~/models/Admin";

export default function AdminDashboard() {
    const [stats, setStats] = useState<AdminDashboardDTO | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        getDashboard()
            .then(setStats)
            .catch(err => setError(err.message));
    }, []);

    if (error) return <div className="container my-5 text-center text-danger">{error}</div>;
    if (!stats) return <div className="container my-5 text-center">Cargando panel...</div>;

    return (
        <div className="container my-5 flex-grow-1">
            <h2 className="mb-4">Panel de Administración</h2>
            
            <div className="row g-4 mb-4">
                <StatCard label="Entradas Vendidas" value={stats.numberTickets} />
                <StatCard label="Eventos Activos" value={stats.activeEvents} />
                <StatCard label="Usuarios" value={stats.numberUsers} />
                <StatCard label="Admins" value={stats.numberAdmins} />
            </div>

            <div className="d-flex flex-wrap gap-4 mt-4 justify-content-center">
                <div className="btn-group shadow-sm" role="group">
                    <Link to="/admin/events/create" className="btn btn-primary">Crear evento</Link>
                    <Link to="/admin/events" className="btn btn-outline-primary">Gestionar eventos</Link>
                </div>

                <div className="btn-group shadow-sm" role="group">
                    <Link to="/admin/artists/create" className="btn btn-primary">Crear artista</Link>
                    <Link to="/admin/artists" className="btn btn-outline-primary">Gestionar artistas</Link>
                </div>
            </div>

            <div className="d-flex flex-wrap gap-4 mt-4 justify-content-center">
                <Link to="/admin/statistics" className="btn btn-primary btn-admin-action shadow-sm">
                    <i className="bi bi-bar-chart-line me-2"></i> Estadísticas
                </Link>
                <Link to="/admin/users" className="btn btn-primary btn-admin-action shadow-sm">
                    <i className="bi bi-people-fill me-2"></i> Usuarios
                </Link>
            </div>
        </div>
    );
}

const StatCard = ({ label, value }: { label: string; value: number }) => (
    <div className="col-md-3">
        <div className="card text-center p-3 shadow-sm">
            <h5>{label}</h5>
            <p className="h4">{value}</p>
        </div>
    </div>
);