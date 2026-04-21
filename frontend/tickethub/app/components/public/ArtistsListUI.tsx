import { Link } from "react-router";
import type { ArtistBasic } from "~/models/ArtistBasic";

interface Props {
    artists: ArtistBasic[];
    searchQuery: string;
    onSearchChange: (query: string) => void;
    onLoadMore: () => void;
    hasMore: boolean;
}

export default function ArtistsListUI({ artists, searchQuery, onSearchChange, onLoadMore, hasMore }: Props) {
    return (
        <main className="flex-fill">
            <div className="container my-5">
                <h2 className="mb-4">Filtrar artistas</h2>

                <div className="row mb-4">
                    <div className="col-md-12">
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Buscar artista..."
                            value={searchQuery}
                            onChange={(e) => onSearchChange(e.target.value)}
                        />
                    </div>
                </div>

                <div className="row g-4">
                    {artists.map((artist) => (
                        <div key={artist.artistID} className="col-md-3">
                            <div className="card text-center">
                                <img
                                    src={`/images/entities/artists/${artist.artistID}`}
                                    className="card-img-top rounded-circle mx-auto mt-3"
                                    style={{ width: "150px", height: "150px", objectFit: "cover" }}
                                    alt={artist.artistName}
                                />
                                <div className="card-body">
                                    <h5 className="card-title">{artist.artistName}</h5>
                                    <Link to={`/public/artist/${artist.artistID}`} className="btn btn-primary">
                                        Ver artista
                                    </Link>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>

                {hasMore && (
                    <div className="text-end mt-4">
                        <button onClick={onLoadMore} className="btn btn-outline-secondary">
                            Cargar más
                        </button>
                    </div>
                )}
            </div>
        </main>
    );
}