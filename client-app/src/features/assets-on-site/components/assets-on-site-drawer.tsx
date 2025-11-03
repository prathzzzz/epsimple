import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2, Check, ChevronsUpDown } from 'lucide-react';
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
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
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
import { cn } from '@/lib/utils';
import { useAssetsOnSite } from '../context/assets-on-site-provider';
import { assetsOnSiteApi } from '../api/assets-on-site-api';
import { assetsOnSiteFormSchema, type AssetsOnSiteFormData } from '../api/schema';
import { assetsApi } from '@/features/assets/api/assets-api';
import { siteApi } from '@/features/sites/api/site-api';
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api';
import { activityWorkApi } from '@/features/activity-works/api/activity-work-api';
import { assetLocationApi, type AssetLocationCheck } from '@/features/assets/api/asset-location-api';

export function AssetsOnSiteDrawer() {
  const { isDrawerOpen, closeDrawer, selectedPlacement } = useAssetsOnSite();
  const [showConfirmDialog, setShowConfirmDialog] = useState(false);
  const [existingLocation, setExistingLocation] = useState<AssetLocationCheck | null>(null);
  const [pendingData, setPendingData] = useState<AssetsOnSiteFormData | null>(null);
  const [assetComboOpen, setAssetComboOpen] = useState(false);
  const [assetSearchTerm, setAssetSearchTerm] = useState('');
  const [siteComboOpen, setSiteComboOpen] = useState(false);
  const [siteSearchTerm, setSiteSearchTerm] = useState('');
  const [statusComboOpen, setStatusComboOpen] = useState(false);
  const [statusSearchTerm, setStatusSearchTerm] = useState('');
  const [activityWorkComboOpen, setActivityWorkComboOpen] = useState(false);
  const [activityWorkSearchTerm, setActivityWorkSearchTerm] = useState('');

  const createMutation = assetsOnSiteApi.useCreate();
  const updateMutation = assetsOnSiteApi.useUpdate();

  // Get initial asset list (for display when no search)
  const { data: assetsResponse } = useQuery({
    queryKey: ['assets', 'list'],
    queryFn: () => assetsApi.getList(),
  });
  const initialAssets = assetsResponse || [];

  // Server-side search for assets
  const { data: searchResults, isLoading: isSearching } = assetsApi.useSearch(assetSearchTerm);
  
  // Use search results if searching, otherwise use initial list
  const assets = assetSearchTerm.trim().length > 0 ? (searchResults || []) : initialAssets;

  // Sites with server-side search
  const { data: sitesResponse } = useQuery({
    queryKey: ['sites', 'list'],
    queryFn: () => siteApi.getList(),
  });
  const initialSites = sitesResponse || [];

  const { data: siteSearchResults, isLoading: isSiteSearching } = siteApi.useSearch(siteSearchTerm);
  const sites = siteSearchTerm.trim().length > 0 ? (siteSearchResults || []) : initialSites;

  // Status types with server-side search
  const { data: statusTypesResponse } = useQuery({
    queryKey: ['generic-status-types', 'list'],
    queryFn: () => genericStatusTypeApi.getList(),
  });
  const initialStatusTypes = statusTypesResponse?.data || [];

  const { data: statusSearchResults, isLoading: isStatusSearching } = genericStatusTypeApi.useSearch(statusSearchTerm);
  const statusTypes = statusSearchTerm.trim().length > 0 ? (statusSearchResults || []) : initialStatusTypes;

  // Activity works with server-side search
  const { data: activityWorksResponse } = useQuery({
    queryKey: ['activity-works', 'list'],
    queryFn: () => activityWorkApi.getList(),
  });
  const initialActivityWorks = activityWorksResponse || [];

  const { data: activityWorkSearchResults, isLoading: isActivityWorkSearching } = activityWorkApi.useSearch({
    searchTerm: activityWorkSearchTerm,
    page: 0,
    size: 50,
  });

  const activityWorks = activityWorkSearchTerm.trim().length > 0
    ? (activityWorkSearchResults?.content || [])
    : initialActivityWorks;

  const form = useForm<AssetsOnSiteFormData>({
    resolver: zodResolver(assetsOnSiteFormSchema),
    defaultValues: {
      assetId: 0,
      siteId: 0,
      assetStatusId: 0,
      activityWorkId: null,
      assignedOn: '',
      deliveredOn: '',
      deployedOn: '',
      activatedOn: '',
      decommissionedOn: '',
    },
  });

  // Get selected asset details
  const selectedAssetId = form.watch('assetId');
  const selectedAsset = assets.find(asset => asset.id === selectedAssetId);

  useEffect(() => {
    if (selectedPlacement) {
      form.reset({
        assetId: selectedPlacement.assetId,
        siteId: selectedPlacement.siteId,
        assetStatusId: selectedPlacement.assetStatusId,
        activityWorkId: selectedPlacement.activityWorkId || null,
        assignedOn: selectedPlacement.assignedOn || '',
        deliveredOn: selectedPlacement.deliveredOn || '',
        deployedOn: selectedPlacement.deployedOn || '',
        activatedOn: selectedPlacement.activatedOn || '',
        decommissionedOn: selectedPlacement.decommissionedOn || '',
      });
    } else {
      form.reset({
        assetId: 0,
        siteId: 0,
        assetStatusId: 0,
        activityWorkId: null,
        assignedOn: '',
        deliveredOn: '',
        deployedOn: '',
        activatedOn: '',
        decommissionedOn: '',
      });
    }
  }, [selectedPlacement, form]);

  const handleActualSubmit = (data: AssetsOnSiteFormData) => {
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

  const onSubmit = async (data: AssetsOnSiteFormData) => {
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
            {selectedPlacement ? 'Update' : 'Place'} Asset on Site
          </SheetTitle>
          <SheetDescription>
            {selectedPlacement
              ? 'Update the asset placement details.'
              : 'Place an asset on a site.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="assets-on-site-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="assetId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Asset *</FormLabel>
                  <Popover open={assetComboOpen} onOpenChange={setAssetComboOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={assetComboOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? (() => {
                                const asset = assets.find((a) => a.id === field.value);
                                return asset ? `[${asset.assetTagId}] - ${asset.assetName}` : "Select an asset";
                              })()
                            : "Select an asset"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[500px] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput 
                          placeholder="Search asset by tag or name..." 
                          value={assetSearchTerm}
                          onValueChange={setAssetSearchTerm}
                        />
                        <CommandList>
                          {isSearching ? (
                            <CommandEmpty>
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin mr-2" />
                                Searching...
                              </div>
                            </CommandEmpty>
                          ) : (
                            <CommandEmpty>No asset found.</CommandEmpty>
                          )}
                          <CommandGroup>
                            {assets.map((asset) => (
                              <CommandItem
                                key={asset.id}
                                value={String(asset.id)}
                                onSelect={() => {
                                  field.onChange(asset.id);
                                  setAssetComboOpen(false);
                                  setAssetSearchTerm('');
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    field.value === asset.id ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                [{asset.assetTagId}] - {asset.assetName}
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

            {/* Display selected asset details */}
            {selectedAsset && (
              <div className="rounded-lg border bg-muted/50 p-4 space-y-2">
                <p className="text-sm font-semibold">Asset Details</p>
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <div>
                    <span className="text-muted-foreground">Tag ID:</span>
                    <p className="font-medium">{selectedAsset.assetTagId}</p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Name:</span>
                    <p className="font-medium">{selectedAsset.assetName}</p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Type:</span>
                    <p className="font-medium">{selectedAsset.assetTypeName}</p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Category:</span>
                    <p className="font-medium">{selectedAsset.assetCategoryName}</p>
                  </div>
                  {selectedAsset.serialNumber && (
                    <div>
                      <span className="text-muted-foreground">Serial No:</span>
                      <p className="font-medium">{selectedAsset.serialNumber}</p>
                    </div>
                  )}
                  <div>
                    <span className="text-muted-foreground">Status:</span>
                    <p className="font-medium">{selectedAsset.statusTypeName}</p>
                  </div>
                  <div>
                    <span className="text-muted-foreground">Vendor:</span>
                    <p className="font-medium">{selectedAsset.vendorName}</p>
                  </div>
                  {selectedAsset.purchaseDate && (
                    <div>
                      <span className="text-muted-foreground">Purchase Date:</span>
                      <p className="font-medium">
                        {format(new Date(selectedAsset.purchaseDate), 'MMM dd, yyyy')}
                      </p>
                    </div>
                  )}
                </div>
              </div>
            )}

            <FormField
              control={form.control}
              name="siteId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Site *</FormLabel>
                  <Popover open={siteComboOpen} onOpenChange={setSiteComboOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={siteComboOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? (() => {
                                const site = sites.find((s) => s.id === field.value);
                                return site ? `[${site.siteCode}] - ${site.locationName}` : "Select a site";
                              })()
                            : "Select a site"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput 
                          placeholder="Search sites..." 
                          value={siteSearchTerm}
                          onValueChange={setSiteSearchTerm}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isSiteSearching ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No site found."
                            )}
                          </CommandEmpty>
                          {sites.map((site) => (
                            <CommandItem
                              key={site.id}
                              value={String(site.id)}
                              onSelect={() => {
                                field.onChange(site.id);
                                setSiteComboOpen(false);
                                setSiteSearchTerm('');
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === site.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              [{site.siteCode}] - {site.locationName}
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="assetStatusId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Status *</FormLabel>
                  <Popover open={statusComboOpen} onOpenChange={setStatusComboOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={statusComboOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? statusTypes.find((status) => status.id === field.value)?.statusName || "Select a status"
                            : "Select a status"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput 
                          placeholder="Search status..." 
                          value={statusSearchTerm}
                          onValueChange={setStatusSearchTerm}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isStatusSearching ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No status found."
                            )}
                          </CommandEmpty>
                          {statusTypes.map((status) => (
                            <CommandItem
                              key={status.id}
                              value={String(status.id)}
                              onSelect={() => {
                                field.onChange(status.id);
                                setStatusComboOpen(false);
                                setStatusSearchTerm('');
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === status.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {status.statusName}
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="activityWorkId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Activity Work (Optional)</FormLabel>
                  <Popover open={activityWorkComboOpen} onOpenChange={setActivityWorkComboOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={activityWorkComboOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? (() => {
                                const work = activityWorks.find((w: any) => w.id === field.value);
                                return work ? `${work.activitiesName} - ${work.vendorName}` : "Select activity work";
                              })()
                            : "Select activity work"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput 
                          placeholder="Search activity work..." 
                          value={activityWorkSearchTerm}
                          onValueChange={setActivityWorkSearchTerm}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isActivityWorkSearching ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No activity work found."
                            )}
                          </CommandEmpty>
                          <CommandItem
                            value="none"
                            onSelect={() => {
                              field.onChange(null);
                              setActivityWorkComboOpen(false);
                              setActivityWorkSearchTerm('');
                            }}
                          >
                            <Check
                              className={cn(
                                "mr-2 h-4 w-4",
                                field.value === null ? "opacity-100" : "opacity-0"
                              )}
                            />
                            None
                          </CommandItem>
                          {activityWorks.map((work: any) => (
                            <CommandItem
                              key={work.id}
                              value={String(work.id)}
                              onSelect={() => {
                                field.onChange(work.id);
                                setActivityWorkComboOpen(false);
                                setActivityWorkSearchTerm('');
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === work.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {work.activitiesName} - {work.vendorName}
                            </CommandItem>
                          ))}
                        </CommandList>
                      </Command>
                    </PopoverContent>
                  </Popover>
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

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="decommissionedOn"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Decommissioned On</FormLabel>
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
            form="assets-on-site-form"
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
          newLocationType="site"
        />
      )}
    </Sheet>
  );
}

