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
import { type State, stateFormSchema, type StateFormData } from '../data/schema'
import { statesApi } from '@/features/states/api/states-api'
import { toast } from 'sonner'
import { Loader2 } from 'lucide-react'

type StatesMutateDrawerProps = {
  open: boolean
  onOpenChange: (open: boolean) => void
  currentRow?: State
}

export function StatesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: StatesMutateDrawerProps) {
  const isUpdate = !!currentRow
  const queryClient = useQueryClient()

  const form = useForm<StateFormData>({
    resolver: zodResolver(stateFormSchema),
    defaultValues: currentRow
      ? {
          stateName: currentRow.stateName,
          stateCode: currentRow.stateCode,
          stateCodeAlt: currentRow.stateCodeAlt || '',
        }
      : {
          stateName: '',
          stateCode: '',
          stateCodeAlt: '',
        },
  })

  const createMutation = useMutation({
    mutationFn: (data: StateFormData) => statesApi.create(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['states'] })
      toast.success('State created successfully')
      onOpenChange(false)
      form.reset()
    },
    onError: (error: unknown) => {
      const message = error instanceof Error ? error.message : 'Failed to create state';
      toast.error(message)
    },
  })

  const updateMutation = useMutation({
    mutationFn: (data: StateFormData) => statesApi.update(currentRow!.id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['states'] })
      toast.success('State updated successfully')
      onOpenChange(false)
      form.reset()
    },
    onError: (error: unknown) => {
      const message = error instanceof Error ? error.message : 'Failed to update state';
      toast.error(message)
    },
  })

  const onSubmit = (data: StateFormData) => {
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
          <SheetTitle>{isUpdate ? 'Update' : 'Create'} State</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? 'Update the state by providing necessary info.'
              : 'Add a new state by providing necessary info.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id='states-form'
            onSubmit={form.handleSubmit(onSubmit)}
            className='flex-1 space-y-6 overflow-y-auto px-4'
          >
            <FormField
              control={form.control}
              name='stateName'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    State Name <span className='text-destructive'>*</span>
                  </FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter state name' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='stateCode'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    State Code <span className='text-destructive'>*</span>
                  </FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter state code' />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name='stateCodeAlt'
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Alternate State Code</FormLabel>
                  <FormControl>
                    <Input {...field} placeholder='Enter alternate state code' />
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
          <Button type='submit' form='states-form' disabled={isSubmitting}>
            {isSubmitting && <Loader2 className='mr-2 h-4 w-4 animate-spin' />}
            {isUpdate ? 'Update' : 'Create'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
