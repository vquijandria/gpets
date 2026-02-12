import type { Pet } from "../domain/models/Pet";
import { httpClient } from "./http/httpClient";

interface PetApiResponse {
  id: string;
  name: string;
  status: string;
  species: string;
  ownerId: string | null;
  lastLocation: {
    lat: number;
    lng: number;
    updatedAt: number;
  };
}

export interface CreatePetRequest {
  name: string;
  species: "DOG" | "CAT";
  status: "LOST" | "FOUND" | "HOME";
  lat: number;
  lng: number;
}

function mapToDomain(pet: PetApiResponse): Pet {
  return {
    id: pet.id,
    name: pet.name,
    status: pet.status as "LOST" | "FOUND" | "HOME",
    ownerId: pet.ownerId ?? undefined,
    location: {
      lat: pet.lastLocation.lat,
      lng: pet.lastLocation.lng,
      updatedAt: new Date(pet.lastLocation.updatedAt).toISOString(),
    },
    createdAt: undefined,
    updatedAt: undefined,
  };
}

export const petsService = {
  async getPets(): Promise<Pet[]> {
    const response = await httpClient.get<Record<string, PetApiResponse>>(
      "/pets"
    );

    return Object.values(response).map(mapToDomain);
  },

  async getPetById(id: string): Promise<Pet> {
    const pet = await httpClient.get<PetApiResponse>(`/pets/${id}`);
    return mapToDomain(pet);
  },

  async createPet(body: CreatePetRequest): Promise<Pet> {
    const pet = await httpClient.post<PetApiResponse>("/pets", body);
    return mapToDomain(pet);
  },

  async claimPet(id: string): Promise<Pet> {
  const pet = await httpClient.post<PetApiResponse>(
    `/pets/${id}/claim`,
    {}
  );
  return mapToDomain(pet);
}
};
