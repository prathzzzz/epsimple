'use client'

import { z } from 'zod'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useCreateUser, useUpdateUser } from '../hooks/use-users-api'
import { useRoles } from '@/hooks/use-roles'
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
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form'
import { Input } from '@/components/ui/input'
import { Checkbox } from '@/components/ui/checkbox'
import { Badge } from '@/components/ui/badge'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Shield } from 'lucide-react'
import { type User } from '../data/schema'
import { type RoleDTO } from '@/lib/roles-api'

const formSchema = z.object({
  name: z.string().min(2, 'Name must be at least 2 characters.'),
  email: z.string().email('Invalid email address.'),
  roleIds: z.array(z.number()).optional(),
  isEdit: z.boolean(),
})

type UserForm = z.infer<typeof formSchema>

type UserActionDialogProps = {
  currentRow?: User
  open: boolean
  onOpenChange: (open: boolean) => void
}

function RoleSelection({
  value = [],
  onChange,
  roles,
  isLoading,
}: {
  value?: number[]
  onChange: (value: number[]) => void
  roles: RoleDTO[]
  isLoading: boolean
}) {
  if (isLoading) {
    return (
      <div className='text-sm text-muted-foreground text-center py-4'>
        Loading roles...
      </div>
    )
  }

  if (roles.length === 0) {
    return (
      <div className='text-sm text-muted-foreground text-center py-4'>
        No roles available
      </div>
    )
  }

  return (
    <div className='space-y-2'>
      {roles.map((role) => {
        const isChecked = value.includes(role.id)
        return (
          <div
            key={role.id}
            className='flex flex-row items-start space-x-3 space-y-0 rounded-lg border p-3 hover:bg-accent cursor-pointer'
            onClick={(e) => {
              e.preventDefault()
              const newValue = isChecked
                ? value.filter((id) => id !== role.id)
                : [...value, role.id]
              onChange(newValue)
            }}
          >
            <Checkbox
              checked={isChecked}
              onCheckedChange={(checked) => {
                const newValue = checked
                  ? [...value, role.id]
                  : value.filter((id) => id !== role.id)
                onChange(newValue)
              }}
              onClick={(e) => e.stopPropagation()}
            />
            <div className='flex-1 space-y-1 leading-none'>
              <div className='flex items-center gap-2'>
                <Shield className='h-4 w-4 text-muted-foreground' />
                <label className='text-sm font-medium cursor-pointer'>
                  {role.name}
                </label>
                {role.isSystemRole && (
                  <Badge variant='outline' className='text-xs'>
                    System
                  </Badge>
                )}
              </div>
              {role.description && (
                <p className='text-xs text-muted-foreground'>
                  {role.description}
                </p>
              )}
            </div>
          </div>
        )
      })}
    </div>
  )
}

export function UsersActionDialog({
  currentRow,
  open,
  onOpenChange,
}: UserActionDialogProps) {
  const isEdit = !!currentRow
  const createUser = useCreateUser()
  const updateUser = useUpdateUser()
  const { data: availableRoles = [], isLoading: rolesLoading } = useRoles()
  
  // Get name from API or mock data
  const userName = currentRow?.name || 
                  (currentRow?.firstName && currentRow?.lastName 
                    ? `${currentRow.firstName} ${currentRow.lastName}` 
                    : '')
  
  const form = useForm<UserForm>({
    resolver: zodResolver(formSchema),
    defaultValues: isEdit
      ? {
          name: userName,
          email: currentRow.email,
          roleIds: [],
          isEdit,
        }
      : {
          name: '',
          email: '',
          roleIds: [],
          isEdit,
        },
  })

  const onSubmit = async (values: UserForm) => {
    try {
      if (isEdit && currentRow) {
        const userId = typeof currentRow.id === 'string' ? parseInt(currentRow.id) : currentRow.id
        await updateUser.mutateAsync({
          id: userId,
          request: {
            name: values.name,
            email: values.email,
          }
        })
      } else {
        await createUser.mutateAsync({
          name: values.name,
          email: values.email,
          roleIds: values.roleIds,
        })
      }
      form.reset()
      onOpenChange(false)
    } catch {
      // Errors handled by mutations
    }
  }

  const isSaving = createUser.isPending || updateUser.isPending

  return (
    <Dialog
      open={open}
      onOpenChange={(state) => {
        if (!state) {
          form.reset()
        }
        onOpenChange(state)
      }}
    >
      <DialogContent className='sm:max-w-lg'>
        <DialogHeader className='text-start'>
          <DialogTitle>{isEdit ? 'Edit User' : 'Add New User'}</DialogTitle>
          <DialogDescription>
            {isEdit 
              ? 'Update the user here. ' 
              : 'Create new user here. A temporary password will be generated and sent via email. '}
            Click save when you&apos;re done.
          </DialogDescription>
        </DialogHeader>
        <div className='max-h-[26.25rem] w-[calc(100%+0.75rem)] overflow-y-auto py-1 pe-3'>
          <Form {...form}>
            <form
              id='user-form'
              onSubmit={form.handleSubmit(onSubmit)}
              className='space-y-4 px-0.5'
            >
              <FormField
                control={form.control}
                name='name'
                render={({ field }) => (
                  <FormItem className='grid grid-cols-6 items-center space-y-0 gap-x-4 gap-y-1'>
                    <FormLabel className='col-span-2 text-end'>
                      Name
                    </FormLabel>
                    <FormControl>
                      <Input
                        placeholder='John Doe'
                        className='col-span-4'
                        autoComplete='name'
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className='col-span-4 col-start-3' />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name='email'
                render={({ field }) => (
                  <FormItem className='grid grid-cols-6 items-center space-y-0 gap-x-4 gap-y-1'>
                    <FormLabel className='col-span-2 text-end'>Email</FormLabel>
                    <FormControl>
                      <Input
                        placeholder='john.doe@gmail.com'
                        className='col-span-4'
                        autoComplete='email'
                        {...field}
                      />
                    </FormControl>
                    <FormMessage className='col-span-4 col-start-3' />
                  </FormItem>
                )}
              />
              {!isEdit && (
                <FormField
                  control={form.control}
                  name='roleIds'
                  render={({ field }) => (
                    <FormItem className='space-y-0'>
                      <FormLabel className='text-base'>Assign Roles</FormLabel>
                      <ScrollArea className='h-[200px] rounded-md border p-3'>
                        <RoleSelection
                          value={field.value}
                          onChange={field.onChange}
                          roles={availableRoles}
                          isLoading={rolesLoading}
                        />
                      </ScrollArea>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              )}
            </form>
          </Form>
        </div>
        <DialogFooter>
          <Button 
            type='submit' 
            form='user-form'
            disabled={isSaving}
          >
            {isSaving ? 'Saving...' : 'Save changes'}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
