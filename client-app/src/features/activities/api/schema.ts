import { z } from "zod";

export const activitySchema = z.object({
  id: z.number(),
  activityName: z.string(),
  activityDescription: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const activityFormSchema = z.object({
  activityName: z
    .string()
    .min(1, "Activity name is required")
    .max(100, "Activity name must not exceed 100 characters"),
  activityDescription: z
    .string()
    .max(5000, "Activity description must not exceed 5000 characters")
    .optional(),
});

export type Activity = z.infer<typeof activitySchema>;
export type ActivityFormData = z.infer<typeof activityFormSchema>;
