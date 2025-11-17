import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
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

import { activitiesListApi } from "../api/activities-list-api";
import {
  activitiesListFormSchema,
  type ActivitiesListFormData,
  type ActivitiesList,
} from "../api/schema";
import { activitiesApi } from "@/features/activities/api/activities-api";

interface ActivitiesListMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: ActivitiesList | null;
}

export function ActivitiesListMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: ActivitiesListMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const [activitySearch, setActivitySearch] = useState("");
  const [activityOpen, setActivityOpen] = useState(false);

  const form = useForm<ActivitiesListFormData>({
    resolver: zodResolver(activitiesListFormSchema),
    defaultValues: {
      activityId: undefined,
      activityName: "",
      activityCategory: "",
      activityDescription: "",
    },
  });

  // Fetch activities with search
  const { data: activities = [], isLoading: isLoadingActivities } =
    activitiesApi.useSearch(activitySearch);
  const { data: allActivities = [] } = activitiesApi.useSearch("");

  // Display logic for activities dropdown
  const displayActivities = (() => {
    if (!currentRow?.activityId) return activities;
    const selectedActivity = allActivities.find((a) => a.id === currentRow.activityId);
    if (!selectedActivity || activities.some((a) => a.id === selectedActivity.id)) {
      return activities;
    }
    return [selectedActivity, ...activities];
  })();

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        activityId: currentRow.activityId,
        activityName: currentRow.activityName,
        activityCategory: currentRow.activityCategory || "",
        activityDescription: currentRow.activityDescription || "",
      });
    } else {
      form.reset({
        activityId: undefined,
        activityName: "",
        activityCategory: "",
        activityDescription: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: activitiesListApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activities"] });
      toast.success("Activities entry created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: ActivitiesListFormData }) =>
      activitiesListApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activities"] });
      toast.success("Activities entry updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: ActivitiesListFormData) => {
    if (isUpdate && currentRow) {
      updateMutation.mutate({ id: currentRow.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>{isUpdate ? "Update" : "Create"} Activities</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the activities entry by providing necessary info."
              : "Add a new activities entry by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="activities-list-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="activityId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Activity *</FormLabel>
                  <Popover open={activityOpen} onOpenChange={setActivityOpen}>
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
                            ? displayActivities.find((a) => a.id === field.value)?.activityName
                            : "Select an activity"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-full p-0" align="start">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search activities..."
                          value={activitySearch}
                          onValueChange={setActivitySearch}
                        />
                        <CommandList>
                          {isLoadingActivities ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayActivities.length === 0 ? (
                            <CommandEmpty>No activities found.</CommandEmpty>
                          ) : (
                            <CommandGroup>
                              {displayActivities.map((activity) => (
                                <CommandItem
                                  key={activity.id}
                                  value={String(activity.id)}
                                  onSelect={() => {
                                    field.onChange(activity.id);
                                    setActivityOpen(false);
                                    setActivitySearch("");
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      "mr-2 h-4 w-4",
                                      activity.id === field.value ? "opacity-100" : "opacity-0"
                                    )}
                                  />
                                  {activity.activityName}
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
              name="activityName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Activity Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter activity name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="activityCategory"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Activity Category</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter activity category" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="activityDescription"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter description"
                      className="resize-none"
                      rows={4}
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>
        <SheetFooter className="mt-4 gap-2 px-4 sm:space-x-0">
          <SheetClose asChild>
            <Button
              type="button"
              variant="outline"
              disabled={createMutation.isPending || updateMutation.isPending}
            >
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="activities-list-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {createMutation.isPending || updateMutation.isPending ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : isUpdate ? (
              "Update"
            ) : (
              "Create"
            )}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
