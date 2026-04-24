import type {ArtistBasic} from "./ArtistBasic";
import type {SessionBasic} from "./SessionBasic";
import type {ImageBasic} from "./ImageBasic";

export interface EventBasic {
  eventID: number;
  name: string;
  artist: ArtistBasic;
  category: string;
  place: string;
  sessions: SessionBasic[];
  mainImageUrl: ImageBasic;
}
