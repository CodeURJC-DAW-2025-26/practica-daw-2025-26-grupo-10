import type ArtistBasic from "~/models/ArtistBasic";
import type SessionBasic from "~/models/SessionBasic";

export default interface EventBasic {
  eventID: number;
  name: string;
  artist: ArtistBasic;
  category: string;
  place: string;
  sessions: SessionBasic[];
  mainImageUrl: string;
}
