import { useState } from "react";
import { getUsers } from "~/services/adminService";
import type { User } from "~/models/User";
import { Link, useLoaderData } from "react-router";
import { Container, Card, Table, Button, Alert, Spinner } from "react-bootstrap";

const PAGE_SIZE = 5;

export async function clientLoader() {
  const users = await getUsers(0, PAGE_SIZE);
  return { users };
}

export default function ManageUsers() {
    const { users: initialUsers } = useLoaderData<typeof clientLoader>();

    const [users, setUsers] = useState<User[]>(initialUsers);
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    async function fetchUsers(newPage: number) {
        setLoading(true);
        setError(null);
        try {
            const data = await getUsers(newPage, PAGE_SIZE);
            setUsers(data);
            setPage(newPage);
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error desconocido");
        } finally {
            setLoading(false);
        }
    };

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
                <Button variant="outline-primary" disabled={page === 0 || loading} onClick={() => fetchUsers(page - 1)}>
                    &laquo; Anterior
                </Button>
                <span className="text-muted">Página {page + 1}</span>
                <Button variant="outline-primary" disabled={users.length < PAGE_SIZE || loading} onClick={() => fetchUsers(page + 1)}>
                    Siguiente &raquo;
                </Button>
            </div>
        </Container>
    );
}