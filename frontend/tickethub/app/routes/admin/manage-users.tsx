import { useEffect, useState } from "react";
import { getUsers } from "~/services/adminService";
import type { UserDTO } from "~/models/User";
import { Link } from "react-router";

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
        <div className="container py-4">
            <h2 className="mb-4">Gestión de Usuarios</h2>

            {error && (
                <div className="alert alert-danger" role="alert">
                    {error}
                </div>
            )}

            <div className="card shadow-sm">
                <div className="card-body">
                    {loading ? (
                        <div className="text-center py-4">
                            <div className="spinner-border text-primary" role="status">
                                <span className="visually-hidden">Cargando...</span>
                            </div>
                        </div>
                    ) : (
                        <div className="table-responsive">
                            <table className="table table-hover align-middle">
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
                                                    {/* Botones de acción vacíos para el futuro */}
                                                    <Link
                                                        to={`/admin/users/edit/${user.userID}`}
                                                        className="btn btn-sm btn-outline-primary me-2"
                                                    >
                                                        Editar
                                                    </Link>
                                                </td>
                                            </tr>
                                        ))
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}
                </div>
            </div>

            {/* Paginación */}
            <div className="d-flex justify-content-between align-items-center mt-3">
                <button 
                    className="btn btn-outline-primary" 
                    disabled={page === 0 || loading} 
                    onClick={() => setPage((p) => p - 1)}
                >
                    &laquo; Anterior
                </button>
                <span className="text-muted">Página {page + 1}</span>
                <button 
                    className="btn btn-outline-primary"
                    disabled={users.length < pageSize || loading}
                    onClick={() => setPage((p) => p + 1)}
                >
                    Siguiente &raquo;
                </button>
            </div>
        </div>
    );
}