import type { Owner } from "../domain/models/Owner";
import { httpClient } from "./http/httpClient";

interface CreateOwnerRequest {
  fullName: string;
  phone?: string;
  address?: string;
}

export const ownersService = {
  async createOwner(data: CreateOwnerRequest): Promise<Owner> {
    return httpClient.post<Owner>("/owners", data);
  },
  async getMe(): Promise<Owner> {
    return httpClient.get<Owner>("/owners/me");
  },
};
