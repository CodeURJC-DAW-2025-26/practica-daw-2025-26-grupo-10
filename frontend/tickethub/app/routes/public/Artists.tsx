import { useState, useEffect } from "react";
import axios from "axios";
import ArtistsUI, { type Artist } from "~/components/public/ArtistsUI";

export default function ArtistsRoute() {
    const [artists, setArtists] = useState<Artist[]>([]);
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    // if search changes, reset to page 0 and fetch new results
    useEffect(() => {
        setPage(0);
        fetchArtists(0, search, true);
    }, [search]); // it will trigger every time the search query changes

    const fetchArtists = async (pageNum: number, query: string, isNewSearch: boolean) => {
        try {
            const res = await axios.get("/api/v1/public/artists", {
                params: { 
                    page: pageNum, 
                    size: 5,
                    name: query
                }
            });

            const newArtists = res.data.content; // artist list
            const isLastPage = res.data.last;

            // if it's a new search, we replace the list, otherwise we append to it
            setArtists(prev => isNewSearch ? newArtists : [...prev, ...newArtists]);
            
            // Actualizamos si debemos seguir mostrando el botón
            setHasMore(!isLastPage); 
        } catch (err) {
            console.error("Error cargando la lista de artistas", err);
            alert("Error al cargar los artistas. Por favor, inténtalo de nuevo más tarde.");
        }
    };

    // Load more for the button
    const handleLoadMore = () => {
        const nextPage = page + 1;
        setPage(nextPage);
        fetchArtists(nextPage, search, false);
    };

    return (
        <ArtistsUI 
            artists={artists}
            searchQuery={search}
            onSearchChange={setSearch}
            onLoadMore={handleLoadMore}
            hasMore={hasMore}
        />
    );
}