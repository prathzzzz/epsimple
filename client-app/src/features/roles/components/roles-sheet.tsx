'use client'

import { useEffect, useState } from 'react'
import { Button } from '@/components/ui/button'
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@/components/ui/tabs'
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
import { Badge } from '@/components/ui/badge'
import { Separator } from '@/components/ui/separator'
import { PermissionSelector } from './permission-selector'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { useCreateRole, useUpdateRole, useUpdateRolePermissions } from '@/hooks/use-roles'
import type { RoleDTO } from '@/lib/roles-api'
import { Shield, CheckCircle2, XCircle, AlertCircle } from 'lucide-react'

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

type RolesSheetProps = {
  currentRow?: RoleDTO | null
  open: boolean
  onOpenChange: (open: boolean) => void
}

export function RolesSheet({
  currentRow,
  open,
  onOpenChange,
}: RolesSheetProps) {
  const isEdit = !!currentRow
  const [activeTab, setActiveTab] = useState('details')
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
      setActiveTab('details')
    } else {
      form.reset({
        name: '',
        description: '',
        permissionIds: [],
      })
      setActiveTab('details')
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
    <Sheet
      open={open}
      onOpenChange={(state) => {
        if (!state) {
          form.reset()
        }
        onOpenChange(state)
      }}
    >
      <SheetContent className='flex flex-col sm:max-w-xl'>
        <SheetHeader className='text-start'>
          <div className='flex items-center gap-2'>
            <div className='flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10'>
              <Shield className='h-5 w-5 text-primary' />
            </div>
            <div>
              <SheetTitle>
                {isEdit ? currentRow?.name : 'Create New Role'}
              </SheetTitle>
              <SheetDescription>
                {isEdit ? 'Manage role details and permissions' : 'Set up a new role with permissions'}
              </SheetDescription>
            </div>
          </div>
        </SheetHeader>

        <div className='flex-1 space-y-4 overflow-y-auto px-4'>
          {isSystemRole && (
            <div className='rounded-lg border border-yellow-200 bg-yellow-50 p-3 dark:border-yellow-800 dark:bg-yellow-900/20'>
              <div className='flex gap-2'>
                <AlertCircle className='h-5 w-5 text-yellow-600 dark:text-yellow-500 flex-shrink-0' />
                <div>
                  <h4 className='text-sm font-medium text-yellow-800 dark:text-yellow-200'>
                    System Role
                  </h4>
                  <p className='mt-1 text-xs text-yellow-700 dark:text-yellow-300'>
                    This is a protected system role. Name and permissions cannot be modified.
                  </p>
                </div>
              </div>
            </div>
          )}

          <Tabs value={activeTab} onValueChange={setActiveTab}>
            <TabsList className='grid w-full grid-cols-2'>
              <TabsTrigger value='details'>Details</TabsTrigger>
              <TabsTrigger value='permissions'>
                Permissions
                {form.watch('permissionIds')?.length > 0 && (
                  <Badge variant='secondary' className='ml-2'>
                    {form.watch('permissionIds')?.length}
                  </Badge>
                )}
              </TabsTrigger>
            </TabsList>

            <Form {...form}>
              <form id='role-form' onSubmit={form.handleSubmit(onSubmit)}>
                <TabsContent value='details' className='space-y-4 mt-4'>
                  <FormField
                    control={form.control}
                    name='name'
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Role Name</FormLabel>
                        <FormControl>
                          <Input
                            placeholder='SITE_MANAGER'
                            autoComplete='off'
                            {...field}
                            disabled={isSubmitting || isSystemRole}
                          />
                        </FormControl>
                        <FormDescription>
                          Use uppercase letters and underscores only (e.g., SITE_MANAGER)
                        </FormDescription>
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
                            placeholder='Describe the role and its responsibilities...'
                            className='resize-none min-h-[100px]'
                            {...field}
                            disabled={isSubmitting || isSystemRole}
                          />
                        </FormControl>
                        <FormDescription>
                          Provide a clear description of the role's purpose
                        </FormDescription>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  {isEdit && currentRow && (
                    <>
                      <Separator />
                      <div className='space-y-3'>
                        <h4 className='text-sm font-medium'>Role Status</h4>
                        <div className='flex items-center justify-between rounded-lg border p-3'>
                          <div className='flex items-center gap-2'>
                            {currentRow.isActive ? (
                              <CheckCircle2 className='h-4 w-4 text-green-600' />
                            ) : (
                              <XCircle className='h-4 w-4 text-gray-400' />
                            )}
                            <span className='text-sm'>
                              {currentRow.isActive ? 'Active' : 'Inactive'}
                            </span>
                          </div>
                          <Badge variant={currentRow.isActive ? 'default' : 'secondary'}>
                            {currentRow.isActive ? 'In Use' : 'Disabled'}
                          </Badge>
                        </div>
                      </div>
                    </>
                  )}
                </TabsContent>

                <TabsContent value='permissions' className='mt-4'>
                  <FormField
                    control={form.control}
                    name='permissionIds'
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Assign Permissions</FormLabel>
                        <FormDescription className='mb-3'>
                          Select the permissions this role should have. Permissions are grouped by category.
                        </FormDescription>
                        <FormControl>
                          <PermissionSelector
                            value={field.value}
                            onChange={field.onChange}
                            disabled={isSubmitting || isSystemRole}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </TabsContent>
              </form>
            </Form>
          </Tabs>
        </div>

        <SheetFooter className='gap-2'>
          <Button
            type='button'
            variant='outline'
            onClick={() => onOpenChange(false)}
            disabled={isSubmitting}
          >
            Cancel
          </Button>
          <Button
            type='submit'
            form='role-form'
            disabled={isSubmitting || isSystemRole}
          >
            {isSubmitting
              ? isEdit
                ? 'Updating...'
                : 'Creating...'
              : isEdit
              ? 'Update Role'
              : 'Create Role'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  )
}
