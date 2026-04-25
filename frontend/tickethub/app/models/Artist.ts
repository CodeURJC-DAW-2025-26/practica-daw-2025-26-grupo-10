import type { EventBasic } from "./EventBasic";
import type { ImageBasic } from "./ImageBasic";

export interface Artist {
    artistID: number,
    artistName: string,
    info: string,
    eventsIncoming: EventBasic[],
    lastEvents: EventBasic[],
    artistImage: ImageBasic,
    instagram: string,
    twitter: string,
    selected: boolean
}