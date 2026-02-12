import { GoogleMap, MarkerF, InfoWindowF, useLoadScript } from "@react-google-maps/api";
import { useMemo, useState } from "react";

type LatLng = { lat: number; lng: number };

interface Props {
  center: LatLng;
  title?: string;
  subtitle?: string;
  heightClassName?: string; 
  zoom?: number;
}

export default function GoogleMapView({
  center,
  title,
  subtitle,
  heightClassName = "h-72",
  zoom = 15,
}: Props) {
  const apiKey = import.meta.env.VITE_GOOGLE_MAPS_API_KEY as string | undefined;

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: apiKey ?? "",
  });

  const [infoOpen, setInfoOpen] = useState(true);

  const mapOptions = useMemo<google.maps.MapOptions>(
    () => ({
      disableDefaultUI: false,
      clickableIcons: false,
      streetViewControl: false,
      mapTypeControl: false,
      fullscreenControl: true,
      zoomControl: true,
    }),
    []
  );

  if (!apiKey) {
    return (
      <div className={`w-full ${heightClassName} rounded-xl bg-white shadow flex items-center justify-center`}>
        <p className="text-sm text-red-600">
          Falta VITE_GOOGLE_MAPS_API_KEY en el .env
        </p>
      </div>
    );
  }

  if (loadError) {
    return (
      <div className={`w-full ${heightClassName} rounded-xl bg-white shadow flex items-center justify-center`}>
        <p className="text-sm text-red-600">
          Error cargando Google Maps. Revisa tu API key y permisos.
        </p>
      </div>
    );
  }

  if (!isLoaded) {
    return (
      <div className={`w-full ${heightClassName} rounded-xl bg-white shadow flex items-center justify-center`}>
        <div className="flex items-center gap-2 text-gray-500 text-sm">
          <span className="inline-block h-4 w-4 animate-spin rounded-full border-2 border-gray-300 border-t-transparent" />
          Cargando mapa...
        </div>
      </div>
    );
  }
  console.log("KEY:", import.meta.env.VITE_GOOGLE_MAPS_API_KEY);

  return (
    <div className={`w-full ${heightClassName} rounded-xl overflow-hidden shadow bg-white`}>
      <GoogleMap
        mapContainerClassName="w-full h-full"
        center={center}
        zoom={zoom}
        options={mapOptions}
        onClick={() => setInfoOpen(false)}
      >
        <MarkerF position={center} onClick={() => setInfoOpen(true)} />

        {infoOpen && (title || subtitle) && (
          <InfoWindowF position={center} onCloseClick={() => setInfoOpen(false)}>
            <div className="min-w-[180px]">
              {title && <p className="font-semibold text-gray-900">{title}</p>}
              {subtitle && <p className="text-sm text-gray-600 mt-1">{subtitle}</p>}
              <p className="text-xs text-gray-400 mt-2">
                {center.lat.toFixed(5)}, {center.lng.toFixed(5)}
              </p>
            </div>
          </InfoWindowF>
        )}
      </GoogleMap>
    </div>
  );
}
