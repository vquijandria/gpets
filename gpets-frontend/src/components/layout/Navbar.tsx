import { Link, NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../auth/useAuth";
import logo from "../../assets/gpets-logo.png";

function cn(...classes: Array<string | false | undefined>) {
  return classes.filter(Boolean).join(" ");
}

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  return (
    <header className="sticky top-0 z-40 h-16 border-b border-slate-200 bg-white/80 backdrop-blur">

      <div className="mx-auto max-w-6xl px-4 sm:px-6 lg:px-8 h-full">
        <div className="flex items-center justify-between gap-4 h-full">
          <Link to="/pets" className="flex items-center h-full">
  <div className="h-16 w-[180px] overflow-hidden flex items-center">
    <img
      src={logo}
      alt="gpets logo"
      className="h-full w-auto object-contain scale-[1.8] origin-left"
    />
  </div>
</Link>

          {user && (
            <nav className="hidden md:flex items-center gap-1">
              <NavLink
                to="/pets"
                className={({ isActive }) =>
                  cn(
                    "px-3 py-2 rounded-xl text-sm font-medium transition",
                    isActive
                      ? "bg-blue-50 text-blue-700"
                      : "text-slate-600 hover:bg-slate-100"
                  )
                }
              >
                Mascotas
              </NavLink>

              <NavLink
                to="/owner"
                className={({ isActive }) =>
                  cn(
                    "px-3 py-2 rounded-xl text-sm font-medium transition",
                    isActive
                      ? "bg-blue-50 text-blue-700"
                      : "text-slate-600 hover:bg-slate-100"
                  )
                }
              >
                Dueño
              </NavLink>
            </nav>
          )}

          <div className="flex items-center gap-2 sm:gap-3">
            {user && (
              <div className="hidden sm:flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3 py-2">
                <div className="h-7 w-7 rounded-full bg-blue-100 text-blue-700 grid place-items-center text-xs font-semibold">
                  {user.email?.slice(0, 1).toUpperCase()}
                </div>
                <div className="max-w-[180px]">
                  <p className="truncate text-xs font-medium text-slate-900">
                    {user.displayName ?? "Usuario"}
                  </p>
                  <p className="truncate text-[11px] text-slate-500">
                    {user.email}
                  </p>
                </div>
              </div>
            )}

            {user && (
              <button
                onClick={handleLogout}
                className="rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-100 transition"
              >
                Salir
              </button>
            )}
          </div>
        </div>

        {/* Mobile nav */}
        {user && (
          <div className="mt-3 flex md:hidden gap-2">
            <NavLink
              to="/pets"
              className={({ isActive }) =>
                cn(
                  "flex-1 text-center px-3 py-2 rounded-xl text-sm font-medium transition",
                  isActive ? "bg-blue-600 text-white" : "bg-slate-100 text-slate-700"
                )
              }
            >
              Mascotas
            </NavLink>
            <NavLink
              to="/owner"
              className={({ isActive }) =>
                cn(
                  "flex-1 text-center px-3 py-2 rounded-xl text-sm font-medium transition",
                  isActive ? "bg-blue-600 text-white" : "bg-slate-100 text-slate-700"
                )
              }
            >
              Dueño
            </NavLink>
          </div>
        )}
      </div>
    </header>
  );
}
