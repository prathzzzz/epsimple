import { z } from "zod";

export const siteCodeGeneratorFormSchema = z.object({
  projectId: z.number().min(1, "Project is required"),
  stateId: z.number().min(1, "State is required"),
  maxSeqDigit: z.number().min(1, "Max sequence digit must be at least 1").optional().or(z.literal(5)),
  runningSeq: z.number().min(1, "Running sequence must be at least 1").optional().or(z.literal(1)),
});

export type SiteCodeGeneratorFormData = z.infer<typeof siteCodeGeneratorFormSchema>;

export interface SiteCodeGenerator {
  id: number;
  projectId: number;
  projectName: string;
  projectCode: string;
  stateId: number;
  stateName: string;
  stateCode: string;
  maxSeqDigit: number;
  runningSeq: number;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface GeneratedSiteCode {
  siteCode: string;
  projectId: number;
  projectName: string;
  projectCode: string;
  stateId: number;
  stateName: string;
  stateCode: string;
  currentSequence: number;
  nextSequence: number;
}
