import { useEffect, useState } from "react";
import { Link } from "react-router";
import { Container, Card, Table, Button, Alert, Spinner } from "react-bootstrap";
import { getUsers } from "~/services/adminService";
import type { UserDTO } from "~/models/User";

export default function ManageUsers() {
    const [users, setUsers] = useState<UserDTO[]>([]);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const pageSize = 5;

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                setLoading(true);
                setError(null);
                const data = await getUsers(page, pageSize);
                setUsers(data);
            } catch (err) {
                setError(err instanceof Error ? err.message : "Error desconocido");
            } finally {
                setLoading(false);
            }
        };
        fetchUsers();
    }, [page]);

    return (
        <Container className="py-4">
            <h2 className="mb-4">Gestión de Usuarios</h2>

            {error && <Alert variant="danger">{error}</Alert>}

            <Card className="shadow-sm">
                <Card.Body>
                    {loading ? (
                        <div className="text-center py-4">
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Cargando...</span>
                            </Spinner>
                        </div>
                    ) : (
                        <div className="table-responsive">
                            <Table hover className="align-middle">
                                <thead className="table-light">
                                    <tr>
                                        <th>ID</th>
                                        <th>Usuario</th>
                                        <th>Email</th>
                                        <th className="text-end">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {users.length === 0 ? (
                                        <tr>
                                            <td colSpan={4} className="text-center py-3 text-muted">
                                                No hay usuarios registrados.
                                            </td>
                                        </tr>
                                    ) : (
                                        users.map((user) => (
                                            <tr key={user.userID}>
                                                <td>{user.userID}</td>
                                                <td>{user.username}</td>
                                                <td>{user.email}</td>
                                                <td className="text-end">
                                                    <Link to={`/admin/users/edit/${user.userID}`} className="btn btn-sm btn-outline-primary me-2">
                                                        Editar
                                                    </Link>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </Table>
                        </div>
                    )}
                </Card.Body>
            </Card>

            <div className="d-flex justify-content-between align-items-center mt-3">
                <Button variant="outline-primary" disabled={page === 0 || loading} onClick={() => setPage((p) => p - 1)}>
                    &laquo; Anterior
                </Button>
                <span className="text-muted">Página {page + 1}</span>
                <Button variant="outline-primary" disabled={users.length < pageSize || loading} onClick={() => setPage((p) => p + 1)}>
                    Siguiente &raquo;
                </Button>
            </div>
        </Container>
    );
}
