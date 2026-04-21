import type { ArtistBasic } from "./ArtistBasic";
import type { ImageBasic } from "./ImageBasic";
import type { SessionBasic } from "./SessionBasic";

export interface Event {
    eventID: number,
    name: string,
    capacity: number,
    targetAge: number,
    artist: ArtistBasic,
    sessions: SessionBasic[],
    zones: ZoneBasic[],
    discounts: DiscountBasic[],
    place: string,
    category: string,
    eventImages: ImageBasic[],
    mainImage: ImageBasic
}