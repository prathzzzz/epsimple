import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
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
import { stateApi } from "@/features/states/api/state-api";

export function SiteCodeGeneratorDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingGenerator, setEditingGenerator } = useSiteCodeGeneratorContext();

  const createMutation = siteCodeGeneratorApi.useCreate();
  const updateMutation = siteCodeGeneratorApi.useUpdate();

  const { data: projectsResponse } = useQuery({
    queryKey: ["managed-projects", "list"],
    queryFn: () => managedProjectApi.getList(),
  });

  const { data: statesResponse } = useQuery({
    queryKey: ["states", "list"],
    queryFn: () => stateApi.getList(),
  });

  const projects = projectsResponse || [];
  const states = statesResponse || [];

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
    } catch (error) {
      // Error is handled by the mutation
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
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
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
                            {project.projectCode && ` (${project.projectCode})`}
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
                name="stateId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>State *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ""}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select state" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {states.map((state) => (
                          <SelectItem key={state.id} value={String(state.id)}>
                            {state.stateName} ({state.stateCode})
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
                name="stateId"
                render={({ field }) => (
                  <FormItem className="flex-1">
                    <FormLabel>State *</FormLabel>
                    <Select
                      onValueChange={(value) => field.onChange(Number(value))}
                      value={field.value ? String(field.value) : ""}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue placeholder="Select state" />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        {states.map((state) => (
                          <SelectItem key={state.id} value={String(state.id)}>
                            {state.stateName} ({state.stateCode})
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
            </div>

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
