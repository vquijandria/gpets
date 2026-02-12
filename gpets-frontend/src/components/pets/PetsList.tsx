import { Link } from "react-router-dom";
import type { Pet } from "../../domain/models/Pet";
import { useAuth } from "../../auth/useAuth";
import { petsService } from "../../services/petsService";

type Props = {
  pets: Pet[];
  loading: boolean;
  onRefresh: () => Promise<void>;
};

export default function PetsList({ pets, loading, onRefresh }: Props) {
  const { user } = useAuth();

  const claim = async (petId: string) => {
    await petsService.claimPet(petId);
    await onRefresh();
  };

  function statusBadge(status: string) {
  switch (status) {
    case "LOST":
      return "bg-red-50 text-red-700 border-red-200";
    case "FOUND":
      return "bg-blue-50 text-blue-700 border-blue-200";
    case "HOME":
      return "bg-emerald-50 text-emerald-700 border-emerald-200";
    default:
      return "bg-slate-50 text-slate-700 border-slate-200";
  }
}

  if (loading) {
    return (
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
        <p className="text-sm text-slate-500">Cargando mascotas...</p>
      </div>
    );
  }

  if (!pets?.length) {
    return (
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6">
        <p className="text-sm text-slate-500">No hay mascotas registradas.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      {pets.map((pet) => {
        const isMine = !!user?.uid && pet.ownerId === user.uid;
        const hasOwner = !!pet.ownerId;
        const canClaim = !!user?.uid && !hasOwner;

        return (
          <Link
            key={pet.id}
            to={`/pets/${pet.id}`}
            className="group block"
          >
            <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-5 transition group-hover:shadow-md group-hover:border-slate-300">
              <div className="flex items-start justify-between gap-3">
                <div>
                  <h3 className="text-lg font-semibold text-slate-900">
                    {pet.name}
                  </h3>
                  <div className="mt-2">
  <span
    className={`inline-flex items-center rounded-full border px-3 py-1 text-xs font-semibold ${statusBadge(
      pet.status
    )}`}
  >
    {pet.status}
  </span>
</div>

                </div>

                {isMine ? (
                  <span className="shrink-0 rounded-full bg-emerald-50 border border-emerald-200 px-3 py-1 text-xs font-semibold text-emerald-700">
                    Tu mascota
                  </span>
                ) : hasOwner ? (
                  <span className="shrink-0 rounded-full bg-slate-50 border border-slate-200 px-3 py-1 text-xs font-semibold text-slate-600">
                    Con dueño
                  </span>
                ) : (
                  <span className="shrink-0 rounded-full bg-blue-50 border border-blue-200 px-3 py-1 text-xs font-semibold text-blue-700">
                    Sin dueño
                  </span>
                )}
              </div>

              <div className="mt-4 text-xs text-slate-500 space-y-1">
                <div>
                  <span className="font-medium text-slate-600">Lat:</span>{" "}
                  {pet.location.lat}
                </div>
                <div>
                  <span className="font-medium text-slate-600">Lng:</span>{" "}
                  {pet.location.lng}
                </div>
              </div>

              <div className="mt-5 flex items-center justify-between gap-3">
                <span className="text-xs text-slate-400">
                  Ver detalle →
                </span>

                {canClaim ? (
                  <button
                    type="button"
                    onClick={async (e) => {
                      e.preventDefault();
                      e.stopPropagation();
                      try {
                        await claim(pet.id);
                      } catch (err) {
                        console.error(err);
                        alert("No se pudo reclamar la mascota.");
                      }
                    }}
                    className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700 transition"
                  >
                    Reclamar
                  </button>
                ) : !user ? (
                  <span className="text-xs text-slate-400">
                    Inicia sesión para reclamar
                  </span>
                ) : (
                  <span className="text-xs text-slate-400">
                    {isMine ? "Asignada a ti" : hasOwner ? "No disponible" : ""}
                  </span>
                )}
              </div>
            </div>
          </Link>
        );
      })}
    </div>
  );
}
