import { z } from 'zod'

const userStatusSchema = z.union([
  z.literal('active'),
  z.literal('inactive'),
  z.literal('invited'),
  z.literal('suspended'),
])
export type UserStatus = z.infer<typeof userStatusSchema>

const userRoleSchema = z.union([
  z.literal('superadmin'),
  z.literal('admin'),
  z.literal('cashier'),
  z.literal('manager'),
])

// Support both API structure (UserDTO) and mock data structure
const roleSchema = z.object({
  id: z.number(),
  name: z.string(),
  description: z.string().optional(),
  isActive: z.boolean().optional(),
  isSystemRole: z.boolean().optional(),
  permissions: z.array(z.object({
    id: z.number(),
    name: z.string(),
    description: z.string().optional(),
  })).optional(),
})

const userSchema = z.object({
  id: z.union([z.string(), z.number()]), // Support both mock (string) and API (number)
  firstName: z.string().optional(), // Mock data field
  lastName: z.string().optional(), // Mock data field
  name: z.string().optional(), // API data field
  username: z.string().optional(), // Mock data field
  email: z.string(),
  phoneNumber: z.string().optional(),
  status: userStatusSchema.optional(),
  isActive: z.boolean().optional(), // API data field
  role: userRoleSchema.optional(), // Mock data field (single role)
  roles: z.array(roleSchema).optional(), // API data field (multiple roles)
  allPermissions: z.array(z.string()).optional(), // API data field
  createdAt: z.coerce.date().optional(),
  updatedAt: z.coerce.date().optional(),
})
export type User = z.infer<typeof userSchema>

export const userListSchema = z.array(userSchema)
