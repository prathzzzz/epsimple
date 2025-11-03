import { useEffect } from 'react'
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
import { Textarea } from '@/components/ui/textarea'
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import { type PersonType, personTypeFormSchema, type PersonTypeFormValues } from '../data/schema'
import { personTypesApi } from '@/features/person-types/api/person-types-api'
import { toast } from 'sonner'
import { Loader2 } from 'lucide-react'

type PersonTypesMutateDrawerProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  currentRow?: PersonType
}

export function PersonTypesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: PersonTypesMutateDrawerProps) {
  const isUpdate = !!currentRow
  const queryClient = useQueryClient()

  const form = useForm<PersonTypeFormValues>({
    resolver: zodResolver(personTypeFormSchema),
    defaultValues: {
      typeName: '',
      description: '',
    },
  })

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        typeName: currentRow.typeName,
        description: currentRow.description || '',
      })
    } else {
      form.reset({
        typeName: '',
        description: '',
      })
    }
  }, [currentRow, form])

  const createMutation = useMutation({
    mutationFn: (data: PersonTypeFormValues) => personTypesApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['person-types'] })
      toast.success('Person type created successfully')
      onOpenChange(false)
      form.reset()
    },
    onError: (error: any) => {
      const message = error?.message || 'Failed to create person type'
      toast.error(message)
    },
  })

  const updateMutation = useMutation({
    mutationFn: (data: PersonTypeFormValues) =>
      personTypesApi.update(currentRow!.id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['person-types'] })
      toast.success('Person type updated successfully')
      onOpenChange(false)
      form.reset()
    },
    onError: (error: any) => {
      const message = error?.message || 'Failed to update person type'
      toast.error(message)
    },
  })

  const onSubmit = (data: PersonTypeFormValues) => {
    if (isUpdate) {
      updateMutation.mutate(data)
    } else {
      createMutation.mutate(data)
    }
  }

  const isSubmitting = createMutation.isPending || updateMutation.isPending

  return (
    <Sheet
      open={open}
      onOpenChange={(v) => {
        onOpenChange(v)
        if (!v) {
          form.reset()
        }
      }}
    >
      <SheetContent className='flex flex-col'>
        <SheetHeader className='text-start'>
          <SheetTitle>{isUpdate ? 'Update' : 'Create'} Person Type</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? 'Update the person type by providing necessary info.'
              : 'Add a new person type by providing necessary info.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id='person-types-form'
            onSubmit={form.handleSubmit(onSubmit)}
            className='flex-1 space-y-6 overflow-y-auto px-4'
          >
            <FormField
              control={form.control}
              name='typeName'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    Type Name <span className='text-destructive'>*</span>
                  </FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter type name' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='description'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      {...field}
                      placeholder='Enter description'
                      className='min-h-[120px]'
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>
        <SheetFooter className='mt-4 gap-2 px-4 sm:space-x-0'>
          <SheetClose asChild>
            <Button type='button' variant='outline' disabled={isSubmitting}>
              Cancel
            </Button>
          </SheetClose>
          <Button type='submit' form='person-types-form' disabled={isSubmitting}>
            {isSubmitting && <Loader2 className='mr-2 h-4 w-4 animate-spin' />}
            {isUpdate ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
