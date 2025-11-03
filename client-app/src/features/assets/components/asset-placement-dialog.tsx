import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { format } from 'date-fns';
import { ChevronsUpDown, Check, Loader2, MapPin } from 'lucide-react';
import { LOCATION_TYPES } from '../lib/location-utils';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
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
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Button } from '@/components/ui/button';
import { DatePicker } from '@/components/date-picker';
import { cn } from '@/lib/utils';
import { toast } from 'sonner';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import api from '@/lib/api';
import { siteApi } from '@/features/sites/api/site-api';
import { warehouseApi } from '@/features/warehouses/api/warehouse-api';
import { datacenterApi } from '@/features/datacenters/api/datacenter-api';
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api';
import { activityWorkApi } from '@/features/activity-works/api/activity-work-api';
import { assetLocationApi, type AssetLocationCheck } from '../api/asset-location-api';
import { AssetPlacementConfirmDialog } from '@/components/asset-placement-confirm-dialog';

const placementFormSchema = z.object({
  locationType: z.enum([LOCATION_TYPES.SITE, LOCATION_TYPES.WAREHOUSE, LOCATION_TYPES.DATACENTER] as const),
  locationId: z.number().min(1, 'Location is required'),
  assetStatusId: z.number().min(1, 'Status is required'),
  activityWorkId: z.number().optional().nullable(),
  assignedOn: z.string().optional().or(z.literal('')),
  deliveredOn: z.string().optional().or(z.literal('')),
  deployedOn: z.string().optional().or(z.literal('')),
  activatedOn: z.string().optional().or(z.literal('')),
  commissionedOn: z.string().optional().or(z.literal('')),
  decommissionedOn: z.string().optional().or(z.literal('')),
  disposedOn: z.string().optional().or(z.literal('')),
  scrappedOn: z.string().optional().or(z.literal('')),
});

type PlacementFormData = z.infer<typeof placementFormSchema>;

interface AssetPlacementDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  assetId: number;
  assetTagId: string;
  assetName: string;
}

