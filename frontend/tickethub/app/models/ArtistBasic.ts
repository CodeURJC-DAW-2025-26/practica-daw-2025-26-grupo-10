import type ImageBasic from "~/models/ImageBasic";

export default interface ArtistBasic {
  artistID: number;
  artistName: string;
  artistImage: ImageBasic;
}
