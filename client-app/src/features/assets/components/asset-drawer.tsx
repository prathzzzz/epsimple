import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useQuery } from '@tanstack/react-query'
import { Sparkles, Loader2 } from 'lucide-react'
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
import { DatePicker } from '@/components/date-picker'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { useAssetContext } from '../context/asset-provider'
import { assetsApi } from '../api/assets-api'
import { assetSchema, type AssetFormData } from '../api/schema'
import { assetCategoryApi } from '@/features/asset-categories/api/asset-categories-api'
import { assetTypesApi } from '@/features/asset-types/api/asset-types-api'
import { useVendorsList } from '@/lib/vendors-api'
import { getAllBanksList } from '@/lib/banks-api'
import { genericStatusTypeApi } from '@/features/generic-status-types/api/generic-status-type-api'
import { assetTagCodeGeneratorApi } from '@/features/asset-tag-generators/api/asset-tag-generator-api'
import { toast } from 'sonner'

export function AssetDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingAsset, setEditingAsset } =
    useAssetContext()
  const createAsset = assetsApi.useCreate()
  const updateAsset = assetsApi.useUpdate()
  const generateTag = assetTagCodeGeneratorApi.useGenerateTag()

  const { data: assetCategories = [] } = useQuery({
    queryKey: ['asset-categories', 'list'],
    queryFn: () => assetCategoryApi.getList(),
  })

  const { data: assetTypes = [] } = useQuery({
    queryKey: ['asset-types', 'list'],
    queryFn: () => assetTypesApi.getList(),
  })

  const { data: vendors = [] } = useVendorsList()

  const { data: banksResponse } = useQuery({
    queryKey: ['banks', 'list'],
    queryFn: () => getAllBanksList(),
  })
  const banks = banksResponse?.data || []

  const { data: statusTypesResponse } = useQuery({
    queryKey: ['generic-status-types', 'list'],
    queryFn: () => genericStatusTypeApi.getList(),
  })
  const statusTypes = statusTypesResponse?.data || []

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
        onSuccess: () => {
          handleClose()
        },
      })
    }
  }

  const handleClose = () => {
    setIsDrawerOpen(false)
    setEditingAsset(null)
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
    } catch (_error) {
      // Error toast is already handled by the mutation
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
                      disabled={!canGenerateTag() || generateTag.isPending}
                      title={
                        !canGenerateTag()
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
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ''}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select category" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {assetCategories.map((category: any) => (
                          <SelectItem key={category.id} value={category.id.toString()}>
                            {category.categoryName}
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
                name="assetTypeId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Asset Type *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ''}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select type" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {assetTypes.map((type: any) => (
                          <SelectItem key={type.id} value={type.id.toString()}>
                            {type.typeName}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ''}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select vendor" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {vendors?.map((vendor) => (
                          <SelectItem key={vendor.id} value={vendor.id.toString()}>
                            {vendor.vendorName}
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
                name="lenderBankId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Bank *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ''}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select bank" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {banks.map((bank: any) => (
                          <SelectItem key={bank.id} value={bank.id.toString()}>
                            {bank.bankName}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ''}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select status" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {statusTypes.map((status: any) => (
                          <SelectItem key={status.id} value={status.id.toString()}>
                            {status.statusName}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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
                          field.onChange(e.target.value ? parseFloat(e.target.value) : undefined)
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
