import { Link } from "react-router";
import { Container, Table, Button, Alert } from "react-bootstrap";
import type { Artist } from "~/models/Artist";
import { ConfirmDialog } from "~/components/confirmDialog";

interface Props {
    artists: Artist[];
    onDelete: (id: number) => void;
    isDialogOpen: boolean;
    dialogMessage: string;
    onDialogConfirm: () => void;
    onDialogCancel: () => void;
    error: string | null;
    success: string | null;
}

export default function ArtistsManagementUI({ artists, onDelete, isDialogOpen,
    dialogMessage, onDialogConfirm, onDialogCancel, error, success
}: Props) {
    return (
        <Container className="my-5">
            <h2>Gestión de Artistas</h2>

            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            {isDialogOpen && (
                <ConfirmDialog message={dialogMessage} onConfirm={onDialogConfirm} onCancel={onDialogCancel} />
            )}

            <Table>
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Instagram</th>
                        <th>Twitter (X)</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {artists.map((artist) => (
                        <tr key={artist.artistID}>
                            <td>{artist.artistName}</td>
                            <td>{artist.instagram}</td>
                            <td>{artist.twitter}</td>
                            <td>
                                <Link to={`/admin/artists/edit/${artist.artistID}`} className="btn btn-sm btn-primary me-2">
                                    Editar
                                </Link>
                                <Button size="sm" variant="danger" onClick={() => onDelete(artist.artistID)}>
                                    Eliminar
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            <div className="d-flex justify-content-start">
                <Link to="/admin/admin" className="btn btn-outline-primary">Volver</Link>
            </div>
        </Container>
    );
}
