import { useState } from 'react';
import { format } from 'date-fns';
import {
  ArrowRight,
  Calendar,
  Loader2,
  PackageOpen,
  TrendingUp,
} from 'lucide-react';
import { assetMovementApi, type AssetMovementHistory } from '../api/asset-movement-api';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Separator } from '@/components/ui/separator';
import { cn } from '@/lib/utils';
import { getLocationIcon, getLocationBadgeColor } from '../lib/location-utils';

interface AssetMovementHistoryProps {
  assetId: number;
  assetTagId?: string;
}

const parseLocationFromMovement = (movement: AssetMovementHistory, direction: 'from' | 'to') => {
  if (direction === 'from') {
    if (movement.fromFactory) {
      return { type: 'Factory', name: movement.fromFactory, code: 'FACTORY' };
    }
    if (movement.fromSiteId) {
      return { type: 'Site', name: movement.fromSiteName || '', code: movement.fromSiteCode || '' };
    }
    if (movement.fromWarehouseId) {
      return { type: 'Warehouse', name: movement.fromWarehouseName || '', code: movement.fromWarehouseCode || '' };
    }
    if (movement.fromDatacenterId) {
      return { type: 'Datacenter', name: movement.fromDatacenterName || '', code: movement.fromDatacenterCode || '' };
    }
    return null;
  } else {
    if (movement.toSiteId) {
      return { type: 'Site', name: movement.toSiteName || '', code: movement.toSiteCode || '' };
    }
    if (movement.toWarehouseId) {
      return { type: 'Warehouse', name: movement.toWarehouseName || '', code: movement.toWarehouseCode || '' };
    }
    if (movement.toDatacenterId) {
      return { type: 'Datacenter', name: movement.toDatacenterName || '', code: movement.toDatacenterCode || '' };
    }
    return null;
  }
};

export function AssetMovementHistory({ assetId, assetTagId }: AssetMovementHistoryProps) {
  const [page, setPage] = useState(0);
  const pageSize = 20;

  const { data, isLoading, error } = assetMovementApi.useMovementHistory(assetId, page, pageSize);

  if (isLoading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <TrendingUp className="h-5 w-5" />
            Movement History
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
            <TrendingUp className="h-5 w-5" />
            Movement History
          </CardTitle>
          <CardDescription>
            {assetTagId && `Asset: ${assetTagId}`}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <p className="text-sm text-destructive">Failed to load movement history</p>
        </CardContent>
      </Card>
    );
  }

  const movements = data?.content || [];
  const hasMovements = movements.length > 0;

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <TrendingUp className="h-5 w-5" />
          Movement History
        </CardTitle>
        <CardDescription>
          {assetTagId && `Asset: ${assetTagId}`}
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!hasMovements ? (
          <div className="flex flex-col items-center justify-center py-12 text-center">
            <PackageOpen className="h-12 w-12 text-muted-foreground/50 mb-4" />
            <p className="text-sm font-medium text-muted-foreground">No movement history</p>
            <p className="text-xs text-muted-foreground mt-1">
              This asset hasn't been moved yet
            </p>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Timeline */}
            <div className="relative space-y-6">
              {movements.map((movement, index) => {
                const fromLocation = parseLocationFromMovement(movement, 'from');
                const toLocation = parseLocationFromMovement(movement, 'to');
                const isLast = index === movements.length - 1;

                return (
                  <div key={movement.id} className="relative pl-8">
                    {/* Timeline line */}
                    {!isLast && (
                      <div className="absolute left-[11px] top-6 bottom-0 w-[2px] bg-border" />
                    )}

                    {/* Timeline dot */}
                    <div className="absolute left-0 top-1 flex h-6 w-6 items-center justify-center rounded-full border-2 border-background bg-primary">
                      <div className="h-2 w-2 rounded-full bg-primary-foreground" />
                    </div>

                    {/* Movement card */}
                    <div className="rounded-lg border bg-card p-4 shadow-sm hover:shadow-md transition-shadow">
                      <div className="flex items-start justify-between gap-4 mb-3">
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-1">
                            <Badge variant="outline" className="font-medium">
                              {movement.movementType}
                            </Badge>
                            <span className="text-xs text-muted-foreground flex items-center gap-1">
                              <Calendar className="h-3 w-3" />
                              {format(new Date(movement.movedAt), 'PPp')}
                            </span>
                          </div>
                          {movement.movementDescription && (
                            <p className="text-xs text-muted-foreground mt-1">
                              {movement.movementDescription}
                            </p>
                          )}
                        </div>
                      </div>

                      {/* From and To locations */}
                      <div className="flex items-center gap-3 flex-wrap">
                        {/* From location */}
                        {fromLocation && (
                          <div className="flex items-center gap-2">
                            <Badge
                              variant="outline"
                              className={cn(
                                'flex items-center gap-1.5 px-2.5 py-1',
                                getLocationBadgeColor(fromLocation.type)
                              )}
                            >
                              {getLocationIcon(fromLocation.type)}
                              <div className="flex flex-col items-start">
                                <span className="text-[10px] uppercase font-semibold opacity-70">
                                  From
                                </span>
                                <span className="font-medium text-xs">
                                  {fromLocation.code || fromLocation.name}
                                </span>
                              </div>
                            </Badge>
                          </div>
                        )}

                        {/* Arrow */}
                        {fromLocation && toLocation && (
                          <ArrowRight className="h-4 w-4 text-muted-foreground flex-shrink-0" />
                        )}

                        {/* To location */}
                        {toLocation && (
                          <div className="flex items-center gap-2">
                            <Badge
                              variant="outline"
                              className={cn(
                                'flex items-center gap-1.5 px-2.5 py-1',
                                getLocationBadgeColor(toLocation.type)
                              )}
                            >
                              {getLocationIcon(toLocation.type)}
                              <div className="flex flex-col items-start">
                                <span className="text-[10px] uppercase font-semibold opacity-70">
                                  To
                                </span>
                                <span className="font-medium text-xs">
                                  {toLocation.code || toLocation.name}
                                </span>
                              </div>
                            </Badge>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Pagination */}
            {data && data.totalPages > 1 && (
              <>
                <Separator />
                <div className="flex items-center justify-between">
                  <p className="text-xs text-muted-foreground">
                    Page {page + 1} of {data.totalPages}
                  </p>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setPage((p) => Math.max(0, p - 1))}
                      disabled={page === 0}
                    >
                      Previous
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => setPage((p) => Math.min(data.totalPages - 1, p + 1))}
                      disabled={page >= data.totalPages - 1}
                    >
                      Next
                    </Button>
                  </div>
                </div>
              </>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
