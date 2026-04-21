import type { SessionBasic } from "./SessionBasic";

export interface EventCreateUpdate {
    name: string,
    category: string,
    place: string,
    artistId: number,
    targetAge: number,
    capacity: number,
    discountIds: number[] | null,
    zones: ZoneBasic[] | null,
    sessions: SessionBasic[] | null
}