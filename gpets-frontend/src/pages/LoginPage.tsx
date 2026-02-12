import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/useAuth";

export default function LoginPage() {
  const { login, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user) navigate("/pets");
  }, [user, navigate]);

  const handleLogin = async () => {
    await login();
    navigate("/pets");
  };

  return (
    <div className="min-h-screen bg-slate-50">
      <div className="absolute inset-0 bg-gradient-to-br from-blue-50 via-white to-slate-50" />

      <div className="relative min-h-screen flex items-center justify-center px-4">
        <div className="w-full max-w-md rounded-2xl border border-slate-200 bg-white shadow-sm p-7 sm:p-8">
          <div className="flex items-center gap-3">
            <div className="h-10 w-10 rounded-2xl bg-blue-600 text-white grid place-items-center font-semibold">
              G
            </div>
            <div>
              <h1 className="text-lg font-semibold text-slate-900">gpets</h1>
              <p className="text-sm text-slate-500">Acceso seguro con Google</p>
            </div>
          </div>

          <button
            onClick={handleLogin}
            className="mt-6 w-full rounded-xl bg-blue-600 px-4 py-3 text-sm font-semibold text-white hover:bg-blue-700 transition shadow-sm"
          >
            Iniciar sesión con Google
          </button>

          <p className="mt-4 text-xs text-slate-400 leading-relaxed">
            Esta interfaz es básica (como pidieron). Lo importante es el consumo
            de servicios y la seguridad del backend.
          </p>
        </div>
      </div>
    </div>
  );
}
