import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import AppShell from "../components/layout/AppShell";
import GoogleMapView from "../components/maps/GoogleMapView";
import type { Pet } from "../domain/models/Pet";
import { petsService } from "../services/petsService";
import { subscribePetRealtime } from "../services/realtime/petDetailRealtime";

export default function PetDetailPage() {
  const { id } = useParams<{ id: string }>();
  const [pet, setPet] = useState<Pet | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;

    setLoading(true);
    setError(null);

    const unsub = subscribePetRealtime(id, (p) => {
      setPet(p);
      setLoading(false);
    });

    const fallback = async () => {
      try {
        const data = await petsService.getPetById(id);
        setPet(data);
      } catch (e) {
        console.error(e);
        setError("No se pudo cargar el detalle de la mascota.");
      } finally {
        setLoading(false);
      }
    };

    void fallback();

    return () => unsub();
  }, [id]);

  return (
    <AppShell>
      <div className="mb-4">
        <Link to="/pets" className="text-sm text-blue-600 hover:underline">
          ← Volver a lista
        </Link>
      </div>

      {error && (
        <div className="mb-4 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">
          {error}
        </div>
      )}

      {loading ? (
        <div className="bg-white rounded-xl shadow p-6">
          <p className="text-gray-500 text-sm">Cargando...</p>
        </div>
      ) : !pet ? (
        <div className="bg-white rounded-xl shadow p-6">
          <p className="text-gray-700 font-medium">No se encontró la mascota</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-xl shadow p-6">
            <h1 className="text-xl font-bold text-gray-900">{pet.name}</h1>
            <p className="text-sm text-gray-500 mt-1">
              Estado: <span className="font-medium">{pet.status}</span>
            </p>

            {pet.description && (
              <p className="text-sm text-gray-700 mt-4">{pet.description}</p>
            )}

            <div className="mt-6 text-sm text-gray-600 space-y-1">
              <p>
                <span className="font-medium">Lat:</span> {pet.location.lat}
              </p>
              <p>
                <span className="font-medium">Lng:</span> {pet.location.lng}
              </p>
              {pet.location.updatedAt && (
                <p className="pt-2 text-xs text-gray-400">
                  Actualizado:{" "}
                  {new Date(pet.location.updatedAt).toLocaleString()}
                </p>
              )}
            </div>
          </div>

          <GoogleMapView
            center={{ lat: pet.location.lat, lng: pet.location.lng }}
            title={pet.name}
            subtitle={`Estado: ${pet.status}`}
            heightClassName="h-[420px]"
          />
        </div>
      )}
    </AppShell>
  );
}
