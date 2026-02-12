import { useMemo, useState } from "react";
import type { CreatePetRequest } from "../../services/petsService";

type Props = {
  open: boolean;
  onClose: () => void;
  onCreate: (data: CreatePetRequest) => Promise<void>;
};

export default function CreatePetModal({ open, onClose, onCreate }: Props) {
  const [name, setName] = useState("");
  const [species, setSpecies] = useState<CreatePetRequest["species"]>("DOG");
  const [status, setStatus] = useState<CreatePetRequest["status"]>("LOST");
  const [lat, setLat] = useState("-12.1211");
  const [lng, setLng] = useState("-77.0296");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const canSubmit = useMemo(() => {
    if (!name.trim()) return false;
    const latNum = Number(lat);
    const lngNum = Number(lng);
    return Number.isFinite(latNum) && Number.isFinite(lngNum);
  }, [name, lat, lng]);

  if (!open) return null;

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    const latNum = Number(lat);
    const lngNum = Number(lng);

    if (!name.trim() || !Number.isFinite(latNum) || !Number.isFinite(lngNum)) {
      setError("Completa los campos correctamente.");
      return;
    }

    setSaving(true);
    try {
      await onCreate({
        name: name.trim(),
        species,
        status,
        lat: latNum,
        lng: lngNum,
      });
      onClose();
      setName("");
      setSpecies("DOG");
      setStatus("LOST");
    } catch (err) {
      console.error(err);
      setError("No se pudo crear la mascota.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4">
      <div className="absolute inset-0 bg-black/40" onClick={onClose} />

      <div className="relative w-full max-w-lg rounded-2xl bg-white shadow-xl border border-slate-200">
        <div className="p-5 border-b border-slate-100 flex items-center justify-between">
          <div>
            <h2 className="text-lg font-semibold text-slate-900">
              Nueva mascota
            </h2>
            <p className="text-sm text-slate-500">
              Se registrará automáticamente bajo tu perfil.
            </p>
          </div>
          <button
            onClick={onClose}
            className="rounded-lg px-3 py-2 text-sm text-slate-600 hover:bg-slate-100"
          >
            ✕
          </button>
        </div>

        <form onSubmit={submit} className="p-5 space-y-4">
          {error && (
            <div className="rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700">
              {error}
            </div>
          )}

          <div>
            <label className="block text-sm font-medium text-slate-700">
              Nombre
            </label>
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="mt-1 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
              placeholder="Ej: Luna"
              required
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-slate-700">
                Especie
              </label>
              <select
                value={species}
                onChange={(e) =>
                  setSpecies(e.target.value as CreatePetRequest["species"])
                }
                className="mt-1 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
              >
                <option value="DOG">DOG</option>
                <option value="CAT">CAT</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700">
                Estado
              </label>
              <select
                value={status}
                onChange={(e) =>
                  setStatus(e.target.value as CreatePetRequest["status"])
                }
                className="mt-1 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
              >
                <option value="LOST">LOST</option>
                <option value="FOUND">FOUND</option>
                <option value="HOME">HOME</option>
              </select>
            </div>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-slate-700">
                Latitud
              </label>
              <input
                value={lat}
                onChange={(e) => setLat(e.target.value)}
                className="mt-1 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
                placeholder="-12.1211"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700">
                Longitud
              </label>
              <input
                value={lng}
                onChange={(e) => setLng(e.target.value)}
                className="mt-1 w-full rounded-xl border border-slate-200 bg-slate-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
                placeholder="-77.0296"
              />
            </div>
          </div>

          <div className="flex items-center justify-end gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="rounded-xl border border-slate-200 bg-white px-4 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
            >
              Cancelar
            </button>

            <button
              type="submit"
              disabled={!canSubmit || saving}
              className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700 disabled:opacity-60"
            >
              {saving ? "Creando..." : "Crear"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
