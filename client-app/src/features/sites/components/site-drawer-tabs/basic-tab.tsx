import { UseFormReturn } from "react-hook-form";
import { useState } from "react";
import { Sparkles, Loader2 } from "lucide-react";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { TabsContent } from "@/components/ui/tabs";
import { toast } from "sonner";
import type { SiteFormData } from "../../api/schema";
import type { Location } from "@/features/locations/api/schema";
import type { ManagedProject } from "@/features/managed-projects/api/schema";
import type { SiteCategory } from "@/features/site-categories/api/schema";
import type { SiteType } from "@/features/site-types/api/schema";
import type { GenericStatusType } from "@/features/generic-status-types/api/schema";
import type { PersonDetails } from "@/features/person-details/api/schema";
import type { State } from "@/features/states/api/state-api";
import { siteCodeGeneratorApi } from "@/features/site-code-generators/api/site-code-generator-api";

interface BasicTabProps {
  form: UseFormReturn<SiteFormData>;
  locations: Location[];
  projects: ManagedProject[];
  categories: SiteCategory[];
  types: SiteType[];
  statuses: GenericStatusType[];
  personDetails: PersonDetails[];
  states: State[];
}

export function BasicTab({ form, locations, projects, categories, types, statuses, personDetails, states }: BasicTabProps) {
  const [isGenerating, setIsGenerating] = useState(false);
  const generateCodeMutation = siteCodeGeneratorApi.useGenerateCode();

  const handleGenerateCode = async () => {
    const projectId = form.getValues("projectId");
    
    // Get stateId from the selected location's stateName
    const locationId = form.getValues("locationId");
    const selectedLocation = locations.find((loc) => loc.id === locationId);
    const stateId = states.find((state) => state.stateName === selectedLocation?.stateName)?.id;

    if (!projectId) {
      toast.error("Please select a project first");
      return;
    }
    if (!stateId) {
      toast.error("Please select a location first");
      return;
    }

    setIsGenerating(true);
    try {
      const result = await generateCodeMutation.mutateAsync({
        projectId,
        stateId,
      });
      form.setValue("siteCode", result.siteCode);
      toast.success(`Generated code: ${result.siteCode}`);
    } catch (error) {
      // Error already shown by mutation
    } finally {
      setIsGenerating(false);
    }
  };

  return (
    <TabsContent value="basic" className="flex-1 space-y-6 overflow-y-auto px-4 mt-0 data-[state=active]:mt-6">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="siteCode"
          render={({ field }) => (
            <FormItem className="flex-1">
              <FormLabel>Site Code *</FormLabel>
              <div className="flex gap-2">
                <FormControl>
                  <Input
                    placeholder="Enter site code"
                    {...field}
                    className="uppercase font-mono"
                  />
                </FormControl>
                <Button
                  type="button"
                  variant="outline"
                  size="icon"
                  onClick={handleGenerateCode}
                  disabled={isGenerating}
                  title="Auto-generate site code"
                >
                  {isGenerating ? (
                    <Loader2 className="h-4 w-4 animate-spin" />
                  ) : (
                    <Sparkles className="h-4 w-4" />
                  )}
                </Button>
              </div>
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

      {/* Contact Information */}
      <div className="space-y-4">
        <h3 className="text-sm font-medium">Contact Information</h3>
        
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          <FormField
            control={form.control}
            name="channelManagerContactId"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormLabel>Channel Manager Contact</FormLabel>
                <Select
                  onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                  value={field.value ? String(field.value) : ""}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select contact" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {personDetails.map((person) => (
                      <SelectItem key={person.id} value={String(person.id)}>
                        {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim() || person.email}
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
            name="regionalManagerContactId"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormLabel>Regional Manager Contact</FormLabel>
                <Select
                  onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                  value={field.value ? String(field.value) : ""}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select contact" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {personDetails.map((person) => (
                      <SelectItem key={person.id} value={String(person.id)}>
                        {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim() || person.email}
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
            name="stateHeadContactId"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormLabel>State Head Contact</FormLabel>
                <Select
                  onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                  value={field.value ? String(field.value) : ""}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select contact" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {personDetails.map((person) => (
                      <SelectItem key={person.id} value={String(person.id)}>
                        {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim() || person.email}
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
            name="bankPersonContactId"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormLabel>Bank Person Contact</FormLabel>
                <Select
                  onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                  value={field.value ? String(field.value) : ""}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select contact" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {personDetails.map((person) => (
                      <SelectItem key={person.id} value={String(person.id)}>
                        {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim() || person.email}
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
            name="masterFranchiseeContactId"
            render={({ field }) => (
              <FormItem className="flex-1">
                <FormLabel>Master Franchisee Contact</FormLabel>
                <Select
                  onValueChange={(value) => field.onChange(value ? Number(value) : null)}
                  value={field.value ? String(field.value) : ""}
                >
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select contact" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    {personDetails.map((person) => (
                      <SelectItem key={person.id} value={String(person.id)}>
                        {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim() || person.email}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
      </div>
    </TabsContent>
  );
}
