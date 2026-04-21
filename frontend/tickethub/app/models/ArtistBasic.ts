import type {Image} from "~/models/ImageBasic";

export interface ArtistBasic {
  artistID: number;
  artistName: string;
  artistImage: Image;
}