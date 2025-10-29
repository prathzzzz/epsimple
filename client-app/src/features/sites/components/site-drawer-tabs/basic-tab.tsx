import { UseFormReturn } from "react-hook-form";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { TabsContent } from "@/components/ui/tabs";
import type { SiteFormData } from "../../api/schema";
import type { Location } from "@/features/locations/api/schema";
import type { ManagedProject } from "@/features/managed-projects/api/schema";
import type { SiteCategory } from "@/features/site-categories/api/schema";
import type { SiteType } from "@/features/site-types/api/schema";
import type { GenericStatusType } from "@/features/generic-status-types/api/schema";

interface BasicTabProps {
  form: UseFormReturn<SiteFormData>;
  locations: Location[];
  projects: ManagedProject[];
  categories: SiteCategory[];
  types: SiteType[];
  statuses: GenericStatusType[];
}

export function BasicTab({ form, locations, projects, categories, types, statuses }: BasicTabProps) {
  return (
    <TabsContent value="basic" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="siteCode"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Site Code *</FormLabel>
              <FormControl>
                <Input
                  placeholder="Enter site code"
                  {...field}
                  className="uppercase font-mono"
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="locationId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Location *</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(Number(value))}
                value={field.value ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select location" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {locations.map((location) => (
                    <SelectItem key={location.id} value={String(location.id)}>
                      {location.locationName} - {location.cityName}, {location.stateName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      {/* Optional Core Fields */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="projectId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Managed Project</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                value={field.value ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select project" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {projects.map((project) => (
                    <SelectItem key={project.id} value={String(project.id)}>
                      {project.projectName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="projectPhase"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Project Phase</FormLabel>
              <FormControl>
                <Input placeholder="Enter project phase" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="siteCategoryId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Site Category</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                value={field.value ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select category" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {categories.map((category) => (
                    <SelectItem key={category.id} value={String(category.id)}>
                      {category.categoryName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="siteTypeId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Site Type</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                value={field.value ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select type" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {types.map((type) => (
                    <SelectItem key={type.id} value={String(type.id)}>
                      {type.typeName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="siteStatusId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Site Status</FormLabel>
              <Select
                onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                value={field.value ? String(field.value) : ""}
              >
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select status" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {statuses.map((status) => (
                    <SelectItem key={status.id} value={String(status.id)}>
                      {status.statusName}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="locationClass"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Location Class</FormLabel>
              <FormControl>
                <Input placeholder="Enter location class" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="oldSiteCode"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Old Site Code</FormLabel>
              <FormControl>
                <Input placeholder="Enter old site code" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="previousMspTermId"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Previous MSP Term ID</FormLabel>
              <FormControl>
                <Input placeholder="Enter previous MSP term ID" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>
    </TabsContent>
  );
}
