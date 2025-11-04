import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQuery } from '@tanstack/react-query';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';

import { Button } from '@/components/ui/button';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from '@/components/ui/command';
import { cn } from '@/lib/utils';
import { Sheet, SheetContent, SheetDescription, SheetFooter, SheetHeader, SheetTitle } from '@/components/ui/sheet';
import { useAssetExpenditureAndActivityWork } from '../context/asset-expenditure-and-activity-work-provider';
import { assetExpenditureAndActivityWorkApi } from '../api/asset-expenditure-and-activity-work-api';
import { assetExpenditureAndActivityWorkFormSchema, type AssetExpenditureAndActivityWorkFormData } from '../api/schema';
import { assetsApi } from '@/features/assets/api/assets-api';
import { activityWorkApi } from '@/features/activity-works/api/activity-work-api';
import { expendituresInvoiceApi } from '@/features/expenditures-invoice/api/expenditures-invoice-api';

export function AssetExpenditureAndActivityWorkDrawer() {
  const { isDrawerOpen, closeDrawer, selectedExpenditure, assetId } = useAssetExpenditureAndActivityWork();

  const [assetSearch, setAssetSearch] = useState('');
  const [assetOpen, setAssetOpen] = useState(false);
  const [activityWorkSearch, setActivityWorkSearch] = useState('');
  const [activityWorkOpen, setActivityWorkOpen] = useState(false);
  const [expenditureInvoiceSearch, setExpenditureInvoiceSearch] = useState('');
  const [expenditureInvoiceOpen, setExpenditureInvoiceOpen] = useState(false);

  const createMutation = assetExpenditureAndActivityWorkApi.useCreate();
  const updateMutation = assetExpenditureAndActivityWorkApi.useUpdate();

  const { data: searchedAssets = [] } = assetsApi.useSearch(assetSearch);
  const { data: allAssets = [] } = useQuery({ queryKey: ['assets', 'list'], queryFn: assetsApi.getList, enabled: !!assetId });
  const assets = assetSearch ? searchedAssets : allAssets;
  const isLoadingAssets = false;
  const currentAsset = assetId ? allAssets.find((a: {id: number; assetTagId: string}) => a.id === assetId) : null;

  const { data: activityWorksData, isLoading: isLoadingActivityWorks } = activityWorkApi.useSearch({ searchTerm: activityWorkSearch, page: 0, size: 50 });
  const searchedActivityWorks = activityWorksData?.content || [];
  const { data: allActivityWorks = [] } = useQuery({ queryKey: ['activity-works', 'list'], queryFn: activityWorkApi.getList, enabled: !activityWorkSearch });
  const activityWorks = activityWorkSearch ? searchedActivityWorks : allActivityWorks;

  const { data: expenditureInvoicesData, isLoading: isLoadingExpenditureInvoices } = useQuery({
    queryKey: ['expenditures-invoices', 'list'],
    queryFn: async () => {
      const response = await expendituresInvoiceApi.getAll(0, 100);
      return response.data.content;
    },
  });
  const expenditureInvoices = expenditureInvoicesData || [];

  const form = useForm<AssetExpenditureAndActivityWorkFormData>({
    resolver: zodResolver(assetExpenditureAndActivityWorkFormSchema),
    defaultValues: {
      assetId: assetId || 0,
      activityWorkId: undefined,
      expendituresInvoiceId: undefined,
    },
  });

  useEffect(() => {
    if (selectedExpenditure) {
      form.reset({
        assetId: selectedExpenditure.assetId,
        activityWorkId: selectedExpenditure.activityWorkId || undefined,
        expendituresInvoiceId: selectedExpenditure.expendituresInvoiceId || undefined,
      });
    } else {
      form.reset({
        assetId: assetId || 0,
        activityWorkId: undefined,
        expendituresInvoiceId: undefined,
      });
    }
  }, [selectedExpenditure, form, assetId]);

  const onSubmit = async (data: AssetExpenditureAndActivityWorkFormData) => {
    if (selectedExpenditure) {
      updateMutation.mutate({ id: selectedExpenditure.id, data }, {
        onSuccess: () => {
          closeDrawer();
          form.reset();
        },
      });
    } else {
      createMutation.mutate(data, {
        onSuccess: () => {
          closeDrawer();
          form.reset();
        },
      });
    }
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={(open) => !open && closeDrawer()}>
      <SheetContent className="w-full overflow-y-auto sm:max-w-xl">
        <SheetHeader>
          <SheetTitle>{selectedExpenditure ? 'Edit' : 'Create'} Asset Expenditure</SheetTitle>
          <SheetDescription>
            {selectedExpenditure ? 'Update' : 'Create new'} asset expenditure and activity work linkage
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex flex-col gap-6 px-4 py-4">
            {/* Asset Field */}
            <FormField
              control={form.control}
              name="assetId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Asset *</FormLabel>
                  {assetId ? (
                    <Button type="button" variant="outline" className="w-full justify-start" disabled>
                      {currentAsset?.assetTagId || 'Loading...'}
                    </Button>
                  ) : (
                    <Popover open={assetOpen} onOpenChange={setAssetOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button variant="outline" role="combobox" className={cn('w-full justify-between', !field.value && 'text-muted-foreground')}>
                            {field.value ? assets.find((a) => a.id === field.value)?.assetTagId || 'Select asset...' : 'Select asset...'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[400px] p-0">
                        <Command>
                          <CommandInput placeholder="Search assets..." onValueChange={setAssetSearch} value={assetSearch} />
                          <CommandList>
                            <CommandEmpty>{isLoadingAssets ? 'Loading...' : 'No asset found.'}</CommandEmpty>
                            <CommandGroup>
                              {assets.map((asset) => (
                                <CommandItem key={asset.id} value={`${asset.id}`} onSelect={() => { field.onChange(asset.id); setAssetOpen(false); }}>
                                  <Check className={cn('mr-2 h-4 w-4', field.value === asset.id ? 'opacity-100' : 'opacity-0')} />
                                  {asset.assetTagId} - {asset.assetName}
                                </CommandItem>
                              ))}
                            </CommandGroup>
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                  )}
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Activity Work Field */}
            <FormField
              control={form.control}
              name="activityWorkId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Activity Work (Optional)</FormLabel>
                  <Popover open={activityWorkOpen} onOpenChange={setActivityWorkOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button variant="outline" role="combobox" className={cn('h-auto min-h-[2.75rem] py-2 justify-between', !field.value && 'text-muted-foreground')}>
                          {field.value ? activityWorks.find((aw) => aw.id === field.value)?.vendorOrderNumber || activityWorks.find((aw) => aw.id === field.value)?.activitiesName || 'Select activity work...' : 'Select activity work...'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command>
                        <CommandInput placeholder="Search activity works..." onValueChange={setActivityWorkSearch} value={activityWorkSearch} />
                        <CommandList>
                          <CommandEmpty>{isLoadingActivityWorks ? 'Loading...' : 'No activity work found.'}</CommandEmpty>
                          <CommandGroup>
                            {activityWorks.map((aw) => (
                              <CommandItem key={aw.id} value={`${aw.id}`} onSelect={() => { field.onChange(aw.id); setActivityWorkOpen(false); }}>
                                <Check className={cn('mr-2 h-4 w-4', field.value === aw.id ? 'opacity-100' : 'opacity-0')} />
                                {aw.vendorOrderNumber || aw.activitiesName}
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

            {/* Expenditure Invoice Field */}
            <FormField
              control={form.control}
              name="expendituresInvoiceId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Expenditure Invoice (Optional)</FormLabel>
                  <Popover open={expenditureInvoiceOpen} onOpenChange={setExpenditureInvoiceOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button variant="outline" role="combobox" className={cn('h-11 justify-between', !field.value && 'text-muted-foreground')}>
                          {field.value ? expenditureInvoices.find((ei) => ei.id === field.value)?.invoiceNumber || 'Select invoice...' : 'Select invoice...'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command>
                        <CommandInput placeholder="Search invoices..." onValueChange={setExpenditureInvoiceSearch} value={expenditureInvoiceSearch} />
                        <CommandList>
                          <CommandEmpty>{isLoadingExpenditureInvoices ? 'Loading...' : 'No invoice found.'}</CommandEmpty>
                          <CommandGroup>
                            {expenditureInvoices.map((ei) => (
                              <CommandItem key={ei.id} value={`${ei.id}`} onSelect={() => { field.onChange(ei.id); setExpenditureInvoiceOpen(false); }}>
                                <Check className={cn('mr-2 h-4 w-4', field.value === ei.id ? 'opacity-100' : 'opacity-0')} />
                                {ei.invoiceNumber} - {ei.costItemFor}
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

            <SheetFooter className="mt-4">
              <Button type="button" variant="outline" onClick={closeDrawer} disabled={isLoading}>
                Cancel
              </Button>
              <Button type="submit" disabled={isLoading}>
                {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                {selectedExpenditure ? 'Update' : 'Create'}
              </Button>
            </SheetFooter>
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
}
