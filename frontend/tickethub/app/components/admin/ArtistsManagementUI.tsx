import { Link } from "react-router";
import type { Artist } from "~/models/Artist";

interface Props {
    artists: Artist[];
    onDelete: (id: number) => void;
}

export default function ArtistsManagementUI({ artists, onDelete }: Props) {
        return (
            <div className="container my-5">
    
                <h2>Gestión de Artistas</h2>
    
                <table className="table">
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
                                    <Link 
                                        to={`/admin/artists/edit/${artist.artistID}`}
                                        className='btn btn-sm btn-primary me-2'>
                                        Editar
                                    </Link>
                                    <button
                                        onClick={() => onDelete(artist.artistID)}
                                        className='btn btn-sm btn-danger'>
                                        Eliminar
                                    </button>    
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
    
                <div className="row mb-3">
                    <div className="col d-flex justify-content-start">
                        <Link to="/admin/admin" className="btn btn-outline-primary ">
                            Volver
                        </Link>
                    </div>
                </div>
    
            </div>
        );
}    