import { useLoaderData } from "react-router";
import axios from "axios";
import ArtistUI from "~/components/public/ArtistDetailUI";

export interface ArtistDetail {
    artistID: number;
    artistName: string;
    info: string;
    instagram: string;
    twitter: string;
    eventsIncoming: { eventID: number; eventName: string }[];
    lastEvents: { eventID: number; eventName: string }[];
}

export async function loader({ params }: { params: { id: string } }) {
    const res = await axios.get(`/api/v1/artists/${params.id}`);
    return res.data as ArtistDetail;
}

export default function ArtistRoute() {
    const artist = useLoaderData<typeof loader>();
    return (
        <ArtistUI
            artist={artist}
            eventsIncoming={artist.eventsIncoming ?? []}
            lastEvents={artist.lastEvents ?? []}
        />
    );
}