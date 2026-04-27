import { useState } from "react";
import { useParams, useNavigate, useLoaderData } from "react-router";
import { Container, Card, Table, Button, Alert, Form, Row, Col } from "react-bootstrap";
import { getSessionsByEvent, createSession, updateSession, deleteSession } from "~/services/session-service";
import type { SessionBasic } from "~/models/SessionBasic";
import { useConfirmDialog } from "~/hooks/useConfirmDialog";
import { useTemporaryMessage } from "~/hooks/useTemporaryMessage";
import { ConfirmDialog } from "~/components/confirmDialog/ConfirmDialog";

export async function clientLoader({ params }: { params: { id: string } }) {
  const sessions = await getSessionsByEvent(params.id);
  return { sessions };
}

export default function ManageSessions() {
    const {sessions: initialSessions} = useLoaderData<typeof clientLoader>()
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [sessions, setSessions] = useState<SessionBasic[]>(initialSessions);
    const [error, setError] = useState<string | null>(null);
    const [newSessionDate, setNewSessionDate] = useState("");
    const [isCreating, setIsCreating] = useState(false);
    const [editingIndex, setEditingIndex] = useState<number | null>(null);
    const [editDate, setEditDate] = useState("");
    
    const { error: deleteError, setError: setDeleteError, success: deleteSuccess, setSuccess: setDeleteSuccess } = useTemporaryMessage();
    const { isOpen: isNotConfirmed, message, confirm, handleCancel, handleConfirm } = useConfirmDialog();

    async function loadSessions() {
        try {
        const data = await getSessionsByEvent(id!);
        setSessions(data);
        } catch (err) {
        setError(err instanceof Error ? err.message : "Error al cargar sesiones");
        }
    }

    const handleCreate = async () => {
        if (!newSessionDate || !id) return;
        setIsCreating(true);
        try {
            await createSession(id, newSessionDate.replace("T", " "));
            setNewSessionDate("");
            await loadSessions();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al crear");
        } finally {
            setIsCreating(false);
        }
    };

    const handleDelete = async (sessionID: number) => {
        confirm("¿Estás seguro de que quieres eliminar este evento?", async () => {
            try {
                await deleteSession(sessionID);
                setSessions(sessions.filter(s => s.sessionID !== sessionID));
                setDeleteSuccess("Sesión eliminada correctamente")
            } catch (err) {
                console.error(err);
                setDeleteError("Error al eliminar la sesión");
            }
        });
    }

    const startEditing = (index: number, currentDate: string) => {
        setEditingIndex(index);
        setEditDate(currentDate.replace(" ", "T"));
    };

    const handleSaveEdit = async () => {
        if (editingIndex === null || !id) return;
        try {
            await updateSession(id, editingIndex + 1, editDate.replace("T", " "));
            setEditingIndex(null);
            await loadSessions();
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al actualizar");
        }
    };

    return (
        <Container className="my-5">
            {isNotConfirmed && (
                <ConfirmDialog
                message={message}
                onConfirm={handleConfirm}
                onCancel={handleCancel}
                />
            )}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Gestionar Sesiones (Evento #{id})</h2>
                <Button variant="outline-secondary" onClick={() => navigate(`/admin/events/${id}`)}>
                    Volver al Evento
                </Button>
            </div>

            {deleteError && <p className="alert alert-danger">{deleteError}</p>}
            {deleteSuccess && <p className="alert alert-success">{deleteSuccess}</p>}

            <Card className="mb-4 shadow-sm">
                <Card.Body>
                    <Card.Title as="h5" className="mb-3">Añadir nueva sesión</Card.Title>
                    <Row className="g-2 align-items-center">
                        <Col md={8}>
                            <Form.Control
                                type="datetime-local"
                                value={newSessionDate}
                                onChange={(e) => setNewSessionDate(e.target.value)}
                            />
                        </Col>
                        <Col md={4}>
                            <Button
                                variant="primary"
                                className="w-100"
                                onClick={handleCreate}
                                disabled={!newSessionDate || isCreating}
                            >
                                {isCreating ? "Añadiendo..." : "Añadir Sesión"}
                            </Button>
                        </Col>
                    </Row>
                </Card.Body>
            </Card>

            <h4>Sesiones Programadas</h4>
            {sessions.length === 0 ? (
                <p className="text-muted">Aún no hay sesiones para este evento.</p>
            ) : (
                <Table hover className="align-middle">
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
                                        <Form.Control
                                            type="datetime-local"
                                            size="sm"
                                            value={editDate}
                                            onChange={(e) => setEditDate(e.target.value)}
                                        />
                                    ) : (
                                        session.date ? new Date(session.date).toLocaleString("es-ES") : "Fecha no disponible"
                                    )}
                                </td>
                                <td className="text-end">
                                    {editingIndex === index ? (
                                        <>
                                            <Button size="sm" variant="success" className="me-2" onClick={handleSaveEdit}>Guardar</Button>
                                            <Button size="sm" variant="secondary" onClick={() => setEditingIndex(null)}>Cancelar</Button>
                                        </>
                                    ) : (
                                        <>
                                            <Button size="sm" variant="outline-primary" className="me-2" onClick={() => startEditing(index, String(session.date || ""))}>
                                                Editar
                                            </Button>
                                            <Button size="sm" variant="outline-danger" onClick={() => handleDelete(session.sessionID)}>
                                                Eliminar
                                            </Button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </Container>
    );
}
