import { useEffect, useRef } from "react";
import { useLoaderData } from "react-router";
import ArtistsListUI from "~/components/public/ArtistsListUI";
import { usePublicArtistsStore } from "~/store/publicArtistStore";
import { publicArtistService } from "~/services/PublicArtistService";
import GlobalSpinner from '~/components/GlobalSpinner'

export async function clientLoader() {
    const res = await publicArtistService.getAllArtists();
    return { initial: res.content, isLast: res.last };
}

export default function ArtistsListRoute() {
    const { initial, isLast } = useLoaderData<typeof clientLoader>();
    const { artists, search, hasMore, loading, reset, setSearch, fetchBySearch, loadMore } = usePublicArtistsStore();
    const isFirstLoad = useRef(true);
    // Set the initial data from the loader into the store when the component mounts
    useEffect(() => { reset(initial, isLast); }, []);

    useEffect(() => {
        if (isFirstLoad.current) {
            isFirstLoad.current = false;
            return; // skip the first effect run since we already have the initial data
        }
        const timer = setTimeout(() => fetchBySearch(search), 1000);
        return () => clearTimeout(timer);
    }, [search]); // only refetch when the search query changes

    if (loading && artists.length === 0) return <GlobalSpinner />;
    
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