import type ArtistBasic from "~/models/ArtistBasic";
import type SessionBasic from "~/models/SessionBasic";
import type ZoneBasic from "~/models/ZoneBasic";
import type DiscountBasic from "~/models/DiscountBasic";
import type ImageBasic from "~/models/ImageBasic";

export default interface Event {
  eventID: number;
  name: string;
  capacity: number;
  targetAge: number;
  artist: ArtistBasic;
  sessions: SessionBasic[];
  zones: ZoneBasic[];
  discounts: DiscountBasic[];
  place: string;
  category: string;
  eventImages: ImageBasic[];
  mainImage: ImageBasic;
}
