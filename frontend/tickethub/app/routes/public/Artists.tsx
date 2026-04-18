import { useState, useEffect, useCallback } from "react";
import axios from "axios";
import ArtistsUI, { type Artist } from "~/components/public/ArtistsUI";

export default function ArtistsRoute() {
    const [artists, setArtists] = useState([]);
    const [search, setSearch] = useState("");
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [loading, setLoading] = useState(false);

    // We use useCallback to memoize the fetchArtists function, so it doesn't get recreated every time user types
    const fetchArtists = useCallback(async (pageNum: number, query: string, isNewSearch: boolean) => {
        setLoading(true);
        try {
            const res = await axios.get("/api/v1/public/artists", {
                params: { 
                    page: pageNum, 
                    size: 5,
                    name: query
                }
            });

            const newArtists = res.data.content;
            const isLastPage = res.data.last;

            // Typing will replace the artists list, while clicking load more will append to it
            setArtists(prev => isNewSearch ? newArtists : [...prev, ...newArtists]);
            setHasMore(!isLastPage);
        } catch (err) {
            console.error("Error cargando artistas", err);
        } finally {
            setLoading(false);
        }
    }, []);

    // Searching
    // Debounce -> waits for user to stop typing for a short period before making the API call
    useEffect(() => {
        const timer = setTimeout(() => {
            setPage(0);
            fetchArtists(0, search, true);
        }, 500); // half a second debounce

        return () => clearTimeout(timer);
    }, [search, fetchArtists]);

    // Button load more
    const handleLoadMore = () => {
        if (loading) return; // It prevents multiple clicks while loading
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
            hasMore={hasMore && !loading} // Don't show load more button if loading or there are no more artists
        />
    );
}