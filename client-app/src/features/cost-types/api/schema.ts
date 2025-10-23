import { z } from "zod";

export const costTypeSchema = z.object({
  typeName: z.string().min(1, "Type name is required").max(100, "Type name cannot exceed 100 characters"),
  typeDescription: z.string().max(5000, "Type description cannot exceed 5000 characters").optional().or(z.literal("")),
  costCategoryId: z.number().min(1, "Cost category is required"),
});

export type CostTypeFormData = z.infer<typeof costTypeSchema>;

export interface CostType {
  id: number;
  typeName: string;
  typeDescription?: string;
  costCategoryId: number;
  costCategoryName: string;
  createdAt: string;
  updatedAt: string;
}
