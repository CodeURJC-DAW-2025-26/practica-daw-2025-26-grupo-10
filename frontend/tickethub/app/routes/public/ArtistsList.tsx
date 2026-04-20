import { useEffect } from "react";
import { useLoaderData } from "react-router";
import axios from "axios";
import ArtistsUI from "~/components/public/ArtistsListUI";
import { usePublicArtistsStore, type PublicArtist } from "~/store/publicArtistStore";

export async function loader() {
    const res = await axios.get("/api/v1/public/artists", {
        params: { page: 0, size: 5, name: "" },
    });
    return { initial: res.data.content as PublicArtist[], isLast: res.data.last as boolean };
}

export default function ArtistsRoute() {
    const { initial, isLast } = useLoaderData<typeof loader>();
    const { artists, search, hasMore, loading, reset, setSearch, fetchBySearch, loadMore } = usePublicArtistsStore();

    // Set the initial data from the loader into the store when the component mounts
    useEffect(() => { reset(initial, isLast); }, []);

    useEffect(() => {
        const timer = setTimeout(() => fetchBySearch(search), 500);
        return () => clearTimeout(timer);
    }, [search]); // only refetch when the search query changes

    return (
        <ArtistsUI
            artists={artists}
            searchQuery={search}
            onSearchChange={setSearch}
            onLoadMore={loadMore}
            hasMore={hasMore && !loading}
        />
    );
}