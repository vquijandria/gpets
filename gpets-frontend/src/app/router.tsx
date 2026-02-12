import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "../auth/ProtectedRoute";
import LoginPage from "../pages/LoginPage";
import PetsPage from "../pages/PetsPage";
import PetDetailPage from "../pages/PetDetailPage";
import OwnerPage from "../pages/OwnerPage";
import { ROUTES } from "./routes";

export default function AppRouter() {
  return (
    <Routes>
      {/* Public */}
      <Route path={ROUTES.login} element={<LoginPage />} />

      {/* Protected */}
      <Route
        path={ROUTES.pets}
        element={
          <ProtectedRoute>
            <PetsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path={ROUTES.petDetail}
        element={
          <ProtectedRoute>
            <PetDetailPage />
          </ProtectedRoute>
        }
      />

      <Route
        path={ROUTES.owner}
        element={
          <ProtectedRoute>
            <OwnerPage />
          </ProtectedRoute>
        }
      />

      {/* Default */}
      <Route path="/" element={<Navigate to={ROUTES.pets} replace />} />

      {/* 404 */}
      <Route path="*" element={<Navigate to={ROUTES.pets} replace />} />
    </Routes>
  );
}
