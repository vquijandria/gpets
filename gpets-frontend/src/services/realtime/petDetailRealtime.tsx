import { onValue, ref } from "firebase/database";
import type { Pet } from "../../domain/models/Pet";
import { database } from "../../auth/firebase";

type PetNode = {
  id: string;
  name: string;
  status: string;
  ownerId: string | null;
  lastLocation?: {
    lat: number;
    lng: number;
    updatedAt: number;
  };
  description?: string;
};

function mapToDomain(p: PetNode): Pet | null {
  if (!p?.id || !p?.name || !p.lastLocation) return null;

  return {
    id: p.id,
    name: p.name,
    status: p.status as "LOST" | "FOUND" | "HOME",
    ownerId: p.ownerId ?? undefined,
    description: p.description,
    location: {
      lat: p.lastLocation.lat,
      lng: p.lastLocation.lng,
      updatedAt: new Date(p.lastLocation.updatedAt).toISOString(),
    },
    createdAt: undefined,
    updatedAt: undefined,
  };
}

export function subscribePetRealtime(
  petId: string,
  onPet: (pet: Pet | null) => void
) {
  const petRef = ref(database, `/pets/${petId}`);

  const unsubscribe = onValue(petRef, (snap) => {
    const raw = snap.val() as PetNode | null;
    if (!raw) {
      onPet(null);
      return;
    }
    onPet(mapToDomain(raw));
  });

  return () => unsubscribe();
}
