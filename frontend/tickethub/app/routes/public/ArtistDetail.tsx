import { useLoaderData } from "react-router";
import ArtistDetailUI from "~/components/public/ArtistDetailUI";
import { publicArtistService } from "~/services/PublicArtistService";

export async function loader({ params }: { params: { id: string } }) {
    const res = await publicArtistService.getArtistById(params.id);
    return res;
}

export default function ArtistDetailRoute() {
    const artist = useLoaderData<typeof loader>();
    return (
        <ArtistDetailUI
            artist={artist}
            eventsIncoming={artist.eventsIncoming ?? []}
            lastEvents={artist.lastEvents ?? []}
        />
    );
}