import { z } from "zod";

export const personDetailsFormSchema = z.object({
  personTypeId: z.number().min(1, "Person type is required"),
  firstName: z
    .string()
    .max(100, "First name cannot exceed 100 characters")
    .optional()
    .or(z.literal("")),
  middleName: z
    .string()
    .max(100, "Middle name cannot exceed 100 characters")
    .optional()
    .or(z.literal("")),
  lastName: z
    .string()
    .max(100, "Last name cannot exceed 100 characters")
    .optional()
    .or(z.literal("")),
  contactNumber: z
    .string()
    .regex(/^[0-9]{10}$/, "Phone number must be exactly 10 digits")
    .optional()
    .or(z.literal("")),
  email: z
    .string()
    .min(1, "Email is required")
    .email("Invalid email address format")
    .max(255, "Email cannot exceed 255 characters"),
  permanentAddress: z
    .string()
    .max(5000, "Permanent address cannot exceed 5000 characters")
    .optional()
    .or(z.literal("")),
  correspondenceAddress: z
    .string()
    .max(5000, "Correspondence address cannot exceed 5000 characters")
    .optional()
    .or(z.literal("")),
});

export type PersonDetailsFormData = z.infer<typeof personDetailsFormSchema>;

export interface PersonDetails {
  id: number;
  personTypeId: number;
  personTypeName: string;
  firstName?: string;
  middleName?: string;
  lastName?: string;
  fullName?: string;
  contactNumber?: string;
  email: string;
  permanentAddress?: string;
  correspondenceAddress?: string;
  createdAt: string;
  updatedAt: string;
}
