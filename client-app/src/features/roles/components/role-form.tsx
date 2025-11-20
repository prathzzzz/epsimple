import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import * as z from 'zod'
import { Button } from '@/components/ui/button'
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

export type RoleFormValues = z.infer<typeof roleFormSchema>

interface RoleFormProps {
  /** Initial values for editing */
  initialData?: RoleDTO
  /** Submit handler */
  onSubmit: (data: RoleFormValues) => void
  /** Cancel handler */
  onCancel?: () => void
  /** Loading state */
  isSubmitting?: boolean
  /** Form mode */
  mode?: 'create' | 'edit'
}

/**
 * Role Form Component
 * Form for creating and editing roles with permission assignment
 */
export function RoleForm({
  initialData,
  onSubmit,
  onCancel,
  isSubmitting = false,
  mode = 'create',
}: RoleFormProps) {
  const form = useForm<RoleFormValues>({
    resolver: zodResolver(roleFormSchema),
    defaultValues: {
      name: initialData?.name || '',
      description: initialData?.description || '',
      permissionIds: initialData?.permissions?.map((p) => p.id) || [],
    },
  })

  const isSystemRole = initialData?.isSystemRole || false

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
        {/* Role Name */}
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Role Name *</FormLabel>
              <FormControl>
                <Input
                  placeholder="SITE_MANAGER"
                  {...field}
                  disabled={isSubmitting || isSystemRole}
                />
              </FormControl>
              <FormDescription>
                Use uppercase letters and underscores only. Example: SITE_MANAGER,
                FINANCE_ADMIN
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* Description */}
        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Description *</FormLabel>
              <FormControl>
                <Textarea
                  placeholder="Describe the role and its responsibilities..."
                  className="resize-none"
                  rows={3}
                  {...field}
                  disabled={isSubmitting || isSystemRole}
                />
              </FormControl>
              <FormDescription>
                Provide a clear description of this role's purpose and responsibilities
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* Permissions */}
        <FormField
          control={form.control}
          name="permissionIds"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Permissions *</FormLabel>
              <FormControl>
                <PermissionSelector
                  value={field.value}
                  onChange={field.onChange}
                  disabled={isSubmitting || isSystemRole}
                />
              </FormControl>
              <FormDescription>
                Select the permissions this role should have. Users with this role will
                be able to perform all selected actions.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
        />

        {/* System Role Warning */}
        {isSystemRole && (
          <div className="rounded-md bg-yellow-50 p-4 dark:bg-yellow-900/20">
            <div className="flex">
              <div className="flex-shrink-0">
                <svg
                  className="h-5 w-5 text-yellow-400"
                  viewBox="0 0 20 20"
                  fill="currentColor"
                >
                  <path
                    fillRule="evenodd"
                    d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z"
                    clipRule="evenodd"
                  />
                </svg>
              </div>
              <div className="ml-3">
                <h3 className="text-sm font-medium text-yellow-800 dark:text-yellow-200">
                  System Role
                </h3>
                <div className="mt-2 text-sm text-yellow-700 dark:text-yellow-300">
                  This is a system role and cannot be modified. System roles are
                  essential for application functionality.
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Form Actions */}
        <div className="flex items-center justify-end gap-3">
          {onCancel && (
            <Button
              type="button"
              variant="outline"
              onClick={onCancel}
              disabled={isSubmitting}
            >
              Cancel
            </Button>
          )}
          <Button type="submit" disabled={isSubmitting || isSystemRole}>
            {isSubmitting
              ? mode === 'create'
                ? 'Creating...'
                : 'Updating...'
              : mode === 'create'
                ? 'Create Role'
                : 'Update Role'}
          </Button>
        </div>
      </form>
    </Form>
  )
}
