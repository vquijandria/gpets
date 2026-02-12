import { getDatabase, onValue, ref } from "firebase/database";
import type { Pet } from "../../domain/models/Pet";
import { firebaseApp } from "../../auth/firebase";

type PetsMap = Record<
  string,
  {
    id: string;
    name: string;
    status: "LOST" | "FOUND" | "HOME" | string;
    species?: string;
    ownerId: string | null;
    lastLocation?: {
      lat: number;
      lng: number;
      updatedAt: number;
    };
  }
>;

function mapToDomain(p: PetsMap[string]): Pet | null {
  if (!p?.id || !p?.name) return null;

  const last = p.lastLocation;
  if (!last) return null;

  return {
    id: p.id,
    name: p.name,
    status: p.status as "LOST" | "FOUND" | "HOME",
    ownerId: p.ownerId ?? undefined,
    location: {
      lat: last.lat,
      lng: last.lng,
      updatedAt: new Date(last.updatedAt).toISOString(),
    },
    createdAt: undefined,
    updatedAt: undefined,
  };
}

export function subscribePetsRealtime(onPets: (pets: Pet[]) => void) {
  const db = getDatabase(firebaseApp);
  const petsRef = ref(db, "/pets");

  const unsubscribe = onValue(petsRef, (snap) => {
    const raw = (snap.val() ?? {}) as PetsMap;
    const list = Object.values(raw)
      .map(mapToDomain)
      .filter((x): x is Pet => x !== null);

    onPets(list);
  });

  return () => unsubscribe();
}
