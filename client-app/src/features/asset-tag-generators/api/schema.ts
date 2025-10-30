import { z } from "zod";

export const assetTagCodeGeneratorFormSchema = z.object({
  assetCategoryId: z.number().min(1, "Asset Category is required"),
  vendorId: z.number().min(1, "Vendor is required"),
  bankId: z.number().min(1, "Bank is required"),
  maxSeqDigit: z.number().min(1, "Max sequence digit must be at least 1").max(10, "Max sequence digit cannot exceed 10").optional().or(z.literal(5)),
  runningSeq: z.number().min(1, "Running sequence must be at least 1").optional().or(z.literal(1)),
});

export type AssetTagCodeGeneratorFormData = z.infer<typeof assetTagCodeGeneratorFormSchema>;

export interface AssetTagCodeGenerator {
  id: number;
  assetCategoryId: number;
  assetCategoryName: string;
  assetCategoryCode: string;
  vendorId: number;
  vendorName: string;
  vendorCode: string;
  bankId: number;
  bankName: string;
  bankCode: string;
  maxSeqDigit: number;
  runningSeq: number;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
}

export interface GeneratedAssetTag {
  assetTag: string;
  assetCategoryCode: string;
  vendorCode: string;
  bankCode: string;
  sequence: number;
  nextSequence: number;
}
