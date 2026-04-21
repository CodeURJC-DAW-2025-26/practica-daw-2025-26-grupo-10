import type { ImageBasic } from "./ImageBasic";

export interface ArtistBasic {
    artistID: number;
    artistName: string;
    artistImage: ImageBasic | null;
}