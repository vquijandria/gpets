import { Link } from "react-router-dom";
import type { Pet } from "../../domain/models/Pet";

function badge(status: Pet["status"]) {
  const base = "inline-flex items-center rounded-full px-2.5 py-1 text-xs font-medium";
  if (status === "LOST") return `${base} bg-red-50 text-red-700`;
  if (status === "FOUND") return `${base} bg-amber-50 text-amber-700`;
  return `${base} bg-emerald-50 text-emerald-700`;
}

export default function PetCard({ pet }: { pet: Pet }) {
  return (
    <Link
      to={`/pets/${pet.id}`}
      className="group rounded-2xl border border-slate-200 bg-white p-4 shadow-sm hover:shadow-md transition"
    >
      <div className="flex items-start justify-between gap-3">
        <div>
          <p className="text-sm font-semibold text-slate-900 group-hover:text-blue-700 transition">
            {pet.name}
          </p>
          
        </div>
        <span className={badge(pet.status)}>{pet.status}</span>
      </div>

      <div className="mt-4 rounded-xl bg-slate-50 border border-slate-100 p-3">
        <p className="text-xs text-slate-600">
          <span className="font-medium">Ubicación:</span>{" "}
          {pet.location.lat.toFixed(5)}, {pet.location.lng.toFixed(5)}
        </p>
      </div>
    </Link>
  );
}
