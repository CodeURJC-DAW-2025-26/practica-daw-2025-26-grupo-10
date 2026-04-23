import type {ArtistBasic} from "./ArtistBasic";
import type {ImageBasic} from "./ImageBasic";

export interface EventBasic {
  eventID: number;
  name: string;
  artist: ArtistBasic;
  category: string;
  place: string;
  mainImage: ImageBasic;
}