export function AssetPlacementDialog({
  open,
  onOpenChange,
  assetId,
  assetTagId,
  assetName,
}: AssetPlacementDialogProps) {
  const queryClient = useQueryClient();
  const [locationType, setLocationType] = useState<typeof LOCATION_TYPES[keyof typeof LOCATION_TYPES]>(LOCATION_TYPES.SITE);
  const [locationOpen, setLocationOpen] = useState(false);
  const [locationSearch, setLocationSearch] = useState('');
  const [statusOpen, setStatusOpen] = useState(false);
  const [statusSearch, setStatusSearch] = useState('');
  const [activityWorkOpen, setActivityWorkOpen] = useState(false);
  const [activityWorkSearch, setActivityWorkSearch] = useState('');
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [existingLocation, setExistingLocation] = useState<AssetLocationCheck | null>(null);
  const [pendingData, setPendingData] = useState<PlacementFormData | null>(null);

  // Fetch initial data for display (using getAll for initial load)
  const { data: initialSitesResponse } = siteApi.useGetAll({ 
    page: 0, 
    size: 50,
    sortBy: 'siteName',
    sortOrder: 'asc'
  });
  const { data: initialWarehousesResponse } = warehouseApi.useGetAll({ 
    page: 0, 
    size: 50,
    sortBy: 'warehouseName',
    sortDirection: 'asc'
  });
  const { data: initialDatacentersResponse } = datacenterApi.useGetAll({ 
    page: 0, 
    size: 50,
    sortBy: 'datacenterName',
    sortDirection: 'asc'
  });
  const { data: initialActivityWorksResponse } = activityWorkApi.useGetAll({
    page: 0,
    size: 50,
  });

  // Fetch search results (only when user is searching)
  const { data: searchedSites = [] } = siteApi.useSearch(locationSearch);
  const { data: searchedWarehousesResponse } = warehouseApi.useSearch({ 
    searchTerm: locationSearch, 
    page: 0, 
    size: 50 
  });
  const { data: searchedDatacentersResponse } = datacenterApi.useSearch({ 
    searchTerm: locationSearch, 
    page: 0, 
    size: 50 
  });
  const { data: searchedActivityWorksResponse } = activityWorkApi.useSearch({
    searchTerm: activityWorkSearch,
    page: 0,
    size: 50,
  });

  // Status types API handles empty search correctly
  const { data: statusTypesData = [] } = genericStatusTypeApi.useSearch(statusSearch);

  // Combine initial and searched results
  const sites = locationSearch.trim() 
    ? searchedSites 
    : (initialSitesResponse?.content || []);
  const warehouses = locationSearch.trim() 
    ? (searchedWarehousesResponse?.content || [])
    : (initialWarehousesResponse?.content || []);
  const datacenters = locationSearch.trim()
    ? (searchedDatacentersResponse?.content || [])
    : (initialDatacentersResponse?.content || []);

  // Get locations based on selected type
  const locations: any[] = locationType === LOCATION_TYPES.SITE
    ? sites 
    : locationType === LOCATION_TYPES.WAREHOUSE
    ? warehouses 
    : datacenters;

  const statusTypes = statusTypesData;

  const activityWorks = activityWorkSearch.trim()
    ? (searchedActivityWorksResponse?.content || [])
    : (initialActivityWorksResponse?.content || []);

  const form = useForm<PlacementFormData>({
    resolver: zodResolver(placementFormSchema),
    defaultValues: {
      locationType: LOCATION_TYPES.SITE,
      locationId: 0,
      assetStatusId: 0,
      activityWorkId: null,
      assignedOn: '',
      deliveredOn: '',
      deployedOn: '',
      activatedOn: '',
      commissionedOn: '',
      decommissionedOn: '',
      disposedOn: '',
      scrappedOn: '',
    },
  });

  const placementMutation = useMutation({
    mutationFn: async (data: PlacementFormData) => {
      const endpoint = 
        data.locationType === LOCATION_TYPES.SITE ? '/api/assets-on-site' :
        data.locationType === LOCATION_TYPES.WAREHOUSE ? '/api/assets-on-warehouse' :
        '/api/assets-on-datacenter';

      const payload = {
        assetId,
        ...(data.locationType === LOCATION_TYPES.SITE && { siteId: data.locationId }),
        ...(data.locationType === LOCATION_TYPES.WAREHOUSE && { warehouseId: data.locationId }),
        ...(data.locationType === LOCATION_TYPES.DATACENTER && { datacenterId: data.locationId }),
        assetStatusId: data.assetStatusId,
        activityWorkId: data.activityWorkId || undefined,
        assignedOn: data.assignedOn || undefined,
        deliveredOn: data.deliveredOn || undefined,
        deployedOn: data.deployedOn || undefined,
        activatedOn: data.activatedOn || undefined,
        commissionedOn: data.commissionedOn || undefined,
        decommissionedOn: data.decommissionedOn || undefined,
        disposedOn: data.disposedOn || undefined,
        scrappedOn: data.scrappedOn || undefined,
      };

      const response = await api.post(endpoint, payload);
      return response.data;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['assets'] });
      queryClient.invalidateQueries({ queryKey: ['assets-on-site'] });
      queryClient.invalidateQueries({ queryKey: ['assets-on-warehouse'] });
      queryClient.invalidateQueries({ queryKey: ['assets-on-Datacenter'] });
      queryClient.invalidateQueries({ queryKey: ['asset-current-location', assetId] });
      queryClient.invalidateQueries({ queryKey: ['asset-movement-history', assetId] });
      toast.success('Asset placed successfully');
      onOpenChange(false);
      form.reset();
    },
    onError: (error: any) => {
      toast.error(error.response?.data?.message || 'Failed to place asset');
    },
  });

  const handleActualSubmit = (data: PlacementFormData) => {
    placementMutation.mutate(data);
  };

  const onSubmit = async (data: PlacementFormData) => {
    // Check if asset is already placed elsewhere
    try {
      const locationCheck = await assetLocationApi.checkLocation(assetId);
      
      if (locationCheck.isPlaced && locationCheck.locationType) {
        // Asset is already placed, show confirmation
        setExistingLocation(locationCheck);
        setPendingData(data);
        setShowConfirmDialog(true);
      } else {
        // Not placed, proceed directly
        handleActualSubmit(data);
      }
    } catch {
      // If location check fails, proceed with placement
      handleActualSubmit(data);
    }
  };

  const handleConfirmMove = () => {
    setShowConfirmDialog(false);
    if (pendingData) {
      handleActualSubmit(pendingData);
      setPendingData(null);
      setExistingLocation(null);
    }
  };

  const handleCancelMove = () => {
    setShowConfirmDialog(false);
    setPendingData(null);
    setExistingLocation(null);
  };

  return (
    <>
      <Dialog open={open} onOpenChange={onOpenChange}>
        <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
          <DialogHeader>
            <DialogTitle className="flex items-center gap-2">
              <MapPin className="h-5 w-5" />
              Place Asset
            </DialogTitle>
            <DialogDescription>
              {assetTagId} - {assetName}
            </DialogDescription>
          </DialogHeader>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              {/* Location Type */}
              <FormField
                control={form.control}
                name="locationType"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Location Type *</FormLabel>
                    <Select
                      value={field.value}
                      onValueChange={(value) => {
                        field.onChange(value);
                        setLocationType(value as any);
                        form.setValue('locationId', 0);
                      }}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select location type" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={LOCATION_TYPES.SITE}>Site</SelectItem>
                        <SelectItem value={LOCATION_TYPES.WAREHOUSE}>Warehouse</SelectItem>
                        <SelectItem value={LOCATION_TYPES.DATACENTER}>Datacenter</SelectItem>
                      </SelectContent>
                    </Select>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Location Selection */}
              <FormField
                control={form.control}
                name="locationId"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>
                      {locationType === LOCATION_TYPES.SITE ? 'Site' : locationType === LOCATION_TYPES.WAREHOUSE ? 'Warehouse' : 'Datacenter'} *
                    </FormLabel>
                    <Popover open={locationOpen} onOpenChange={setLocationOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              'w-full justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? locations.find((l: any) => l.id === field.value)?.[
                                  locationType === LOCATION_TYPES.SITE ? 'siteCode' : 
                                  locationType === LOCATION_TYPES.WAREHOUSE ? 'warehouseName' : 
                                  'datacenterName'
                                ] || 'Select location'
                              : 'Select location'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[400px] p-0">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder={`Search ${locationType}s...`}
                            value={locationSearch}
                            onValueChange={setLocationSearch}
                          />
                          <CommandList>
                            <CommandEmpty>No locations found.</CommandEmpty>
                            <CommandGroup>
                              {locations.map((location: any) => (
                                <CommandItem
                                  key={location.id}
                                  value={String(location.id)}
                                  onSelect={() => {
                                    field.onChange(location.id);
                                    setLocationOpen(false);
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      location.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {locationType === LOCATION_TYPES.SITE
                                    ? `${(location as any).siteCode}` 
                                    : locationType === LOCATION_TYPES.WAREHOUSE
                                    ? (location as any).warehouseName
                                    : (location as any).datacenterName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Status */}
              <FormField
                control={form.control}
                name="assetStatusId"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Asset Status *</FormLabel>
                    <Popover open={statusOpen} onOpenChange={setStatusOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              'w-full justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? statusTypes.find((s) => s.id === field.value)?.statusName
                              : 'Select status'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[400px] p-0">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search statuses..."
                            value={statusSearch}
                            onValueChange={setStatusSearch}
                          />
                          <CommandList>
                            <CommandEmpty>No statuses found.</CommandEmpty>
                            <CommandGroup>
                              {statusTypes.map((status) => (
                                <CommandItem
                                  key={status.id}
                                  value={String(status.id)}
                                  onSelect={() => {
                                    field.onChange(status.id);
                                    setStatusOpen(false);
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      status.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {status.statusName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Activity Work (Optional) */}
              <FormField
                control={form.control}
                name="activityWorkId"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Activity Work (Optional)</FormLabel>
                    <Popover open={activityWorkOpen} onOpenChange={setActivityWorkOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              'w-full justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? activityWorks.find((w) => w.id === field.value)?.vendorOrderNumber
                              : 'Select activity work'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[400px] p-0">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search activity works..."
                            value={activityWorkSearch}
                            onValueChange={setActivityWorkSearch}
                          />
                          <CommandList>
                            <CommandEmpty>No activity works found.</CommandEmpty>
                            <CommandGroup>
                              {activityWorks.map((work) => (
                                <CommandItem
                                  key={work.id}
                                  value={String(work.id)}
                                  onSelect={() => {
                                    field.onChange(work.id);
                                    setActivityWorkOpen(false);
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      work.id === field.value ? 'opacity-100' : 'opacity-0'
                                    )}
                                  />
                                  {work.vendorOrderNumber}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />

              {/* Dates */}
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

              {/* Site-specific dates */}
              {locationType === LOCATION_TYPES.SITE && (
                <div className="grid grid-cols-2 gap-4">
                  <FormField
                    control={form.control}
                    name="deployedOn"
                    render={({ field }) => (
                      <FormItem className="flex flex-col">
                        <FormLabel>Deployed On</FormLabel>
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
                    name="activatedOn"
                    render={({ field }) => (
                      <FormItem className="flex flex-col">
                        <FormLabel>Activated On</FormLabel>
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
              )}

              {/* Warehouse/Datacenter-specific dates */}
              {(locationType === LOCATION_TYPES.WAREHOUSE || locationType === LOCATION_TYPES.DATACENTER) && (
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
              )}

              <DialogFooter>
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => onOpenChange(false)}
                  disabled={placementMutation.isPending}
                >
                  Cancel
                </Button>
                <Button type="submit" disabled={placementMutation.isPending}>
                  {placementMutation.isPending && (
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                  )}
                  Place Asset
                </Button>
              </DialogFooter>
            </form>
          </Form>
        </DialogContent>
      </Dialog>

      {existingLocation?.locationType && (
        <AssetPlacementConfirmDialog
          open={showConfirmDialog}
          onOpenChange={handleCancelMove}
          onConfirm={handleConfirmMove}
          assetTagId={assetTagId}
          currentLocationType={existingLocation.locationType}
          currentLocationName={existingLocation.locationName || ''}
          currentLocationCode={existingLocation.locationCode || ''}
          newLocationType={locationType}
        />
      )}
    </>
  );
}
