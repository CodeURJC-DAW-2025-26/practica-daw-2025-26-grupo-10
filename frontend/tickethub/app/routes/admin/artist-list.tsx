import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router';
import axios from 'axios';

interface Artist {
    artistID: number;
    artistName: string;
    instagram: string;
    twitter: string;
}

export default function ArtistList() {
    const [artists, setArtists] = useState<Artist[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        axios.get(`/api/v1/admin/artists`)
            .then(res => {
                setArtists(res.data.content);
                setLoading(false);
            }).catch(err => {
                console.error("Error fetching artists:", err);
                setLoading(false);
                alert("Error al cargar los artistas. Por favor, inténtalo de nuevo más tarde.");
            });
    }, []);

    const handleDelete = async (id: number) => {
        const confirmDelete = window.confirm("¿Estás seguro de que deseas eliminar este artista? Esta acción no se puede deshacer.");
        if (confirmDelete) {
            try {
                await axios.delete(`/api/v1/admin/artists/${id}`);
                setArtists(artists.filter(artist => artist.artistID !== id));
            } catch (error) {
                alert("Hubo un error al eliminar el artista.");
            }
        }
    };

    if (loading) {
        return <div className="container my-5 text-center">Cargando lista de artistas...</div>;
    }

    return (
        //header admin
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
                                    to={`api/v1/admin/artists/${artist.artistID}`}
                                    className='btn btn-sm btn-primary me-2'>
                                    Editar
                                </Link>
                                <button
                                    onClick={() => handleDelete(artist.artistID)}
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
                    <Link to="/admin/admin" className="btn btn-outline-primary btn-success">
                        Volver
                    </Link>
                </div>
            </div>

        </div>
        //footer admin
    );
}