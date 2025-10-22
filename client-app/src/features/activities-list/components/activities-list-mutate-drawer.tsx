import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
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
import { Loader2 } from "lucide-react";
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
import { SelectDropdown } from "@/components/select-dropdown";

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

  const form = useForm<ActivitiesListFormData>({
    resolver: zodResolver(activitiesListFormSchema),
    defaultValues: {
      activityId: undefined as any,
      activityName: "",
      activityCategory: "",
      activityDescription: "",
    },
  });

  // Fetch activity list for dropdown
  const { data: activitiesData } = useQuery({
    queryKey: ["activity-dropdown"],
    queryFn: () => activitiesApi.getList(),
  });

  const activities = activitiesData?.data || [];

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
        activityId: undefined as any,
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
                  <FormControl>
                    <SelectDropdown
                      defaultValue={field.value?.toString()}
                      onValueChange={(value) => field.onChange(Number(value))}
                      placeholder="Select an activity"
                      isControlled={true}
                      items={activities.map((activity) => ({
                        value: activity.id.toString(),
                        label: activity.activityName,
                      }))}
                    />
                  </FormControl>
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
