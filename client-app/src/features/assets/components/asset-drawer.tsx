import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Check, ChevronsUpDown, Sparkles, Loader2 } from 'lucide-react'
import { format } from 'date-fns'
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { Textarea } from '@/components/ui/textarea'
import { Checkbox } from '@/components/ui/checkbox'
import { DatePicker } from '@/components/date-picker'
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@/components/ui/command'
import { cn } from '@/lib/utils'
import { useAssetContext } from '../context/asset-provider'
import { assetsApi } from '../api/assets-api'
import { assetSchema, type AssetFormData } from '../api/schema'
import { assetCategoryApi } from '@/features/asset-categories/api/asset-categories-api'
import { assetTypesApi } from '@/features/asset-types/api/asset-types-api'
import { useSearchVendors } from '@/lib/vendors-api'
import { useSearchBanks } from '@/lib/banks-api'
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api'
import { assetTagCodeGeneratorApi } from '@/features/asset-tag-generators/api/asset-tag-generator-api'
import { toast } from 'sonner'

export function AssetDrawer() {
  const { 
    isDrawerOpen, 
    setIsDrawerOpen, 
    editingAsset, 
    setEditingAsset,
    setIsPlacementDialogOpen,
    setAssetForPlacement 
  } = useAssetContext()

  const [categorySearch, setCategorySearch] = useState('')
  const [categoryOpen, setCategoryOpen] = useState(false)
  const [typeSearch, setTypeSearch] = useState('')
  const [typeOpen, setTypeOpen] = useState(false)
  const [vendorSearch, setVendorSearch] = useState('')
  const [vendorOpen, setVendorOpen] = useState(false)
  const [bankSearch, setBankSearch] = useState('')
  const [bankOpen, setBankOpen] = useState(false)
  const [statusSearch, setStatusSearch] = useState('')
  const [statusOpen, setStatusOpen] = useState(false)
  const [placeAfterCreation, setPlaceAfterCreation] = useState(false)

  const createAsset = assetsApi.useCreate()
  const updateAsset = assetsApi.useUpdate()
  const generateTag = assetTagCodeGeneratorApi.useGenerateTag()

  const { data: assetCategories = [], isLoading: isLoadingCategories } = 
    assetCategoryApi.useSearch(categorySearch)
  const { data: assetTypes = [], isLoading: isLoadingTypes } = 
    assetTypesApi.useSearch(typeSearch)
  const { data: vendors = [], isLoading: isLoadingVendors } = 
    useSearchVendors(vendorSearch)
  const { data: banks = [], isLoading: isLoadingBanks } = 
    useSearchBanks(bankSearch)
  const { data: statusTypes = [], isLoading: isLoadingStatuses } = 
    genericStatusTypeApi.useSearch(statusSearch)

  // Fetch initial items for display
  const { data: allAssetCategories = [] } = assetCategoryApi.useSearch("")
  const { data: allAssetTypes = [] } = assetTypesApi.useSearch("")
  const { data: allVendors = [] } = useSearchVendors("")
  const { data: allBanks = [] } = useSearchBanks("")
  const { data: allStatusTypes = [] } = genericStatusTypeApi.useSearch("")

  // Combine search results with selected items
  const displayAssetCategories = (() => {
    if (!editingAsset?.assetCategoryId) return assetCategories;
    const selected = allAssetCategories.find(c => c.id === editingAsset.assetCategoryId);
    if (!selected) return assetCategories;
    if (assetCategories.some(c => c.id === selected.id)) return assetCategories;
    return [selected, ...assetCategories];
  })();

  const displayAssetTypes = (() => {
    if (!editingAsset?.assetTypeId) return assetTypes;
    const selected = allAssetTypes.find(t => t.id === editingAsset.assetTypeId);
    if (!selected) return assetTypes;
    if (assetTypes.some(t => t.id === selected.id)) return assetTypes;
    return [selected, ...assetTypes];
  })();

  const displayVendors = (() => {
    if (!editingAsset?.vendorId) return vendors;
    const selected = allVendors.find(v => v.id === editingAsset.vendorId);
    if (!selected) return vendors;
    if (vendors.some(v => v.id === selected.id)) return vendors;
    return [selected, ...vendors];
  })();

  const displayBanks = (() => {
    if (!editingAsset?.lenderBankId) return banks;
    const selected = allBanks.find(b => b.id === editingAsset.lenderBankId);
    if (!selected) return banks;
    if (banks.some(b => b.id === selected.id)) return banks;
    return [selected, ...banks];
  })();

  const displayStatusTypes = (() => {
    if (!editingAsset?.statusTypeId) return statusTypes;
    const selected = allStatusTypes.find(s => s.id === editingAsset.statusTypeId);
    if (!selected) return statusTypes;
    if (statusTypes.some(s => s.id === selected.id)) return statusTypes;
    return [selected, ...statusTypes];
  })();

  const form = useForm<AssetFormData>({
    resolver: zodResolver(assetSchema),
    defaultValues: {
      assetTagId: '',
      assetName: '',
      serialNumber: '',
      assetCategoryId: 0,
      assetTypeId: 0,
      vendorId: 0,
      lenderBankId: 0,
      statusTypeId: 0,
      purchaseDate: '',
      purchasePrice: undefined,
      warrantyExpiryDate: '',
      remarks: '',
    },
  })

  useEffect(() => {
    if (editingAsset) {
      form.reset({
        assetTagId: editingAsset.assetTagId,
        assetName: editingAsset.assetName,
        serialNumber: editingAsset.serialNumber || '',
        assetCategoryId: editingAsset.assetCategoryId,
        assetTypeId: editingAsset.assetTypeId,
        vendorId: editingAsset.vendorId,
        lenderBankId: editingAsset.lenderBankId,
        statusTypeId: editingAsset.statusTypeId,
        purchaseDate: editingAsset.purchaseDate || '',
        purchasePrice: editingAsset.purchasePrice,
        warrantyExpiryDate: editingAsset.warrantyExpiryDate || '',
        remarks: editingAsset.remarks || '',
      })
    } else {
      form.reset()
    }
  }, [editingAsset, form])

  const onSubmit = (data: AssetFormData) => {
    const payload = {
      ...data,
      assetCategoryId: Number(data.assetCategoryId),
      assetTypeId: Number(data.assetTypeId),
      vendorId: Number(data.vendorId),
      lenderBankId: Number(data.lenderBankId),
      statusTypeId: Number(data.statusTypeId),
    }

    if (editingAsset) {
      updateAsset.mutate(
        { id: editingAsset.id, data: payload },
        {
          onSuccess: () => {
            handleClose()
          },
        }
      )
    } else {
      createAsset.mutate(payload, {
        onSuccess: (createdAsset) => {
          handleClose()
          
          // Open placement dialog if checkbox was checked
          if (placeAfterCreation && createdAsset) {
            setAssetForPlacement(createdAsset)
            setIsPlacementDialogOpen(true)
          }
        },
      })
    }
  }

  const handleClose = () => {
    setIsDrawerOpen(false)
    setEditingAsset(null)
    setPlaceAfterCreation(false)
    form.reset()
  }

  const handleGenerateTag = async () => {
    const categoryId = form.watch('assetCategoryId')
    const vendorId = form.watch('vendorId')
    const bankId = form.watch('lenderBankId')

    if (!categoryId || !vendorId || !bankId) {
      toast.error('Please select Asset Category, Vendor, and Bank first')
      return
    }

    try {
      const result = await generateTag.mutateAsync({
        assetCategoryId: categoryId,
        vendorId: vendorId,
        bankId: bankId,
      })
      
      form.setValue('assetTagId', result.assetTag)
      toast.success(`Generated tag: ${result.assetTag}`)
    } catch (error) {
      console.error('Failed to generate asset tag:', error);
    }
  }

  const canGenerateTag = () => {
    const categoryId = form.watch('assetCategoryId')
    const vendorId = form.watch('vendorId')
    const bankId = form.watch('lenderBankId')
    return categoryId > 0 && vendorId > 0 && bankId > 0
  }

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-[650px]">
        <SheetHeader className="text-start">
          <SheetTitle>{editingAsset ? 'Update' : 'Create'} Asset</SheetTitle>
          <SheetDescription>
            {editingAsset
              ? 'Update the asset information.'
              : 'Add a new asset to the system.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="asset-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="assetTagId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Asset Tag ID *</FormLabel>
                  <div className="flex gap-2">
                    <FormControl>
                      <Input placeholder="Enter asset tag ID" {...field} />
                    </FormControl>
                    <Button
                      type="button"
                      variant="outline"
                      size="icon"
                      onClick={handleGenerateTag}
                      disabled={canGenerateTag() === false || generateTag.isPending}
                      title={
                        canGenerateTag() === false
                          ? 'Select Category, Vendor, and Bank first'
                          : 'Generate Asset Tag'
                      }
                    >
                      <Sparkles className="h-4 w-4" />
                    </Button>
                  </div>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="assetName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Asset Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter asset name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="serialNumber"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Serial Number</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter serial number" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="assetCategoryId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Asset Category *</FormLabel>
                    <Popover open={categoryOpen} onOpenChange={setCategoryOpen}>
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
                              ? displayAssetCategories.find((c) => c.id === field.value)?.categoryName
                              : 'Select category'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search categories..."
                            value={categorySearch}
                            onValueChange={setCategorySearch}
                          />
                          <CommandList>
                            {(() => {
                              if (isLoadingCategories) {
                                return (
                                  <div className="flex items-center justify-center py-6">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                  </div>
                                );
                              }
                              if (displayAssetCategories.length === 0) {
                                return <CommandEmpty>No categories found.</CommandEmpty>;
                              }
                              return (
                                <CommandGroup>
                                  {displayAssetCategories.map((category) => (
                                    <CommandItem
                                      key={category.id}
                                      value={String(category.id)}
                                      onSelect={() => {
                                        field.onChange(category.id)
                                        setCategoryOpen(false)
                                        setCategorySearch('')
                                      }}
                                    >
                                      <Check
                                        className={cn(
                                          'mr-2 h-4 w-4',
                                          category.id === field.value ? 'opacity-100' : 'opacity-0'
                                        )}
                                      />
                                      {category.categoryName}
                                    </CommandItem>
                                  ))}
                                </CommandGroup>
                              );
                            })()}
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
                name="assetTypeId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Asset Type *</FormLabel>
                    <Popover open={typeOpen} onOpenChange={setTypeOpen}>
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
                              ? displayAssetTypes.find((t) => t.id === field.value)?.typeName
                              : 'Select type'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search types..."
                            value={typeSearch}
                            onValueChange={setTypeSearch}
                          />
                          <CommandList>
                            {(() => {
                              if (isLoadingTypes) {
                                return (
                                  <div className="flex items-center justify-center py-6">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                  </div>
                                );
                              }
                              if (displayAssetTypes.length === 0) {
                                return <CommandEmpty>No types found.</CommandEmpty>;
                              }
                              return (
                                <CommandGroup>
                                  {displayAssetTypes.map((type) => (
                                    <CommandItem
                                      key={type.id}
                                      value={String(type.id)}
                                      onSelect={() => {
                                        field.onChange(type.id)
                                        setTypeOpen(false)
                                        setTypeSearch('')
                                      }}
                                    >
                                      <Check
                                        className={cn(
                                          'mr-2 h-4 w-4',
                                          type.id === field.value ? 'opacity-100' : 'opacity-0'
                                        )}
                                      />
                                      {type.typeName}
                                    </CommandItem>
                                  ))}
                                </CommandGroup>
                              );
                            })()}
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="vendorId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Vendor *</FormLabel>
                    <Popover open={vendorOpen} onOpenChange={setVendorOpen}>
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
                              ? vendors.find((v) => v.id === field.value)?.vendorName
                              : 'Select vendor'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search vendors..."
                            value={vendorSearch}
                            onValueChange={setVendorSearch}
                          />
                          <CommandList>
                            {(() => {
                              if (isLoadingVendors) {
                                return (
                                  <div className="flex items-center justify-center py-6">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                  </div>
                                );
                              }
                              if (vendors.length === 0) {
                                return <CommandEmpty>No vendors found.</CommandEmpty>;
                              }
                              return (
                                <CommandGroup>
                                  {vendors.map((vendor) => (
                                    <CommandItem
                                      key={vendor.id}
                                      value={String(vendor.id)}
                                      onSelect={() => {
                                        field.onChange(vendor.id)
                                        setVendorOpen(false)
                                        setVendorSearch('')
                                      }}
                                    >
                                      <Check
                                        className={cn(
                                          'mr-2 h-4 w-4',
                                          vendor.id === field.value ? 'opacity-100' : 'opacity-0'
                                        )}
                                      />
                                      {vendor.vendorName}
                                    </CommandItem>
                                  ))}
                                </CommandGroup>
                              );
                            })()}
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
                name="lenderBankId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Bank *</FormLabel>
                    <Popover open={bankOpen} onOpenChange={setBankOpen}>
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
                              ? banks.find((b) => b.id === field.value)?.bankName
                              : 'Select bank'}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search banks..."
                            value={bankSearch}
                            onValueChange={setBankSearch}
                          />
                          <CommandList>
                            {(() => {
                              if (isLoadingBanks) {
                                return (
                                  <div className="flex items-center justify-center py-6">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                  </div>
                                );
                              }
                              if (banks.length === 0) {
                                return <CommandEmpty>No banks found.</CommandEmpty>;
                              }
                              return (
                                <CommandGroup>
                                  {banks.map((bank) => (
                                    <CommandItem
                                      key={bank.id}
                                      value={String(bank.id)}
                                      onSelect={() => {
                                        field.onChange(bank.id)
                                        setBankOpen(false)
                                        setBankSearch('')
                                      }}
                                    >
                                      <Check
                                        className={cn(
                                          'mr-2 h-4 w-4',
                                          bank.id === field.value ? 'opacity-100' : 'opacity-0'
                                        )}
                                      />
                                      {bank.bankName}
                                    </CommandItem>
                                  ))}
                                </CommandGroup>
                              );
                            })()}
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="statusTypeId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Status *</FormLabel>
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
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search statuses..."
                            value={statusSearch}
                            onValueChange={setStatusSearch}
                          />
                          <CommandList>
                            {(() => {
                              if (isLoadingStatuses) {
                                return (
                                  <div className="flex items-center justify-center py-6">
                                    <Loader2 className="h-4 w-4 animate-spin" />
                                  </div>
                                );
                              }
                              if (statusTypes.length === 0) {
                                return <CommandEmpty>No statuses found.</CommandEmpty>;
                              }
                              return (
                                <CommandGroup>
                                  {statusTypes.map((status) => (
                                    <CommandItem
                                      key={status.id}
                                      value={String(status.id)}
                                      onSelect={() => {
                                        field.onChange(status.id)
                                        setStatusOpen(false)
                                        setStatusSearch('')
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
                              );
                            })()}
                          </CommandList>
                        </Command>
                      </PopoverContent>
                    </Popover>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <FormField
                control={form.control}
                name="purchaseDate"
                render={({ field }) => (
                  <FormItem className="flex flex-col">
                    <FormLabel>Purchase Date</FormLabel>
                    <DatePicker
                      selected={field.value ? new Date(field.value) : undefined}
                      onSelect={(date: Date | undefined) =>
                        field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                      }
                      placeholder="Select purchase date"
                    />
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="purchasePrice"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Purchase Price</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        step="0.01"
                        placeholder="0.00"
                        {...field}
                        onChange={(e) =>
                          field.onChange(e.target.value ? Number.parseFloat(e.target.value) : undefined)
                        }
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="warrantyExpiryDate"
              render={({ field }) => (
                <FormItem className="flex flex-col">
                  <FormLabel>Warranty Expiry Date</FormLabel>
                  <DatePicker
                    selected={field.value ? new Date(field.value) : undefined}
                    onSelect={(date: Date | undefined) =>
                      field.onChange(date ? format(date, "yyyy-MM-dd") : "")
                    }
                    placeholder="Select warranty expiry date"
                  />
                  <FormMessage />
                </FormItem>
              )}
            />

            {!editingAsset && (
              <div className="flex items-center space-x-2 p-4 border rounded-lg bg-muted/50">
                <Checkbox
                  id="placeAfterCreation"
                  checked={placeAfterCreation}
                  onCheckedChange={(checked) => setPlaceAfterCreation(checked === true)}
                />
                <label
                  htmlFor="placeAfterCreation"
                  className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70 cursor-pointer"
                >
                  Place asset at a location after creation
                </label>
              </div>
            )}

            <FormField
              control={form.control}
              name="remarks"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Remarks</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter any additional remarks"
                      className="resize-none"
                      rows={3}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 px-4">
          <Button
            type="button"
            variant="outline"
            onClick={handleClose}
            disabled={createAsset.isPending || updateAsset.isPending}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="asset-form"
            disabled={createAsset.isPending || updateAsset.isPending}
          >
            {createAsset.isPending || updateAsset.isPending && (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            )}
            {editingAsset ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
