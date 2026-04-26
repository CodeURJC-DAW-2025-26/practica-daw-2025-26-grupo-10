import type { ArtistBasic } from "./ArtistBasic";
import type DiscountBasic from "./DiscountBasic";
import type { ImageBasic } from "./ImageBasic";
import type { SessionBasic } from "./SessionBasic";
import type ZoneBasic from "./ZoneBasic";

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
