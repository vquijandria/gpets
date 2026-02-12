import { useEffect, useState } from "react";
import AppShell from "../components/layout/AppShell";
import { useAuth } from "../auth/useAuth";
import { ownersService } from "../services/ownersService";

export default function OwnerPage() {
  const { user } = useAuth();

  const [fullName, setFullName] = useState(user?.displayName ?? "");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);


  useEffect(() => {
    const loadOwner = async () => {
      setLoading(true);
      setError(null);

      try {
        const owner = await ownersService.getMe();
        setFullName(owner.fullName ?? "");
        setPhone(owner.phone ?? "");
        setAddress(owner.address ?? "");
      } catch {
        console.warn("Owner no registrado aún.");
      } finally {
        setLoading(false);
      }
    };

    void loadOwner();
  }, []);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);
    setSuccess(false);

    try {
      await ownersService.createOwner({
        fullName,
        phone,
        address,
      });

      setSuccess(true);
    } catch (err) {
      console.error(err);
      setError("No se pudo guardar el perfil de dueño.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <AppShell>
      <div className="max-w-xl mx-auto bg-white rounded-xl shadow p-6">
        <h1 className="text-xl font-bold text-gray-900">Perfil de dueño</h1>
        <p className="text-sm text-gray-500 mt-1">
          Completa tus datos para registrar tu perfil.
        </p>

        {error && (
          <div className="mt-4 rounded-lg bg-red-50 border border-red-200 p-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {success && (
          <div className="mt-4 rounded-lg bg-green-50 border border-green-200 p-3 text-sm text-green-700">
            Perfil guardado correctamente.
          </div>
        )}

        {loading ? (
          <div className="mt-6 text-sm text-gray-500">Cargando...</div>
        ) : (
          <form onSubmit={handleSubmit} className="mt-6 space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Email
              </label>
              <input
                value={user?.email ?? ""}
                disabled
                className="mt-1 w-full rounded-lg border border-gray-200 bg-gray-100 px-3 py-2 text-sm"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Nombre completo
              </label>
              <input
                value={fullName}
                onChange={(e) => setFullName(e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-200 bg-gray-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
                placeholder="Tu nombre"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Teléfono
              </label>
              <input
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-200 bg-gray-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
                placeholder="+51..."
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">
                Dirección
              </label>
              <input
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                className="mt-1 w-full rounded-lg border border-gray-200 bg-gray-50 px-3 py-2 text-sm outline-none focus:border-blue-500 focus:bg-white"
                placeholder="Distrito, calle, referencia"
              />
            </div>

            <button
              type="submit"
              disabled={saving}
              className="w-full rounded-lg bg-blue-600 px-4 py-3 text-white font-medium hover:bg-blue-700 transition disabled:opacity-60"
            >
              {saving ? "Guardando..." : "Guardar"}
            </button>
          </form>
        )}
      </div>
    </AppShell>
  );
}
