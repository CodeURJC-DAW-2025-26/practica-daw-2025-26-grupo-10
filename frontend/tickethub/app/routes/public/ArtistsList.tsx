import { useEffect } from "react";
import { useLoaderData } from "react-router";
import ArtistsListUI from "~/components/public/ArtistsListUI";
import { usePublicArtistsStore } from "~/store/publicArtistStore";
import { publicArtistService } from "~/services/PublicArtistService";

export async function loader() {
    const res = await publicArtistService.getAllArtists();
    return { initial: res.content, isLast: res.last };
}

export default function ArtistsListRoute() {
    const { initial, isLast } = useLoaderData<typeof loader>();
    const { artists, search, hasMore, loading, reset, setSearch, fetchBySearch, loadMore } = usePublicArtistsStore();

    // Set the initial data from the loader into the store when the component mounts
    useEffect(() => { reset(initial, isLast); }, []);

    useEffect(() => {
        const timer = setTimeout(() => fetchBySearch(search), 500);
        return () => clearTimeout(timer);
    }, [search]); // only refetch when the search query changes

    return (
        <ArtistsListUI
            artists={artists}
            searchQuery={search}
            onSearchChange={setSearch}
            onLoadMore={loadMore}
            hasMore={hasMore && !loading}
        />
    );
}