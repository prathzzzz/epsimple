import { Building2, Factory, MapPin, Package, Server } from 'lucide-react';

export const LOCATION_TYPES = {
  SITE: 'site',
  WAREHOUSE: 'warehouse',
  DATACENTER: 'datacenter',
} as const;

export type LocationType = typeof LOCATION_TYPES[keyof typeof LOCATION_TYPES];

export const LOCATION_TYPE_LABELS: Record<string, string> = {
  [LOCATION_TYPES.SITE]: 'Site',
  [LOCATION_TYPES.WAREHOUSE]: 'Warehouse',
  [LOCATION_TYPES.DATACENTER]: 'Datacenter',
  factory: 'Factory',
};

export const getLocationIcon = (locationType?: string) => {
  switch (locationType?.toLowerCase()) {
    case LOCATION_TYPES.SITE:
      return <Building2 className="h-4 w-4" />;
    case LOCATION_TYPES.WAREHOUSE:
      return <Package className="h-4 w-4" />;
    case LOCATION_TYPES.DATACENTER:
      return <Server className="h-4 w-4" />;
    case 'factory':
      return <Factory className="h-4 w-4" />;
    default:
      return <MapPin className="h-4 w-4" />;
  }
};

export const getLocationBadgeColor = (locationType?: string) => {
  switch (locationType?.toLowerCase()) {
    case LOCATION_TYPES.SITE:
      return 'bg-blue-500/10 text-blue-700 dark:text-blue-400 border-blue-500/20';
    case LOCATION_TYPES.WAREHOUSE:
      return 'bg-purple-500/10 text-purple-700 dark:text-purple-400 border-purple-500/20';
    case LOCATION_TYPES.DATACENTER:
      return 'bg-green-500/10 text-green-700 dark:text-green-400 border-green-500/20';
    case 'factory':
      return 'bg-orange-500/10 text-orange-700 dark:text-orange-400 border-orange-500/20';
    default:
      return 'bg-gray-500/10 text-gray-700 dark:text-gray-400 border-gray-500/20';
  }
};

export const getLocationLabel = (locationType?: string): string => {
  if (!locationType) return 'Unknown';
  return LOCATION_TYPE_LABELS[locationType.toLowerCase()] || 'Unknown';
};
