import type { SessionBasic } from "./SessionBasic";
import type ZoneBasic from "./ZoneBasic";

export interface EventUpdate {
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