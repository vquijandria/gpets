import { useEffect, useState } from "react";
import AppShell from "../components/layout/AppShell";
import PetsList from "../components/pets/PetsList";
import type { Pet } from "../domain/models/Pet";
import { petsService, type CreatePetRequest } from "../services/petsService";
import CreatePetModal from "../components/pets/CreatePetModal";
import { subscribePetsRealtime } from "../services/realtime/petsRealtime";

export default function PetsPage() {
  const [pets, setPets] = useState<Pet[]>([]);
  const [loading, setLoading] = useState(true);
  const [openCreate, setOpenCreate] = useState(false);

  const loadPets = async () => {
    setLoading(true);
    try {
      const data = await petsService.getPets();
      setPets(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadPets();

    const unsub = subscribePetsRealtime((realtimePets) => {
      setPets(realtimePets);
      setLoading(false);
    });

    return () => unsub();
  }, []);

  const handleCreate = async (data: CreatePetRequest) => {
    await petsService.createPet(data);
  };

  return (
    <AppShell>
      <div className="flex items-start justify-between gap-4 mb-6">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Mascotas</h1>
          <p className="text-sm text-slate-500 mt-1">
            Cambios de ubicación se reflejan en tiempo real.
          </p>
        </div>

        <button
          onClick={() => setOpenCreate(true)}
          className="shrink-0 rounded-xl bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
        >
          + Nueva mascota
        </button>
      </div>

      <PetsList pets={pets} loading={loading} onRefresh={loadPets} />

      <CreatePetModal
        open={openCreate}
        onClose={() => setOpenCreate(false)}
        onCreate={handleCreate}
      />
    </AppShell>
  );
}
