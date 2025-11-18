import type { UseFormReturn } from "react-hook-form";
import { useState } from "react";
import { Sparkles, Loader2, Check, ChevronsUpDown } from "lucide-react";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandEmpty, CommandGroup, CommandInput, CommandItem, CommandList } from "@/components/ui/command";
import { TabsContent } from "@/components/ui/tabs";
import { toast } from "sonner";
import { cn } from "@/lib/utils";
import type { SiteFormData } from "../../api/schema";
import type { State } from "@/features/states/api/states-api";
import { siteCodeGeneratorApi } from "@/features/site-code-generators/api/site-code-generator-api";
import { locationApi } from "@/features/locations/api/location-api";
import { managedProjectApi } from "@/features/managed-projects/api/managed-project-api";
import { siteCategoryApi } from "@/features/site-categories/api/site-category-api";
import { siteTypeApi } from "@/features/site-types/api/site-type-api";
import { genericStatusTypeApi } from "@/features/generic-status-types/api/generic-status-type-api";
import { personDetailsApi } from "@/features/person-details/api/person-details-api";

interface BasicTabProps {
  readonly form: UseFormReturn<SiteFormData>;
  readonly states: State[];
}

export function BasicTab({ form, states }: Readonly<BasicTabProps>) {
  const [isGenerating, setIsGenerating] = useState(false);
  const generateCodeMutation = siteCodeGeneratorApi.useGenerateCode();
  
  const [locationSearch, setLocationSearch] = useState("");
  const [locationOpen, setLocationOpen] = useState(false);
  const [projectSearch, setProjectSearch] = useState("");
  const [projectOpen, setProjectOpen] = useState(false);
  const [categorySearch, setCategorySearch] = useState("");
  const [categoryOpen, setCategoryOpen] = useState(false);
  const [typeSearch, setTypeSearch] = useState("");
  const [typeOpen, setTypeOpen] = useState(false);
  const [statusSearch, setStatusSearch] = useState("");
  const [statusOpen, setStatusOpen] = useState(false);
  const [personSearch, setPersonSearch] = useState("");
  const [channelManagerOpen, setChannelManagerOpen] = useState(false);
  const [regionalManagerOpen, setRegionalManagerOpen] = useState(false);
  const [stateHeadOpen, setStateHeadOpen] = useState(false);
  const [bankPersonOpen, setBankPersonOpen] = useState(false);
  const [masterFranchiseeOpen, setMasterFranchiseeOpen] = useState(false);
  
  const { data: locations = [], isLoading: isLoadingLocations } = locationApi.useSearch({ 
    searchTerm: locationSearch, 
    page: 0, 
    size: 50 
  });
  const { data: projects = [], isLoading: isLoadingProjects } = managedProjectApi.useSearch(projectSearch);
  const { data: allProjects = [] } = managedProjectApi.useSearch('');
  const { data: categories = [], isLoading: isLoadingCategories } = siteCategoryApi.useSearch(categorySearch);
  const { data: allCategories = [] } = siteCategoryApi.useSearch('');
  const { data: types = [], isLoading: isLoadingTypes } = siteTypeApi.useSearch(typeSearch);
  const { data: allTypes = [] } = siteTypeApi.useSearch('');
  const { data: statuses = [], isLoading: isLoadingStatuses } = genericStatusTypeApi.useSearch(statusSearch);
  const { data: personDetails = [], isLoading: isLoadingPersons } = personDetailsApi.useSearch(personSearch);

  // Display logic for dropdowns
  const displayProjects = (() => {
    const projectId = form.watch('projectId');
    if (!projectId) return projects;
    const selectedProject = allProjects.find((p) => p.id === projectId);
    if (!selectedProject || projects.some((p) => p.id === selectedProject.id)) {
      return projects;
    }
    return [selectedProject, ...projects];
  })();

  const displayCategories = (() => {
    const categoryId = form.watch('siteCategoryId');
    if (!categoryId) return categories;
    const selectedCategory = allCategories.find((c) => c.id === categoryId);
    if (!selectedCategory || categories.some((c) => c.id === selectedCategory.id)) {
      return categories;
    }
    return [selectedCategory, ...categories];
  })();

  const displayTypes = (() => {
    const typeId = form.watch('siteTypeId');
    if (!typeId) return types;
    const selectedType = allTypes.find((t) => t.id === typeId);
    if (!selectedType || types.some((t) => t.id === selectedType.id)) {
      return types;
    }
    return [selectedType, ...types];
  })();

  const handleGenerateCode = async () => {
    const projectId = form.getValues("projectId");
    const locationId = form.getValues("locationId");
    
    if (!projectId) {
      toast.error("Please select a project first");
      return;
    }
    if (!locationId) {
      toast.error("Please select a location first");
      return;
    }

    // Fetch the full location to get stateName
    const selectedLocation = locations.find((loc) => loc.id === locationId);
    if (!selectedLocation) {
      toast.error("Selected location not found");
      return;
    }

    const stateId = states.find((state) => state.stateName === selectedLocation?.stateName)?.id;
    if (!stateId) {
      toast.error("Could not determine state from location");
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
    } catch {
      // Error toast is already shown by the mutation's onError handler
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
          render={({ field }) => {
            const selectedLocation = field.value ? locations.find((loc) => loc.id === field.value) : null;
            const displayText = selectedLocation
              ? `${selectedLocation.locationName} - ${selectedLocation.cityName}, ${selectedLocation.stateName}`
              : "Select location";

            return (
              <FormItem className="flex-1">
                <FormLabel>Location *</FormLabel>
                <Popover open={locationOpen} onOpenChange={setLocationOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {displayText}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search locations..."
                        value={locationSearch}
                        onValueChange={setLocationSearch}
                      />
                      <CommandList>
                        {isLoadingLocations && (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        )}
                        {!isLoadingLocations && locations.length === 0 && (
                          <CommandEmpty>No location found.</CommandEmpty>
                        )}
                        {!isLoadingLocations && locations.length > 0 && (
                          <CommandGroup>
                            {locations.map((location) => (
                              <CommandItem
                                key={location.id}
                                value={String(location.id)}
                                onSelect={() => {
                                  field.onChange(location.id);
                                  setLocationOpen(false);
                                  setLocationSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === location.id ? "opacity-100" : "opacity-0")}
                                />
                                {location.locationName} - {location.cityName}, {location.stateName}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
                <FormMessage />
              </FormItem>
            );
          }}
        />
      </div>

      {/* Optional Core Fields */}
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <FormField
          control={form.control}
          name="projectId"
          render={({ field }) => {
            const selectedProject = field.value ? displayProjects.find((p) => p.id === field.value) : null;
            const displayText = selectedProject?.projectName || "Select project";

            return (
              <FormItem className="flex-1">
                <FormLabel>Managed Project</FormLabel>
                <Popover open={projectOpen} onOpenChange={setProjectOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {displayText}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search projects..."
                        value={projectSearch}
                        onValueChange={setProjectSearch}
                      />
                      <CommandList>
                        {isLoadingProjects && (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        )}
                        {!isLoadingProjects && displayProjects.length === 0 && (
                          <CommandEmpty>No project found.</CommandEmpty>
                        )}
                        {!isLoadingProjects && displayProjects.length > 0 && (
                          <CommandGroup>
                            {displayProjects.map((project) => (
                              <CommandItem
                                key={project.id}
                                value={String(project.id)}
                                onSelect={() => {
                                  field.onChange(project.id);
                                  setProjectOpen(false);
                                  setProjectSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === project.id ? "opacity-100" : "opacity-0")}
                                />
                                {project.projectName}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
                <FormMessage />
              </FormItem>
            );
          }}
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
              <Popover open={categoryOpen} onOpenChange={setCategoryOpen}>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant="outline"
                      role="combobox"
                      className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                    >
                      {field.value
                        ? displayCategories.find((c) => c.id === field.value)?.categoryName || "Select category"
                        : "Select category"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-full p-0" align="start">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search categories..."
                      value={categorySearch}
                      onValueChange={setCategorySearch}
                    />
                    <CommandList>
                      {isLoadingCategories ? (
                        <div className="flex items-center justify-center py-6">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : displayCategories.length === 0 ? (
                        <CommandEmpty>No category found.</CommandEmpty>
                      ) : (
                        <CommandGroup>
                          {displayCategories.map((category) => (
                            <CommandItem
                              key={category.id}
                              value={String(category.id)}
                              onSelect={() => {
                                field.onChange(category.id);
                                setCategoryOpen(false);
                                setCategorySearch("");
                              }}
                            >
                              <Check
                                className={cn("mr-2 h-4 w-4", field.value === category.id ? "opacity-100" : "opacity-0")}
                              />
                              {category.categoryName}
                            </CommandItem>
                          ))}
                        </CommandGroup>
                      )}
                    </CommandList>
                  </Command>
                </PopoverContent>
              </Popover>
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
              <Popover open={typeOpen} onOpenChange={setTypeOpen}>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant="outline"
                      role="combobox"
                      className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                    >
                      {field.value
                        ? displayTypes.find((t) => t.id === field.value)?.typeName || "Select type"
                        : "Select type"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-full p-0" align="start">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search types..."
                      value={typeSearch}
                      onValueChange={setTypeSearch}
                    />
                    <CommandList>
                      {isLoadingTypes ? (
                        <div className="flex items-center justify-center py-6">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : displayTypes.length === 0 ? (
                        <CommandEmpty>No type found.</CommandEmpty>
                      ) : (
                        <CommandGroup>
                          {displayTypes.map((type) => (
                            <CommandItem
                              key={type.id}
                              value={String(type.id)}
                              onSelect={() => {
                                field.onChange(type.id);
                                setTypeOpen(false);
                                setTypeSearch("");
                              }}
                            >
                              <Check
                                className={cn("mr-2 h-4 w-4", field.value === type.id ? "opacity-100" : "opacity-0")}
                              />
                              {type.typeName}
                            </CommandItem>
                          ))}
                        </CommandGroup>
                      )}
                    </CommandList>
                  </Command>
                </PopoverContent>
              </Popover>
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
              <Popover open={statusOpen} onOpenChange={setStatusOpen}>
                <PopoverTrigger asChild>
                  <FormControl>
                    <Button
                      variant="outline"
                      role="combobox"
                      className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                    >
                      {field.value
                        ? statuses.find((s) => s.id === field.value)?.statusName || "Select status"
                        : "Select status"}
                      <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                    </Button>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent className="w-full p-0" align="start">
                  <Command shouldFilter={false}>
                    <CommandInput
                      placeholder="Search statuses..."
                      value={statusSearch}
                      onValueChange={setStatusSearch}
                    />
                    <CommandList>
                      {isLoadingStatuses ? (
                        <div className="flex items-center justify-center py-6">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </div>
                      ) : statuses.length === 0 ? (
                        <CommandEmpty>No status found.</CommandEmpty>
                      ) : (
                        <CommandGroup>
                          {statuses.map((status) => (
                            <CommandItem
                              key={status.id}
                              value={String(status.id)}
                              onSelect={() => {
                                field.onChange(status.id);
                                setStatusOpen(false);
                                setStatusSearch("");
                              }}
                            >
                              <Check
                                className={cn("mr-2 h-4 w-4", field.value === status.id ? "opacity-100" : "opacity-0")}
                              />
                              {status.statusName}
                            </CommandItem>
                          ))}
                        </CommandGroup>
                      )}
                    </CommandList>
                  </Command>
                </PopoverContent>
              </Popover>
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
                <Popover open={channelManagerOpen} onOpenChange={setChannelManagerOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {field.value
                          ? personDetails.find((p) => p.id === field.value)
                            ? personDetails.find((p) => p.id === field.value)?.fullName || `${personDetails.find((p) => p.id === field.value)?.firstName || ''} ${personDetails.find((p) => p.id === field.value)?.lastName || ''}`.trim()
                            : "Select contact"
                          : "Select contact"}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search contacts..."
                        value={personSearch}
                        onValueChange={setPersonSearch}
                      />
                      <CommandList>
                        {isLoadingPersons ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : personDetails.length === 0 ? (
                          <CommandEmpty>No contact found.</CommandEmpty>
                        ) : (
                          <CommandGroup>
                            {personDetails.map((person) => (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setChannelManagerOpen(false);
                                  setPersonSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === person.id ? "opacity-100" : "opacity-0")}
                                />
                                {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim()}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
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
                <Popover open={regionalManagerOpen} onOpenChange={setRegionalManagerOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {field.value
                          ? personDetails.find((p) => p.id === field.value)
                            ? personDetails.find((p) => p.id === field.value)?.fullName || `${personDetails.find((p) => p.id === field.value)?.firstName || ''} ${personDetails.find((p) => p.id === field.value)?.lastName || ''}`.trim()
                            : "Select contact"
                          : "Select contact"}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search contacts..."
                        value={personSearch}
                        onValueChange={setPersonSearch}
                      />
                      <CommandList>
                        {isLoadingPersons ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : personDetails.length === 0 ? (
                          <CommandEmpty>No contact found.</CommandEmpty>
                        ) : (
                          <CommandGroup>
                            {personDetails.map((person) => (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setRegionalManagerOpen(false);
                                  setPersonSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === person.id ? "opacity-100" : "opacity-0")}
                                />
                                {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim()}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
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
                <Popover open={stateHeadOpen} onOpenChange={setStateHeadOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {field.value
                          ? personDetails.find((p) => p.id === field.value)
                            ? personDetails.find((p) => p.id === field.value)?.fullName || `${personDetails.find((p) => p.id === field.value)?.firstName || ''} ${personDetails.find((p) => p.id === field.value)?.lastName || ''}`.trim()
                            : "Select contact"
                          : "Select contact"}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search contacts..."
                        value={personSearch}
                        onValueChange={setPersonSearch}
                      />
                      <CommandList>
                        {isLoadingPersons ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : personDetails.length === 0 ? (
                          <CommandEmpty>No contact found.</CommandEmpty>
                        ) : (
                          <CommandGroup>
                            {personDetails.map((person) => (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setStateHeadOpen(false);
                                  setPersonSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === person.id ? "opacity-100" : "opacity-0")}
                                />
                                {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim()}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
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
                <Popover open={bankPersonOpen} onOpenChange={setBankPersonOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {field.value
                          ? personDetails.find((p) => p.id === field.value)
                            ? personDetails.find((p) => p.id === field.value)?.fullName || `${personDetails.find((p) => p.id === field.value)?.firstName || ''} ${personDetails.find((p) => p.id === field.value)?.lastName || ''}`.trim()
                            : "Select contact"
                          : "Select contact"}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search contacts..."
                        value={personSearch}
                        onValueChange={setPersonSearch}
                      />
                      <CommandList>
                        {isLoadingPersons ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : personDetails.length === 0 ? (
                          <CommandEmpty>No contact found.</CommandEmpty>
                        ) : (
                          <CommandGroup>
                            {personDetails.map((person) => (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setBankPersonOpen(false);
                                  setPersonSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === person.id ? "opacity-100" : "opacity-0")}
                                />
                                {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim()}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
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
                <Popover open={masterFranchiseeOpen} onOpenChange={setMasterFranchiseeOpen}>
                  <PopoverTrigger asChild>
                    <FormControl>
                      <Button
                        variant="outline"
                        role="combobox"
                        className={cn("w-full justify-between font-normal", !field.value && "text-muted-foreground")}
                      >
                        {field.value
                          ? personDetails.find((p) => p.id === field.value)
                            ? personDetails.find((p) => p.id === field.value)?.fullName || `${personDetails.find((p) => p.id === field.value)?.firstName || ''} ${personDetails.find((p) => p.id === field.value)?.lastName || ''}`.trim()
                            : "Select contact"
                          : "Select contact"}
                        <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                      </Button>
                    </FormControl>
                  </PopoverTrigger>
                  <PopoverContent className="w-full p-0" align="start">
                    <Command shouldFilter={false}>
                      <CommandInput
                        placeholder="Search contacts..."
                        value={personSearch}
                        onValueChange={setPersonSearch}
                      />
                      <CommandList>
                        {isLoadingPersons ? (
                          <div className="flex items-center justify-center py-6">
                            <Loader2 className="h-4 w-4 animate-spin" />
                          </div>
                        ) : personDetails.length === 0 ? (
                          <CommandEmpty>No contact found.</CommandEmpty>
                        ) : (
                          <CommandGroup>
                            {personDetails.map((person) => (
                              <CommandItem
                                key={person.id}
                                value={String(person.id)}
                                onSelect={() => {
                                  field.onChange(person.id);
                                  setMasterFranchiseeOpen(false);
                                  setPersonSearch("");
                                }}
                              >
                                <Check
                                  className={cn("mr-2 h-4 w-4", field.value === person.id ? "opacity-100" : "opacity-0")}
                                />
                                {person.fullName || `${person.firstName || ''} ${person.lastName || ''}`.trim()}
                              </CommandItem>
                            ))}
                          </CommandGroup>
                        )}
                      </CommandList>
                    </Command>
                  </PopoverContent>
                </Popover>
                <FormMessage />
              </FormItem>
            )}
          />
        </div>
      </div>
    </TabsContent>
  );
}
