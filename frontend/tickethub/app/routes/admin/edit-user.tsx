import { useEffect, useState, type SetStateAction } from "react";
import { useParams, useNavigate } from "react-router";
import { getUserById, updateUser } from "~/services/adminService";
import type { UserDTO } from "~/models/User";

export default function EditUser() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    
    const [formData, setFormData] = useState<UserDTO | null>(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!id) return;
        getUserById(id)
            .then((data: SetStateAction<UserDTO | null>) => setFormData(data))
            .catch((err: { message: SetStateAction<string | null>; }) => setError(err.message))
            .finally(() => setLoading(false));
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData || !id) return;

        setSaving(true);
        try {
            await updateUser(formData.userID, formData);
            navigate("/admin/users");
        } catch (err) {
            setError(err instanceof Error ? err.message : "Error al guardar");
            setSaving(false);
        }
    };

    if (loading) return <div className="container py-5 text-center">Cargando...</div>;
    if (!formData) return <div className="container py-5 text-center text-danger">Usuario no encontrado</div>;

    return (
        <div className="container py-4">
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Editar Usuario #{id}</h2>
                <button className="btn btn-outline-secondary" onClick={() => navigate("/admin/users")}>
                    Volver
                </button>
            </div>

            {error && <div className="alert alert-danger">{error}</div>}

            <div className="card shadow-sm col-md-8 offset-md-2">
                <div className="card-body">
                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Nombre de Usuario</label>
                            <input 
                                type="text" 
                                className="form-control" 
                                value={formData.username || ""} 
                                onChange={e => setFormData({...formData, username: e.target.value})}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Correo Electrónico</label>
                            <input 
                                type="email" 
                                className="form-control" 
                                value={formData.email || ""} 
                                onChange={e => setFormData({...formData, email: e.target.value})}
                                required
                            />
                        </div>

                        <div className="mb-4 form-check form-switch">
                            <input 
                                className="form-check-input" 
                                type="checkbox" 
                                role="switch" 
                                id="adminSwitch"
                                checked={formData.admin}
                                onChange={e => setFormData({...formData, admin: e.target.checked})}
                            />
                            <label className="form-check-label" htmlFor="adminSwitch">
                                ¿Es Administrador?
                            </label>
                        </div>

                        <div className="d-grid">
                            <button type="submit" className="btn btn-primary" disabled={saving}>
                                {saving ? "Guardando..." : "Guardar Cambios"}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    );
}