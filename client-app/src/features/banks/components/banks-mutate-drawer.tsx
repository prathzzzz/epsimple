import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { Button } from '@/components/ui/button'
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import { type Bank, bankFormSchema, type BankFormData } from '../data/schema'
import { createBank, updateBank } from '@/features/banks/api/banks-api'
import { toast } from 'sonner'
import { Loader2, Upload, X } from 'lucide-react'
import { useState } from 'react'

type BanksMutateDrawerProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  currentRow?: Bank
}

export function BanksMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: BanksMutateDrawerProps) {
  const isUpdate = !!currentRow
  const queryClient = useQueryClient()
  const [logoFile, setLogoFile] = useState<File | null>(null)
  const [logoPreview, setLogoPreview] = useState<string | null>(
    currentRow?.bankLogo || null
  )

  const form = useForm<BankFormData>({
    resolver: zodResolver(bankFormSchema),
    defaultValues: currentRow
      ? {
          bankName: currentRow.bankName,
          rbiBankCode: currentRow.rbiBankCode || '',
          epsBankCode: currentRow.epsBankCode || '',
          bankCodeAlt: currentRow.bankCodeAlt || '',
          bankLogo: currentRow.bankLogo || '',
        }
      : {
          bankName: '',
          rbiBankCode: '',
          epsBankCode: '',
          bankCodeAlt: '',
          bankLogo: '',
        },
  })

  const createMutation = useMutation({
    mutationFn: (data: BankFormData) => createBank(data, logoFile || undefined),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['banks'] })
      toast.success('Bank created successfully')
      onOpenChange(false)
      form.reset()
      setLogoFile(null)
      setLogoPreview(null)
    },
    onError: (error: any) => {
      const message = error?.response?.data?.message || 'Failed to create bank'
      toast.error(message)
    },
  })

  const updateMutation = useMutation({
    mutationFn: (data: BankFormData) => updateBank(currentRow!.id, data, logoFile || undefined),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['banks'] })
      toast.success('Bank updated successfully')
      onOpenChange(false)
      form.reset()
      setLogoFile(null)
      setLogoPreview(null)
    },
    onError: (error: any) => {
      const message = error?.response?.data?.message || 'Failed to update bank'
      toast.error(message)
    },
  })

  const onSubmit = (data: BankFormData) => {
    if (isUpdate) {
      updateMutation.mutate(data)
    } else {
      createMutation.mutate(data)
    }
  }

  const isSubmitting = createMutation.isPending || updateMutation.isPending

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      setLogoFile(file)
      const reader = new FileReader()
      reader.onloadend = () => {
        setLogoPreview(reader.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleRemoveLogo = () => {
    setLogoFile(null)
    setLogoPreview(null)
  }

  return (
    <Sheet
      open={open}
      onOpenChange={(v) => {
        onOpenChange(v)
        if (!v) {
          form.reset()
          setLogoFile(null)
          setLogoPreview(currentRow?.bankLogo || null)
        }
      }}
    >
      <SheetContent className='flex flex-col'>
        <SheetHeader className='text-start'>
          <SheetTitle>{isUpdate ? 'Update' : 'Create'} Bank</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? 'Update the bank by providing necessary info.'
              : 'Add a new bank by providing necessary info.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id='banks-form'
            onSubmit={form.handleSubmit(onSubmit)}
            className='flex-1 space-y-6 overflow-y-auto px-4'
          >
            <FormField
              control={form.control}
              name='bankName'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    Bank Name <span className='text-destructive'>*</span>
                  </FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter bank name' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='rbiBankCode'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RBI Bank Code</FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter RBI bank code' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='epsBankCode'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>EPS Bank Code</FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter EPS bank code' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='bankCodeAlt'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Alternate Bank Code</FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter alternate bank code' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormItem>
              <FormLabel>Bank Logo</FormLabel>
              <FormControl>
                <div className='space-y-4'>
                  <div className='flex items-center gap-4'>
                    <Input
                      id='logo-upload'
                      type='file'
                      accept='image/*'
                      onChange={handleFileChange}
                      className='hidden'
                    />
                    <Button
                      type='button'
                      variant='outline'
                      onClick={() => document.getElementById('logo-upload')?.click()}
                      className='w-full'
                    >
                      <Upload className='mr-2 size-4' />
                      {logoFile ? 'Change Logo' : 'Upload Logo'}
                    </Button>
                  </div>
                  {logoPreview && (
                    <div className='relative w-32 h-32 border rounded-md overflow-hidden'>
                      <img
                        src={logoPreview}
                        alt='Bank logo preview'
                        className='w-full h-full object-contain'
                      />
                      <Button
                        type='button'
                        variant='destructive'
                        size='icon'
                        className='absolute top-1 right-1 h-6 w-6'
                        onClick={handleRemoveLogo}
                      >
                        <X className='h-4 w-4' />
                      </Button>
                    </div>
                  )}
                </div>
              </FormControl>
              <FormMessage />
            </FormItem>
          </form>
        </Form>
        <SheetFooter className='gap-2'>
          <SheetClose asChild>
            <Button variant='outline' disabled={isSubmitting}>
              Close
            </Button>
          </SheetClose>
          <Button form='banks-form' type='submit' disabled={isSubmitting}>
            {isSubmitting && <Loader2 className='mr-2 size-4 animate-spin' />}
            Save changes
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
