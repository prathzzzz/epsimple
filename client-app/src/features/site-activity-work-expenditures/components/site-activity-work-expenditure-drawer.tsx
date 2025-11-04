import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useQuery } from '@tanstack/react-query';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';

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
import { cn } from '@/lib/utils';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { useSiteActivityWorkExpenditure } from '../context/site-activity-work-expenditure-provider';
import { siteActivityWorkExpenditureApi } from '../api/site-activity-work-expenditure-api';
import {
  siteActivityWorkExpenditureSchema,
  type SiteActivityWorkExpenditureFormData,
} from '../api/schema';
import { siteApi } from '@/features/sites/api/site-api';
import { activityWorkApi } from '@/features/activity-works/api/activity-work-api';
import { expendituresInvoiceApi } from '@/features/expenditures-invoice/api/expenditures-invoice-api';

export function SiteActivityWorkExpenditureDrawer() {
  const { isDrawerOpen, closeDrawer, selectedExpenditure, siteId } =
    useSiteActivityWorkExpenditure();

  const [siteSearch, setSiteSearch] = useState('');
  const [siteOpen, setSiteOpen] = useState(false);
  const [activityWorkSearch, setActivityWorkSearch] = useState('');
  const [activityWorkOpen, setActivityWorkOpen] = useState(false);
  const [expenditureInvoiceSearch, setExpenditureInvoiceSearch] = useState('');
  const [expenditureInvoiceOpen, setExpenditureInvoiceOpen] = useState(false);

  const createMutation = siteActivityWorkExpenditureApi.useCreate();
  const updateMutation = siteActivityWorkExpenditureApi.useUpdate();

  const { data: sites = [], isLoading: isLoadingSites } = siteApi.useSearch(siteSearch);
  
  // Fetch all sites list to get the current site data when siteId is provided
  const { data: allSites = [] } = useQuery({
    queryKey: ['sites', 'list'],
    queryFn: siteApi.getList,
    enabled: !!siteId,
  });
  
  // Find the current site when siteId is provided
  const currentSite = siteId ? allSites.find((s) => s.id === siteId) : null;

  const { data: activityWorksData, isLoading: isLoadingActivityWorks } =
    activityWorkApi.useSearch({
      searchTerm: activityWorkSearch,
      page: 0,
      size: 50,
    });
  const searchedActivityWorks = activityWorksData?.content || [];

  // Fetch all activity works list for initial display
  const { data: allActivityWorks = [] } = useQuery({
    queryKey: ['activity-works', 'list'],
    queryFn: activityWorkApi.getList,
    enabled: !activityWorkSearch, // Only fetch list when not searching
  });

  // Use searched results if searching, otherwise use the full list
  const activityWorks = activityWorkSearch ? searchedActivityWorks : allActivityWorks;

  const { data: expenditureInvoicesData, isLoading: isLoadingExpenditureInvoices } =
    useQuery({
      queryKey: ['expenditures-invoices', 'list'],
      queryFn: async () => {
        const response = await expendituresInvoiceApi.getAll(0, 100);
        return response.data.content;
      },
    });
  const expenditureInvoices = expenditureInvoicesData || [];

  const form = useForm<SiteActivityWorkExpenditureFormData>({
    resolver: zodResolver(siteActivityWorkExpenditureSchema),
    defaultValues: {
      siteId: siteId || 0,
      activityWorkId: 0,
      expendituresInvoiceId: 0,
    },
  });

  useEffect(() => {
    if (selectedExpenditure) {
      form.reset({
        siteId: selectedExpenditure.siteId,
        activityWorkId: selectedExpenditure.activityWorkId,
        expendituresInvoiceId: selectedExpenditure.expendituresInvoiceId,
      });
    } else {
      form.reset({
        siteId: siteId || 0,
        activityWorkId: 0,
        expendituresInvoiceId: 0,
      });
    }
  }, [selectedExpenditure, form, siteId]);

  const onSubmit = async (data: SiteActivityWorkExpenditureFormData) => {
    if (selectedExpenditure) {
      updateMutation.mutate(
        {
          id: selectedExpenditure.id,
          data,
        },
        {
          onSuccess: () => {
            closeDrawer();
            form.reset();
          },
        }
      );
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
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col sm:max-w-[600px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {selectedExpenditure ? 'Update' : 'Create'} Expenditure Link
          </SheetTitle>
          <SheetDescription>
            {selectedExpenditure
              ? 'Update the activity work expenditure linkage.'
              : 'Link an activity work with an expenditure invoice.'}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex flex-1 flex-col gap-6 overflow-y-auto px-4 py-4"
          >
            {/* Site Selection */}
            <FormField
              control={form.control}
              name="siteId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>
                    Site <span className="text-destructive">*</span>
                  </FormLabel>
                  {siteId ? (
                    <FormControl>
                      <Button
                        variant="outline"
                        disabled
                        className="h-11 justify-between opacity-60"
                      >
                        {currentSite?.siteCode || `Site #${siteId}`}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  ) : (
                    <Popover open={siteOpen} onOpenChange={setSiteOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              'h-11 justify-between',
                              !field.value && 'text-muted-foreground'
                            )}
                          >
                            {field.value
                              ? sites.find((site) => site.id === field.value)
                                  ?.siteCode || 'Select site'
                              : 'Select site'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-[400px] p-0">
                        <Command>
                          <CommandInput
                            placeholder="Search sites..."
                            value={siteSearch}
                            onValueChange={setSiteSearch}
                          />
                          <CommandList>
                            <CommandEmpty>
                              {isLoadingSites ? 'Loading...' : 'No site found.'}
                            </CommandEmpty>
                            <CommandGroup>
                              {sites.map((site) => (
                                <CommandItem
                                  key={site.id}
                                  value={site.siteCode}
                                  onSelect={() => {
                                    form.setValue('siteId', site.id);
                                    setSiteOpen(false);
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      'mr-2 h-4 w-4',
                                      site.id === field.value
                                        ? 'opacity-100'
                                        : 'opacity-0'
                                    )}
                                  />
                                  {site.siteCode}
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

            {/* Activity Work Selection */}
            <FormField
              control={form.control}
              name="activityWorkId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>
                    Activity Work <span className="text-destructive">*</span>
                  </FormLabel>
                  <Popover
                    open={activityWorkOpen}
                    onOpenChange={setActivityWorkOpen}
                  >
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          className={cn(
                            'h-auto min-h-[2.75rem] justify-between py-2',
                            !field.value && 'text-muted-foreground'
                          )}
                        >
                          {field.value ? (
                            (() => {
                              const selectedWork = activityWorks.find((aw) => aw.id === field.value);
                              return (
                                <div className="flex flex-col items-start gap-0.5 overflow-hidden">
                                  <span className="font-medium truncate w-full">
                                    {selectedWork?.vendorOrderNumber || selectedWork?.activitiesName || 'Select activity work'}
                                  </span>
                                  {selectedWork?.vendorOrderNumber && (
                                    <span className="text-xs text-muted-foreground truncate w-full">
                                      {selectedWork?.activitiesName}
                                    </span>
                                  )}
                                </div>
                              );
                            })()
                          ) : (
                            'Select activity work'
                          )}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command>
                        <CommandInput
                          placeholder="Search activity works..."
                          value={activityWorkSearch}
                          onValueChange={setActivityWorkSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingActivityWorks
                              ? 'Loading...'
                              : 'No activity work found.'}
                          </CommandEmpty>
                          <CommandGroup>
                            {activityWorks.map((aw) => (
                              <CommandItem
                                key={aw.id}
                                value={aw.vendorOrderNumber || aw.id.toString()}
                                onSelect={() => {
                                  form.setValue('activityWorkId', aw.id);
                                  setActivityWorkOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    'mr-2 h-4 w-4',
                                    aw.id === field.value
                                      ? 'opacity-100'
                                      : 'opacity-0'
                                  )}
                                />
                                <div className="flex flex-col">
                                  <span className="font-medium">
                                    {aw.vendorOrderNumber || `ID: ${aw.id}`}
                                  </span>
                                  <span className="text-xs text-muted-foreground">
                                    {aw.activitiesName}
                                  </span>
                                </div>
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

            {/* Expenditure Invoice Selection */}
            <FormField
              control={form.control}
              name="expendituresInvoiceId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>
                    Expenditure Invoice <span className="text-destructive">*</span>
                  </FormLabel>
                  <Popover
                    open={expenditureInvoiceOpen}
                    onOpenChange={setExpenditureInvoiceOpen}
                  >
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          className={cn(
                            'h-11 justify-between',
                            !field.value && 'text-muted-foreground'
                          )}
                        >
                          {field.value
                            ? expenditureInvoices.find((ei) => ei.id === field.value)
                                ?.invoiceNumber || 'Select expenditure invoice'
                            : 'Select expenditure invoice'}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[400px] p-0">
                      <Command>
                        <CommandInput
                          placeholder="Search expenditure invoices..."
                          value={expenditureInvoiceSearch}
                          onValueChange={setExpenditureInvoiceSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingExpenditureInvoices
                              ? 'Loading...'
                              : 'No expenditure invoice found.'}
                          </CommandEmpty>
                          <CommandGroup>
                            {expenditureInvoices.map((ei) => (
                              <CommandItem
                                key={ei.id}
                                value={ei.invoiceNumber || ei.id.toString()}
                                onSelect={() => {
                                  form.setValue('expendituresInvoiceId', ei.id);
                                  setExpenditureInvoiceOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    'mr-2 h-4 w-4',
                                    ei.id === field.value
                                      ? 'opacity-100'
                                      : 'opacity-0'
                                  )}
                                />
                                <div className="flex flex-col">
                                  <span className="font-medium">
                                    {ei.invoiceNumber || `ID: ${ei.id}`}
                                  </span>
                                  <span className="text-xs text-muted-foreground">
                                    {ei.costItemFor} - {ei.projectCode}
                                  </span>
                                </div>
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

            <SheetFooter className="mt-auto">
              <Button type="button" variant="outline" onClick={closeDrawer}>
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
