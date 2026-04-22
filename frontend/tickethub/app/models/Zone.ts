export interface Zone {
  id: number;
  name: string;
  capacity: number;
  price: number;
  eventId: number;
  selected: boolean;
}

export interface ZoneCreateDTO {
  name: string;
  capacity: number;
  price: number;
}
