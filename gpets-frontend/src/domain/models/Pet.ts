import type { Location } from "./Location";

export type PetStatus = "LOST" | "FOUND" | "HOME";

export interface Pet {
  id: string;               
  name: string;
  
  status: PetStatus;        

  breed?: string;
  description?: string;
  imageUrl?: string;

  ownerId?: string;        

  location: Location;       

  createdAt?: string;       
  updatedAt?: string;       
}
