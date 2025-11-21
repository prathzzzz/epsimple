'use client'

import { useEffect } from 'react'
import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog'
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Textarea } from '@/components/ui/textarea'
import { PermissionSelector } from './permission-selector'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useCreateRole, useUpdateRole, useUpdateRolePermissions } from '@/hooks/use-roles'
import type { RoleDTO } from '@/lib/roles-api'

const roleFormSchema = z.object({
  name: z
    .string()
    .min(3, 'Role name must be at least 3 characters')
    .max(50, 'Role name must not exceed 50 characters')
    .regex(/^[A-Z_]+$/, 'Role name must be uppercase with underscores only (e.g., SITE_MANAGER)'),
  description: z
    .string()
    .min(10, 'Description must be at least 10 characters')
    .max(200, 'Description must not exceed 200 characters'),
  permissionIds: z
    .array(z.number())
    .min(1, 'Select at least one permission'),
})

type RoleForm = z.infer<typeof roleFormSchema>

type RoleActionDialogProps = {
  currentRow?: RoleDTO
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function RolesActionDialog({
  currentRow,
  open,
  onOpenChange,
}: RoleActionDialogProps) {
  const isEdit = !!currentRow
  const createRole = useCreateRole()
  const updateRole = useUpdateRole()
  const updateRolePermissions = useUpdateRolePermissions()

  const form = useForm<RoleForm>({
    resolver: zodResolver(roleFormSchema),
    defaultValues: {
      name: '',
      description: '',
      permissionIds: [],
    },
  })

  // Update form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        name: currentRow.name || '',
        description: currentRow.description || '',
        permissionIds: currentRow.permissions?.map((p) => p.id) || [],
      })
    } else {
      form.reset({
        name: '',
        description: '',
        permissionIds: [],
      })
    }
  }, [currentRow, form])

  const onSubmit = async (values: RoleForm) => {
    try {
      if (isEdit && currentRow) {
        await updateRole.mutateAsync({
          id: currentRow.id,
          data: {
            name: values.name,
            description: values.description,
          },
        })
        // Update permissions separately
        await updateRolePermissions.mutateAsync({
          id: currentRow.id,
          data: {
            permissionIds: values.permissionIds,
          },
        })
      } else {
        await createRole.mutateAsync(values)
      }
      form.reset()
      onOpenChange(false)
    } catch {
      // Error is handled by mutation hooks with toast
    }
  }

  const isSystemRole = currentRow?.isSystemRole || false
  const isSubmitting = createRole.isPending || updateRole.isPending

  return (
    <Dialog
      open={open}
      onOpenChange={(state) => {
        form.reset()
        onOpenChange(state)
      }}
    >
      <DialogContent className='sm:max-w-lg'>
        <DialogHeader className='text-start'>
          <DialogTitle>{isEdit ? 'Edit Role' : 'Create Role'}</DialogTitle>
          <DialogDescription>
            {isEdit ? 'Update the role here. ' : 'Create a new role and assign permissions. '}
            Click save when you&apos;re done.
          </DialogDescription>
        </DialogHeader>
        <div className='h-[26.25rem] w-[calc(100%+0.75rem)] overflow-y-auto py-1 pe-3'>
          <Form {...form}>
            <form
              id='role-form'
              onSubmit={form.handleSubmit(onSubmit)}
              className='space-y-4 px-0.5'
            >
              <FormField
                control={form.control}
                name='name'
                render={({ field }) => (
                  <FormItem className='grid grid-cols-6 items-center space-y-0 gap-x-4 gap-y-1'>
                    <FormLabel className='col-span-2 text-end'>
                      Role Name
                    </FormLabel>
                    <FormControl>
                      <Input
                        placeholder='SITE_MANAGER'
                        className='col-span-4'
                        autoComplete='off'
                        {...field}
                        disabled={isSubmitting || isSystemRole}
                      />
                    </FormControl>
                    <FormMessage className='col-span-4 col-start-3' />
                    <FormDescription className='col-span-4 col-start-3 text-xs'>
                      Use uppercase and underscores only
                    </FormDescription>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name='description'
                render={({ field }) => (
                  <FormItem className='grid grid-cols-6 items-start space-y-0 gap-x-4 gap-y-1'>
                    <FormLabel className='col-span-2 text-end pt-2'>
                      Description
                    </FormLabel>
                    <FormControl>
                      <Textarea
                        placeholder='Describe the role and its responsibilities...'
                        className='col-span-4 resize-none'
                        rows={3}
                        {...field}
                        disabled={isSubmitting || isSystemRole}
                      />
                    </FormControl>
                    <FormMessage className='col-span-4 col-start-3' />
                    <FormDescription className='col-span-4 col-start-3 text-xs'>
                      Provide a clear description
                    </FormDescription>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name='permissionIds'
                render={({ field }) => (
                  <FormItem className='grid grid-cols-6 items-start space-y-0 gap-x-4 gap-y-1'>
                    <FormLabel className='col-span-2 text-end pt-2'>
                      Permissions
                    </FormLabel>
                    <FormControl>
                      <div className='col-span-4'>
                        <PermissionSelector
                          value={field.value}
                          onChange={field.onChange}
                          disabled={isSubmitting || isSystemRole}
                          enabled={open}
                        />
                      </div>
                    </FormControl>
                    <FormMessage className='col-span-4 col-start-3' />
                    <FormDescription className='col-span-4 col-start-3 text-xs'>
                      Select at least one permission
                    </FormDescription>
                  </FormItem>
                )}
              />
              
              {isSystemRole && (
                <div className='col-span-6 rounded-md bg-yellow-50 p-3 dark:bg-yellow-900/20'>
                  <div className='flex'>
                    <div className='flex-shrink-0'>
                      <svg
                        className='h-5 w-5 text-yellow-400'
                        viewBox='0 0 20 20'
                        fill='currentColor'
                      >
                        <path
                          fillRule='evenodd'
                          d='M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z'
                          clipRule='evenodd'
                        />
                      </svg>
                    </div>
                    <div className='ml-3'>
                      <h3 className='text-sm font-medium text-yellow-800 dark:text-yellow-200'>
                        System Role
                      </h3>
                      <div className='mt-1 text-xs text-yellow-700 dark:text-yellow-300'>
                        This is a system role and cannot be modified.
                      </div>
                    </div>
                  </div>
                </div>
              )}
            </form>
          </Form>
        </div>
        <DialogFooter>
          <Button 
            type='submit' 
            form='role-form'
            disabled={isSubmitting || isSystemRole}
          >
            {isSubmitting
              ? isEdit
                ? 'Updating...'
                : 'Creating...'
              : 'Save changes'}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
