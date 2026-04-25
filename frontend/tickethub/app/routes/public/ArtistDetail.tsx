import { useLoaderData } from "react-router";
import ArtistDetailUI from "~/components/public/ArtistDetailUI";
import { publicArtistService } from "~/services/PublicArtistService";

export async function clientLoader({ params }: { params: { id: string } }) {
    const res = await publicArtistService.getArtistById(params.id);
    return res;
}

export default function ArtistDetailRoute() {
    const artist = useLoaderData<typeof clientLoader>();
    return (
        <ArtistDetailUI
            artist={artist}
        />
    );
}