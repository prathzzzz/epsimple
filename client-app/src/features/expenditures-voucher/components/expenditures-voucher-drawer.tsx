import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Check, ChevronsUpDown, Loader2 } from 'lucide-react';
import { format } from 'date-fns';
import { useExpendituresVoucherContext } from '../context/expenditures-voucher-provider';
import { expendituresVoucherFormSchema, type ExpendituresVoucherFormData } from '../api/schema';
import { expendituresVoucherApi } from '../api/expenditures-voucher-api';
import type { CostItem } from '@/features/cost-items/api/cost-items-api';
import type { Voucher } from '@/features/vouchers/api/schema';
import type { ManagedProject } from '@/features/managed-projects/api/schema';
import api from '@/lib/api';
import { cn } from '@/lib/utils';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
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
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { Calendar } from '@/components/ui/calendar';
import { useQuery } from '@tanstack/react-query';

export const ExpendituresVoucherDrawer = () => {
  const { isDrawerOpen, closeDrawer, editingExpenditure } = useExpendituresVoucherContext();
  const [costItemSearch, setCostItemSearch] = useState('');
  const [costItemOpen, setCostItemOpen] = useState(false);
  const [voucherSearch, setVoucherSearch] = useState('');
  const [voucherOpen, setVoucherOpen] = useState(false);
  const [projectSearch, setProjectSearch] = useState('');
  const [projectOpen, setProjectOpen] = useState(false);

  // Fetch cost items
  const { data: costItems = [], isLoading: costItemsLoading } = useQuery({
    queryKey: ['cost-items', 'search', costItemSearch],
    queryFn: async () => {
      const response = await api.get<{ data: CostItem[]; message: string }>('/api/cost-items/list');
      return response.data.data.filter((item: CostItem) => 
        item.costItemFor.toLowerCase().includes(costItemSearch.toLowerCase())
      );
    },
  });

  // Fetch vouchers
  const { data: vouchers = [], isLoading: vouchersLoading } = useQuery({
    queryKey: ['vouchers', 'search', voucherSearch],
    queryFn: async () => {
      const response = await api.get<{ data: Voucher[]; message: string }>('/api/vouchers/list');
      return response.data.data.filter((voucher: Voucher) => 
        voucher.voucherNumber.toLowerCase().includes(voucherSearch.toLowerCase())
      );
    },
  });

  // Fetch projects
  const { data: projects = [], isLoading: projectsLoading } = useQuery({
    queryKey: ['managed-projects', 'search', projectSearch],
    queryFn: async () => {
      const response = await api.get<{ data: ManagedProject[]; message: string }>('/api/managed-projects/list');
      return response.data.data.filter((proj: ManagedProject) => 
        proj.projectName.toLowerCase().includes(projectSearch.toLowerCase())
      );
    },
  });
  
  const createMutation = expendituresVoucherApi.useCreate();
  const updateMutation = expendituresVoucherApi.useUpdate();

  const form = useForm<ExpendituresVoucherFormData>({
    resolver: zodResolver(expendituresVoucherFormSchema),
    defaultValues: {
      costItemId: 0,
      voucherId: 0,
      managedProjectId: 0,
      incurredDate: '',
      description: '',
    },
  });

  useEffect(() => {
    if (editingExpenditure) {
      form.reset({
        costItemId: editingExpenditure.costItemId,
        voucherId: editingExpenditure.voucherId,
        managedProjectId: editingExpenditure.managedProjectId,
        incurredDate: editingExpenditure.incurredDate || '',
        description: editingExpenditure.description || '',
      });
    } else {
      form.reset({
        costItemId: 0,
        voucherId: 0,
        managedProjectId: 0,
        incurredDate: '',
        description: '',
      });
    }
  }, [editingExpenditure, form]);

  const onSubmit = async (data: ExpendituresVoucherFormData) => {
    try {
      if (editingExpenditure) {
        await updateMutation.mutateAsync({
          id: editingExpenditure.id,
          data,
        });
      } else {
        await createMutation.mutateAsync(data);
      }
      closeDrawer();
      form.reset();
    } catch (error) {
      console.error('Failed to save expenditure:', error);
    }
  };

  return (
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col sm:max-w-[500px]">
        <SheetHeader>
          <SheetTitle>
            {editingExpenditure ? 'Edit Expenditure' : 'Create Expenditure'}
          </SheetTitle>
          <SheetDescription>
            {editingExpenditure
              ? 'Update the expenditure details'
              : 'Add a new expenditure to the voucher'}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form 
            id="expenditure-form"
            onSubmit={form.handleSubmit(onSubmit)} 
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="costItemId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Cost Item *</FormLabel>
                  <Popover open={costItemOpen} onOpenChange={setCostItemOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={costItemOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? costItems.find((item: CostItem) => item.id === field.value)?.costItemFor || "Select cost item"
                            : "Select cost item"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search cost items..."
                          value={costItemSearch}
                          onValueChange={setCostItemSearch}
                        />
                        <CommandList>
                          {costItemsLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : costItems.length === 0 ? (
                            <CommandEmpty>No cost item found.</CommandEmpty>
                          ) : (
                            costItems.map((item: CostItem) => (
                              <CommandItem
                                key={item.id}
                                value={String(item.id)}
                                onSelect={() => {
                                  field.onChange(item.id);
                                  setCostItemOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    item.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {item.costItemFor}
                              </CommandItem>
                            ))
                          )}
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
              name="voucherId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Voucher *</FormLabel>
                  <Popover open={voucherOpen} onOpenChange={setVoucherOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={voucherOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? vouchers.find((voucher: Voucher) => voucher.id === field.value)?.voucherNumber || "Select voucher"
                            : "Select voucher"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search vouchers..."
                          value={voucherSearch}
                          onValueChange={setVoucherSearch}
                        />
                        <CommandList>
                          {vouchersLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : vouchers.length === 0 ? (
                            <CommandEmpty>No voucher found.</CommandEmpty>
                          ) : (
                            vouchers.map((voucher: Voucher) => (
                              <CommandItem
                                key={voucher.id}
                                value={String(voucher.id)}
                                onSelect={() => {
                                  field.onChange(voucher.id);
                                  setVoucherOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    voucher.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {voucher.voucherNumber}
                              </CommandItem>
                            ))
                          )}
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
              name="managedProjectId"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Project *</FormLabel>
                  <Popover open={projectOpen} onOpenChange={setProjectOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={projectOpen}
                          className={cn(
                            "justify-between font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? projects.find((proj: ManagedProject) => proj.id === field.value)?.projectName || "Select project"
                            : "Select project"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search projects..."
                          value={projectSearch}
                          onValueChange={setProjectSearch}
                        />
                        <CommandList>
                          {projectsLoading ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : projects.length === 0 ? (
                            <CommandEmpty>No project found.</CommandEmpty>
                          ) : (
                            projects.map((proj: ManagedProject) => (
                              <CommandItem
                                key={proj.id}
                                value={String(proj.id)}
                                onSelect={() => {
                                  field.onChange(proj.id);
                                  setProjectOpen(false);
                                }}
                              >
                                <Check
                                  className={cn(
                                    "mr-2 h-4 w-4",
                                    proj.id === field.value ? "opacity-100" : "opacity-0"
                                  )}
                                />
                                {proj.projectName}
                              </CommandItem>
                            ))
                          )}
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
              name="incurredDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Incurred Date *</FormLabel>
                  <Popover>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          className={cn(
                            "justify-start text-left font-normal",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value ? (
                            format(new Date(field.value), 'PPP')
                          ) : (
                            <span>Pick a date</span>
                          )}
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-auto p-0" align="start">
                      <Calendar
                        mode="single"
                        selected={field.value ? new Date(field.value) : undefined}
                        onSelect={(date) => {
                          field.onChange(date ? format(date, 'yyyy-MM-dd') : '');
                        }}
                        disabled={(date) =>
                          date > new Date() || date < new Date('1900-01-01')
                        }
                        initialFocus
                      />
                    </PopoverContent>
                  </Popover>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* Claim Amount field removed */}

            <FormField
              control={form.control}
              name="description"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter description (optional)"
                      {...field}
                      maxLength={1000}
                      rows={4}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <Button
            type="button"
            variant="outline"
            onClick={closeDrawer}
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="expenditure-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {(createMutation.isPending || updateMutation.isPending) && (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            )}
            {editingExpenditure ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
};
