import type {ArtistBasic} from "./ArtistBasic";
import type {EventBasic} from "./EventBasic";

export interface IndexResponse {
    eventsTop: EventBasic[];
    eventsBottom: EventBasic[];
    artists: ArtistBasic[];
    recommendedEvents: EventBasic[];
}