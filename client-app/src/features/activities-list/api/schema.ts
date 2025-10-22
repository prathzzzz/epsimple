import { z } from "zod";

export const activitiesListSchema = z.object({
  id: z.number(),
  activityId: z.number(),
  activityName: z.string(),
  activityCategory: z.string().nullable(),
  activityDescription: z.string().nullable(),
  createdAt: z.string(),
  updatedAt: z.string(),
  createdBy: z.string(),
  updatedBy: z.string(),
});

export const activitiesListFormSchema = z.object({
  activityId: z.number({
    message: "Activity is required",
  }),
  activityName: z
    .string()
    .min(1, "Activity name is required")
    .max(100, "Activity name must not exceed 100 characters"),
  activityCategory: z
    .string()
    .max(100, "Activity category must not exceed 100 characters")
    .optional(),
  activityDescription: z
    .string()
    .max(5000, "Activity description must not exceed 5000 characters")
    .optional(),
});

export type ActivitiesList = z.infer<typeof activitiesListSchema>;
export type ActivitiesListFormData = z.infer<typeof activitiesListFormSchema>;
