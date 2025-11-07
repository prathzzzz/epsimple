import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import { cn } from "@/lib/utils";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import { useSiteCodeGeneratorContext } from "../context/site-code-generator-provider";
import { siteCodeGeneratorApi } from "../api/site-code-generator-api";
import { siteCodeGeneratorFormSchema, type SiteCodeGeneratorFormData } from "../api/schema";
import { managedProjectApi } from "@/features/managed-projects/api/managed-project-api";
import { statesApi } from "@/features/states/api/states-api";

export function SiteCodeGeneratorDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingGenerator, setEditingGenerator } = useSiteCodeGeneratorContext();

  const [projectSearch, setProjectSearch] = useState("");
  const [projectOpen, setProjectOpen] = useState(false);
  const [stateSearch, setStateSearch] = useState("");
  const [stateOpen, setStateOpen] = useState(false);

  const createMutation = siteCodeGeneratorApi.useCreate();
  const updateMutation = siteCodeGeneratorApi.useUpdate();

  const { data: projects = [], isLoading: isLoadingProjects } = 
    managedProjectApi.useSearch(projectSearch);
  const { data: states = [], isLoading: isLoadingStates } = 
    statesApi.useSearch(stateSearch);

  const form = useForm<SiteCodeGeneratorFormData>({
    resolver: zodResolver(siteCodeGeneratorFormSchema),
    defaultValues: {
      projectId: 0,
      stateId: 0,
      maxSeqDigit: 5,
      runningSeq: 1,
    },
  });

  useEffect(() => {
    if (editingGenerator) {
      form.reset({
        projectId: editingGenerator.projectId,
        stateId: editingGenerator.stateId,
        maxSeqDigit: editingGenerator.maxSeqDigit,
        runningSeq: editingGenerator.runningSeq,
      });
    } else {
      form.reset({
        projectId: 0,
        stateId: 0,
        maxSeqDigit: 5,
        runningSeq: 1,
      });
    }
  }, [editingGenerator, form]);

  const onSubmit = async (data: SiteCodeGeneratorFormData) => {
    try {
      if (editingGenerator) {
        await updateMutation.mutateAsync({ id: editingGenerator.id, data });
      } else {
        await createMutation.mutateAsync(data);
      }
      handleClose();
    } catch (_error) {
      // Error is already handled by mutation onError callbacks
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setEditingGenerator(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-2xl">
        <SheetHeader>
          <SheetTitle>{editingGenerator ? "Edit" : "Create"} Site Code Generator</SheetTitle>
          <SheetDescription>
            {editingGenerator
              ? "Update the site code generator configuration"
              : "Add a new site code generator configuration"}
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="flex-1 space-y-6 overflow-y-auto px-4 py-6">
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="projectId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Project *</FormLabel>
                    <Popover open={projectOpen} onOpenChange={setProjectOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              "w-full justify-between",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? projects.find((p) => p.id === field.value)?.projectName +
                                (projects.find((p) => p.id === field.value)?.projectCode
                                  ? ` (${projects.find((p) => p.id === field.value)?.projectCode})`
                                  : "")
                              : "Select project"}
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
                            {isLoadingProjects ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : projects.length === 0 ? (
                              <CommandEmpty>No projects found.</CommandEmpty>
                            ) : (
                              <CommandGroup>
                                {projects.map((project) => (
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
                                      className={cn(
                                        "mr-2 h-4 w-4",
                                        project.id === field.value ? "opacity-100" : "opacity-0"
                                      )}
                                    />
                                    {project.projectName}
                                    {project.projectCode && ` (${project.projectCode})`}
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
                name="stateId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>State *</FormLabel>
                    <Popover open={stateOpen} onOpenChange={setStateOpen}>
                      <PopoverTrigger asChild>
                        <FormControl>
                          <Button
                            variant="outline"
                            role="combobox"
                            className={cn(
                              "w-full justify-between",
                              !field.value && "text-muted-foreground"
                            )}
                          >
                            {field.value
                              ? `${states.find((s) => s.id === field.value)?.stateName} (${states.find((s) => s.id === field.value)?.stateCode})`
                              : "Select state"}
                            <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                          </Button>
                        </FormControl>
                      </PopoverTrigger>
                      <PopoverContent className="w-full p-0" align="start">
                        <Command shouldFilter={false}>
                          <CommandInput
                            placeholder="Search states..."
                            value={stateSearch}
                            onValueChange={setStateSearch}
                          />
                          <CommandList>
                            {isLoadingStates ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : states.length === 0 ? (
                              <CommandEmpty>No states found.</CommandEmpty>
                            ) : (
                              <CommandGroup>
                                {states.map((state) => (
                                  <CommandItem
                                    key={state.id}
                                    value={String(state.id)}
                                    onSelect={() => {
                                      field.onChange(state.id);
                                      setStateOpen(false);
                                      setStateSearch("");
                                    }}
                                  >
                                    <Check
                                      className={cn(
                                        "mr-2 h-4 w-4",
                                        state.id === field.value ? "opacity-100" : "opacity-0"
                                      )}
                                    />
                                    {state.stateName} ({state.stateCode})
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
                name="maxSeqDigit"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Max Sequence Digits</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        placeholder="Enter max sequence digits"
                        {...field}
                        onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : 5)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="runningSeq"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>Running Sequence</FormLabel>
                    <FormControl>
                      <Input
                        type="number"
                        placeholder="Enter running sequence"
                        {...field}
                        onChange={(e) => field.onChange(e.target.value ? Number(e.target.value) : 1)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <SheetFooter className="px-4">
              <SheetClose asChild>
                <Button variant="outline" type="button">
                  Cancel
                </Button>
              </SheetClose>
              <Button type="submit" disabled={isLoading}>
                {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
                {editingGenerator ? "Update" : "Create"}
              </Button>
            </SheetFooter>
          </form>
        </Form>
      </SheetContent>
    </Sheet>
  );
}
