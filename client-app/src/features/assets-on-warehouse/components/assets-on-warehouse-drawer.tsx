import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2 } from 'lucide-react';
import { useQuery } from '@tanstack/react-query';
import { format } from 'date-fns';

import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { DatePicker } from '@/components/date-picker';
import { AssetPlacementConfirmDialog } from '@/components/asset-placement-confirm-dialog';
import { useAssetsOnWarehouse } from '../context/assets-on-warehouse-provider';
import { assetsOnWarehouseApi } from '../api/assets-on-warehouse-api';
import { assetsOnWarehouseFormSchema, type AssetsOnWarehouseFormData } from '../api/schema';
import { assetsApi } from '@/features/assets/api/assets-api';
import { warehouseApi } from '@/features/warehouses/api/warehouse-api';
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api';
import { activityWorkApi } from '@/features/activity-works/api/activity-work-api';
import { assetLocationApi, type AssetLocationCheck } from '@/features/assets/api/asset-location-api';

export function AssetsOnWarehouseDrawer() {
  const { isDrawerOpen, closeDrawer, selectedPlacement } = useAssetsOnWarehouse();
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [existingLocation, setExistingLocation] = useState<AssetLocationCheck | null>(null);
  const [pendingData, setPendingData] = useState<AssetsOnWarehouseFormData | null>(null);

  const createMutation = assetsOnWarehouseApi.useCreate();
  const updateMutation = assetsOnWarehouseApi.useUpdate();

  const { data: assetsResponse } = useQuery({
    queryKey: ['assets', 'list'],
    queryFn: () => assetsApi.getList(),
  });
  const assets = assetsResponse || [];

  const { data: warehousesResponse } = useQuery({
    queryKey: ['warehouses', 'list'],
    queryFn: () => warehouseApi.getList(),
  });
  const warehouses = warehousesResponse || [];

  const { data: statusTypesResponse } = useQuery({
    queryKey: ['generic-status-types', 'list'],
    queryFn: () => genericStatusTypeApi.getList(),
  });
  const statusTypes = statusTypesResponse?.data || [];

  const { data: activityWorksResponse } = useQuery({
    queryKey: ['activity-works', 'list'],
    queryFn: () => activityWorkApi.getList(),
  });
  const activityWorks = activityWorksResponse || [];

  const form = useForm<AssetsOnWarehouseFormData>({
    resolver: zodResolver(assetsOnWarehouseFormSchema),
    defaultValues: {
      assetId: 0,
      warehouseId: 0,
      assetStatusId: 0,
      activityWorkId: null,
      assignedOn: '',
      deliveredOn: '',
      commissionedOn: '',
      vacatedOn: '',
      disposedOn: '',
      scrappedOn: '',
    },
  });

  useEffect(() => {
    if (selectedPlacement) {
      form.reset({
        assetId: selectedPlacement.assetId,
        warehouseId: selectedPlacement.warehouseId,
        assetStatusId: selectedPlacement.assetStatusId,
        activityWorkId: selectedPlacement.activityWorkId || null,
        assignedOn: selectedPlacement.assignedOn || '',
        deliveredOn: selectedPlacement.deliveredOn || '',
        commissionedOn: selectedPlacement.commissionedOn || '',
        vacatedOn: selectedPlacement.vacatedOn || '',
        disposedOn: selectedPlacement.disposedOn || '',
        scrappedOn: selectedPlacement.scrappedOn || '',
      });
    } else {
      form.reset();
    }
  }, [selectedPlacement, form]);

  const handleActualSubmit = (data: AssetsOnWarehouseFormData) => {
    const payload = {
      ...data,
      activityWorkId: data.activityWorkId || undefined,
    };

    if (selectedPlacement) {
      updateMutation.mutate(
        {
          id: selectedPlacement.id,
          data: payload,
        },
        {
          onSuccess: () => {
            closeDrawer();
            form.reset();
            setExistingLocation(null);
            setPendingData(null);
          },
        }
      );
    } else {
      createMutation.mutate(payload, {
        onSuccess: () => {
          closeDrawer();
          form.reset();
          setExistingLocation(null);
          setPendingData(null);
        },
      });
    }
  };

  const onSubmit = async (data: AssetsOnWarehouseFormData) => {
    // If updating existing placement, no need to check
    if (selectedPlacement) {
      handleActualSubmit(data);
      return;
    }

    // Check if asset is already placed elsewhere
    try {
      const locationCheck = await assetLocationApi.checkLocation(data.assetId);
      
      if (locationCheck.locationType) {
        // Asset is already placed somewhere (if locationType exists, asset is placed)
        setExistingLocation(locationCheck);
        setPendingData(data);
        setShowConfirmDialog(true);
      } else {
        // Asset is not placed anywhere, proceed normally
        handleActualSubmit(data);
      }
    } catch (error) {
      console.error('Error checking asset location:', error);
      // If check fails, proceed with submission (backend will validate)
      handleActualSubmit(data);
    }
  };

  const handleConfirmMove = async () => {
    setShowConfirmDialog(false);
    if (pendingData && existingLocation) {
      try {
        // First, remove from current location and WAIT for it to complete
        await assetLocationApi.removeFromCurrentLocation(pendingData.assetId);
        
        // Add a small delay to ensure transaction is committed
        await new Promise(resolve => setTimeout(resolve, 100));
        
        // Then place in new location
        handleActualSubmit(pendingData);
      } catch (error) {
        console.error('Error removing asset from current location:', error);
        // Show error to user
        alert('Failed to remove asset from current location. Please try again.');
        setExistingLocation(null);
        setPendingData(null);
      }
    }
  };

  const handleCancelMove = () => {
    setShowConfirmDialog(false);
    setExistingLocation(null);
    setPendingData(null);
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col sm:max-w-[650px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {selectedPlacement ? 'Update' : 'Place'} Asset in Warehouse
          </SheetTitle>
          <SheetDescription>
            {selectedPlacement
              ? 'Update the asset placement details.'
              : 'Place an asset in a warehouse.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="assets-on-warehouse-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="assetId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Asset *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select an asset" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {assets.map((asset) => (
                        <SelectItem key={asset.id} value={String(asset.id)}>
                          [{asset.assetTagId}] - {asset.assetName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="warehouseId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Warehouse *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a warehouse" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {warehouses.map((warehouse) => (
                        <SelectItem key={warehouse.id} value={String(warehouse.id)}>
                          {warehouse.warehouseName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="assetStatusId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Status *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a status" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {statusTypes.map((status) => (
                        <SelectItem key={status.id} value={String(status.id)}>
                          {status.statusName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="activityWorkId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Activity Work (Optional)</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(value === 'none' ? null : Number(value))}
                    value={field.value ? String(field.value) : 'none'}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select activity work" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      <SelectItem value="none">None</SelectItem>
                      {activityWorks.map((work: any) => (
                        <SelectItem key={work.id} value={String(work.id)}>
                          {work.activitiesName} - {work.vendorName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="assignedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Assigned On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="deliveredOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Delivered On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="commissionedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Commissioned On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="vacatedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Vacated On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="disposedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Disposed On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="scrappedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Scrapped On</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, 'yyyy-MM-dd') : '')
                      }
                      placeholder="Select date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 px-4">
          <Button
            type="button"
            variant="outline"
            onClick={closeDrawer}
            disabled={isLoading}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="assets-on-warehouse-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {selectedPlacement ? 'Update' : 'Place'}
          </Button>
        </SheetFooter>
      </SheetContent>

      {existingLocation?.locationType && (
        <AssetPlacementConfirmDialog
          open={showConfirmDialog}
          onOpenChange={handleCancelMove}
          onConfirm={handleConfirmMove}
          assetTagId={existingLocation.assetTagId}
          currentLocationType={existingLocation.locationType}
          currentLocationName={existingLocation.locationName || ''}
          currentLocationCode={existingLocation.locationCode || ''}
          newLocationType="warehouse"
        />
      )}
    </Sheet>
  );
}
