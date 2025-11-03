import { format } from 'date-fns';
import {
  Calendar,
  CheckCircle2,
  Loader2,
  MapPin,
  MapPinOff,
  FileText,
} from 'lucide-react';
import { assetMovementApi } from '../api/asset-movement-api';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { cn } from '@/lib/utils';
import { getLocationIcon, getLocationBadgeColor, getLocationLabel } from '../lib/location-utils';

interface AssetCurrentLocationProps {
  assetId: number;
  assetTagId?: string;
}

export function AssetCurrentLocation({ assetId, assetTagId }: AssetCurrentLocationProps) {
  const { data, isLoading, error } = assetMovementApi.useCurrentLocation(assetId);

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <MapPin className="h-5 w-5" />
            Current Location
          </CardTitle>
          <CardDescription>
            {assetTagId && `Asset: ${assetTagId}`}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex items-center justify-center py-8">
            <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
          </div>
        </CardContent>
      </Card>
    );
  }

  if (error) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <MapPin className="h-5 w-5" />
            Current Location
          </CardTitle>
          <CardDescription>
            {assetTagId && `Asset: ${assetTagId}`}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-destructive">Failed to load current location</p>
        </CardContent>
      </Card>
    );
  }

  const isPlaced = data?.isPlaced;

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          {isPlaced ? <MapPin className="h-5 w-5" /> : <MapPinOff className="h-5 w-5" />}
          Current Location
        </CardTitle>
        <CardDescription>
          {assetTagId && `Asset: ${assetTagId}`}
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!isPlaced ? (
          <div className="flex flex-col items-center justify-center py-8 text-center">
            <div className="rounded-full bg-muted p-3 mb-4">
              <MapPinOff className="h-8 w-8 text-muted-foreground" />
            </div>
            <p className="text-sm font-medium">Not Currently Placed</p>
            <p className="text-xs text-muted-foreground mt-1">
              This asset is not assigned to any location
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Location Type Badge */}
            <div className="flex items-center justify-center">
              <Badge
                variant="outline"
                className={cn(
                  'flex items-center gap-2 px-4 py-2 text-base',
                  getLocationBadgeColor(data?.locationType)
                )}
              >
                <span className="h-5 w-5 flex items-center justify-center">
                  {getLocationIcon(data?.locationType)}
                </span>
                <span className="font-semibold">{getLocationLabel(data?.locationType)}</span>
              </Badge>
            </div>

            <Separator />

            {/* Location Details */}
            <div className="space-y-3">
              <div className="flex items-start justify-between gap-4">
                <div className="flex-1 space-y-1">
                  <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide">
                    Location Name
                  </p>
                  <p className="text-sm font-medium">{data?.locationName || '—'}</p>
                </div>
                <div className="flex-1 space-y-1">
                  <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide">
                    Location Code
                  </p>
                  <p className="text-sm font-mono font-medium">{data?.locationCode || '—'}</p>
                </div>
              </div>

              {/* Dates */}
              {(data?.assignedOn || data?.deliveredOn) && (
                <>
                  <Separator />
                  <div className="grid grid-cols-2 gap-4">
                    {data?.assignedOn && (
                      <div className="space-y-1">
                        <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide flex items-center gap-1">
                          <Calendar className="h-3 w-3" />
                          Assigned On
                        </p>
                        <p className="text-sm">
                          {format(new Date(data.assignedOn), 'PP')}
                        </p>
                      </div>
                    )}
                    {data?.deliveredOn && (
                      <div className="space-y-1">
                        <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide flex items-center gap-1">
                          <CheckCircle2 className="h-3 w-3" />
                          Delivered On
                        </p>
                        <p className="text-sm">
                          {format(new Date(data.deliveredOn), 'PP')}
                        </p>
                      </div>
                    )}
                  </div>
                </>
              )}

              {/* Status and Work Order */}
              {(data?.assetStatusName || data?.activityWorkNumber) && (
                <>
                  <Separator />
                  <div className="grid grid-cols-2 gap-4">
                    {data?.assetStatusName && (
                      <div className="space-y-1">
                        <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide">
                          Status
                        </p>
                        <Badge variant="secondary" className="font-medium">
                          {data.assetStatusName}
                        </Badge>
                      </div>
                    )}
                    {data?.activityWorkNumber && (
                      <div className="space-y-1">
                        <p className="text-xs font-medium text-muted-foreground uppercase tracking-wide flex items-center gap-1">
                          <FileText className="h-3 w-3" />
                          Work Order
                        </p>
                        <p className="text-sm font-mono">{data.activityWorkNumber}</p>
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
