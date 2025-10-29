import { z } from "zod";

const ipv4Regex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;

export const siteSchema = z.object({
  // Required fields
  siteCode: z
    .string()
    .min(5, "Site code must be at least 5 characters")
    .max(50, "Site code cannot exceed 50 characters")
    .regex(/^[A-Z0-9]+$/, "Site code must be uppercase alphanumeric"),
  locationId: z.number().min(1, "Location is required"),

  // Optional foreign keys
  projectId: z.number().optional().nullable(),
  siteCategoryId: z.number().optional().nullable(),
  siteTypeId: z.number().optional().nullable(),
  siteStatusId: z.number().optional().nullable(),

  // Basic site info
  projectPhase: z.string().max(100).optional().or(z.literal("")),
  oldSiteCode: z.string().max(50).optional().or(z.literal("")),
  previousMspTermId: z.string().max(50).optional().or(z.literal("")),
  locationClass: z.string().max(50).optional().or(z.literal("")),

  // Dates
  techLiveDate: z.string().optional().nullable(),
  cashLiveDate: z.string().optional().nullable(),
  siteCloseDate: z.string().optional().nullable(),
  possessionDate: z.string().optional().nullable(),
  actualPossessionDate: z.string().optional().nullable(),
  otcActivationDate: z.string().optional().nullable(),

  // Infrastructure
  groutingStatus: z.string().max(50).optional().or(z.literal("")),
  itStabilizer: z.string().max(50).optional().or(z.literal("")),
  rampStatus: z.string().max(50).optional().or(z.literal("")),
  upsBatteryBackupCapacity: z.string().max(50).optional().or(z.literal("")),
  connectivityType: z.string().max(50).optional().or(z.literal("")),
  acUnits: z.string().max(50).optional().or(z.literal("")),

  // Physical specs
  mainDoorGlassWidth: z.number().min(0).optional().nullable(),
  fixedGlassWidth: z.number().min(0).optional().nullable(),
  signboardSize: z.string().max(50).optional().or(z.literal("")),
  brandingSize: z.string().max(50).optional().or(z.literal("")),

  // Contacts
  channelManagerContactId: z.number().optional().nullable(),
  regionalManagerContactId: z.number().optional().nullable(),
  stateHeadContactId: z.number().optional().nullable(),
  bankPersonContactId: z.number().optional().nullable(),
  masterFranchiseeContactId: z.number().optional().nullable(),

  // Network configuration
  gatewayIp: z.string().regex(ipv4Regex, "Invalid IPv4 address").optional().or(z.literal("")),
  atmIp: z.string().regex(ipv4Regex, "Invalid IPv4 address").optional().or(z.literal("")),
  subnetMask: z.string().regex(ipv4Regex, "Invalid IPv4 address").optional().or(z.literal("")),
  natIp: z.string().regex(ipv4Regex, "Invalid IPv4 address").optional().or(z.literal("")),
  switchIp: z.string().regex(ipv4Regex, "Invalid IPv4 address").optional().or(z.literal("")),
  tlsPort: z.string().max(20).optional().or(z.literal("")),
  tlsDomainName: z.string().max(255).optional().or(z.literal("")),

  // Technical details
  ejDocket: z.string().max(100).optional().or(z.literal("")),
  tssDocket: z.string().max(100).optional().or(z.literal("")),
  otcActivationStatus: z.string().max(50).optional().or(z.literal("")),
  craName: z.string().max(255).optional().or(z.literal("")),

  // Cassette configuration
  cassetteSwapStatus: z.string().max(50).optional().or(z.literal("")),
  cassetteType1: z.string().max(50).optional().or(z.literal("")),
  cassetteType2: z.string().max(50).optional().or(z.literal("")),
  cassetteType3: z.string().max(50).optional().or(z.literal("")),
  cassetteType4: z.string().max(50).optional().or(z.literal("")),
});

export type SiteFormData = z.infer<typeof siteSchema>;

export interface Site {
  id: number;

  // Project details
  projectId?: number;
  projectName?: string;
  bankName?: string;
  projectPhase?: string;

  // Site identification
  siteCode: string;
  oldSiteCode?: string;
  previousMspTermId?: string;

  // Site classification
  siteCategoryId?: number;
  siteCategoryName?: string;

  // Location details
  locationId: number;
  locationName: string;
  cityName: string;
  stateName: string;
  locationClass?: string;

  // Site type
  siteTypeId?: number;
  siteTypeName?: string;

  // Status
  siteStatusId?: number;
  siteStatusName?: string;

  // Dates
  techLiveDate?: string;
  cashLiveDate?: string;
  siteCloseDate?: string;
  possessionDate?: string;
  actualPossessionDate?: string;

  // Infrastructure
  groutingStatus?: string;
  itStabilizer?: string;
  rampStatus?: string;
  upsBatteryBackupCapacity?: string;
  connectivityType?: string;
  acUnits?: string;

  // Physical specs
  mainDoorGlassWidth?: number;
  fixedGlassWidth?: number;
  signboardSize?: string;
  brandingSize?: string;

  // Contacts
  channelManagerContactId?: number;
  channelManagerContactName?: string;
  regionalManagerContactId?: number;
  regionalManagerContactName?: string;
  stateHeadContactId?: number;
  stateHeadContactName?: string;
  bankPersonContactId?: number;
  bankPersonContactName?: string;
  masterFranchiseeContactId?: number;
  masterFranchiseeContactName?: string;

  // Network configuration
  gatewayIp?: string;
  atmIp?: string;
  subnetMask?: string;
  natIp?: string;
  tlsPort?: string;
  switchIp?: string;
  tlsDomainName?: string;

  // Technical details
  ejDocket?: string;
  tssDocket?: string;
  otcActivationStatus?: string;
  otcActivationDate?: string;
  craName?: string;

  // Cassette configuration
  cassetteSwapStatus?: string;
  cassetteType1?: string;
  cassetteType2?: string;
  cassetteType3?: string;
  cassetteType4?: string;

  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}
