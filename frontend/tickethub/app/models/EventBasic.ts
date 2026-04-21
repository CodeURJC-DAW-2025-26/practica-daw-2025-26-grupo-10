import type {ArtistBasic} from "~/models/ArtistBasic";
import type {ImageBasic} from "~/models/ImageBasic";

export interface EventBasic {
  eventID: number;
  name: string;
  artist: ArtistBasic;
  category: string;
  place: string;
  mainImage: ImageBasic;
}