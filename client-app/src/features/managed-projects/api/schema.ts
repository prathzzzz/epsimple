import { z } from "zod";

export const managedProjectSchema = z.object({
  bankId: z.number().min(1, "Bank is required"),
  projectType: z.string().max(50, "Project type cannot exceed 50 characters").optional().or(z.literal("")),
  projectName: z.string().min(1, "Project name is required").max(255, "Project name cannot exceed 255 characters"),
  projectCode: z
    .string()
    .min(1, "Project code is required")
    .max(50, "Project code cannot exceed 50 characters")
    .regex(/^[A-Za-z0-9_-]+$/, "Project code can only contain letters, numbers, hyphens and underscores"),
  projectDescription: z.string().max(5000, "Project description cannot exceed 5000 characters").optional().or(z.literal("")),
});

export type ManagedProjectFormData = z.infer<typeof managedProjectSchema>;

export interface ManagedProject {
  id: number;
  bankId: number;
  bankName: string;
  projectType?: string;
  projectName: string;
  projectCode?: string;
  projectDescription?: string;
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}
