import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router";
import {
    getSessionsByEvent,
    createSession,
    updateSession,
    deleteSession
} from "~/services/session-service";
import type { SessionBasic } from "~/models/SessionBasic";

export default function ManageSessions() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [sessions, setSessions] = useState<SessionBasic[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [newSessionDate, setNewSessionDate] = useState("");
    const [isCreating, setIsCreating] = useState(false);

    const [editingIndex, setEditingIndex] = useState<number | null>(null);
    const [editDate, setEditDate] = useState("");

    useEffect(() => {
        if (!id) return;
        loadSessions();
    }, [id]);

    async function loadSessions() {
        setLoading(true);
        try {
            const data = await getSessionsByEvent(id!);
            setSessions(data);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al cargar sesiones");
        } finally {
            setLoading(false);
        }
    }


    const handleCreate = async () => {
        if (!newSessionDate || !id) return;
        setIsCreating(true);
        try {
            const formattedDate = newSessionDate.replace("T", " ");
            await createSession(id, formattedDate);
            setNewSessionDate("");
            await loadSessions();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al crear");
        } finally {
            setIsCreating(false);
        }
    };

    const handleDelete = async (sessionID: number) => {
        if (!confirm("¿Seguro que deseas eliminar esta sesión?")) return;
        try {
            await deleteSession(sessionID);
            setSessions(sessions.filter(s => s.sessionID !== sessionID));
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al eliminar");
        }
    };

    const startEditing = (index: number, currentDate: string) => {
        setEditingIndex(index);
        setEditDate(currentDate.replace(" ", "T"));
    };

    const handleSaveEdit = async () => {
        if (editingIndex === null || !id) return;
        try {
            const formattedDate = editDate.replace("T", " ");
            await updateSession(id, editingIndex + 1, formattedDate);
            setEditingIndex(null);
            await loadSessions();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al actualizar");
        }
    };

    if (loading) return <div className="container my-5"><p>Cargando sesiones...</p></div>;

    return (
        <div className="container my-5">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Gestionar Sesiones (Evento #{id})</h2>
                <button className="btn btn-outline-secondary" onClick={() => navigate(`/admin/events/edit/${id}`)}>
                    Volver al Evento
                </button>
            </div>

            {error && <div className="alert alert-danger">{error}</div>}

            <div className="card mb-4 shadow-sm">
                <div className="card-body">
                    <h5 className="card-title mb-3">Añadir nueva sesión</h5>
                    <div className="row g-2 align-items-center">
                        <div className="col-md-8">
                            <input 
                                type="datetime-local"
                                className="form-control"
                                value={newSessionDate}
                                onChange={(e) => setNewSessionDate(e.target.value)}
                            />
                        </div>
                        <div className="col-md-4">
                            <button
                                className="btn btn-primary w-100"
                                onClick={handleCreate}
                                disabled={!newSessionDate || isCreating}
                            >
                                {isCreating ? "Añadiendo..." : "Añadir Sesión"}
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <h4>Sesiones Programadas</h4>
            {sessions.length === 0 ? (
                <p className="text-muted">Aún no hay sesiones para este evento.</p>
            ) : (
                <table className="table table-hover align-middle">
                    <thead className="table-light">
                        <tr>
                            <th>#</th>
                            <th>Fecha y Hora</th>
                            <th className="text-end">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        {sessions.map((session, index) => (
                            <tr key={session.sessionID}>
                                <td>{index + 1}</td>
                                <td>
                                    {editingIndex === index ? (
                                        <input 
                                            type="datetime-local" 
                                            className="form-control form-control-sm"
                                            value={editDate}
                                            onChange={(e) => setEditDate(e.target.value)}
                                        />
                                    ) : (
                                        String(session.date || session.date || "Fecha no disponible")
                                    )}
                                </td>
                                <td className="text-end">
                                    {editingIndex === index ? (
                                        <>
                                            <button className="btn btn-sm btn-success me-2" onClick={handleSaveEdit}>Guardar</button>
                                            <button className="btn btn-sm btn-secondary" onClick={() => setEditingIndex(null)}>Cancelar</button>
                                        </>
                                    ) : (
                                        <>
                                            <button className="btn btn-sm btn-outline-primary me-2" onClick={() => startEditing(index, String(session.date || ""))}>
                                                Editar
                                            </button>
                                            <button className="btn btn-sm btn-outline-danger" onClick={() => handleDelete(session.sessionID)}>
                                                Eliminar
                                            </button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}