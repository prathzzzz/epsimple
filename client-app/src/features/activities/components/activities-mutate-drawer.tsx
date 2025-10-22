import { useEffect } from "react";
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

import { activitiesApi } from "../api/activities-api";
import {
  activityFormSchema,
  type ActivityFormData,
  type Activity,
} from "../api/schema";

interface ActivitiesMutateDrawerProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  currentRow: Activity | null;
}

export function ActivitiesMutateDrawer({
  open,
  onOpenChange,
  currentRow,
}: ActivitiesMutateDrawerProps) {
  const queryClient = useQueryClient();
  const isUpdate = !!currentRow;

  const form = useForm<ActivityFormData>({
    resolver: zodResolver(activityFormSchema),
    defaultValues: {
      activityName: "",
      activityDescription: "",
    },
  });

  // Reset form when currentRow changes
  useEffect(() => {
    if (currentRow) {
      form.reset({
        activityName: currentRow.activityName,
        activityDescription: currentRow.activityDescription || "",
      });
    } else {
      form.reset({
        activityName: "",
        activityDescription: "",
      });
    }
  }, [currentRow, form]);

  const createMutation = useMutation({
    mutationFn: activitiesApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activities"] });
      toast.success("Activity created successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: number; data: ActivityFormData }) =>
      activitiesApi.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["activities"] });
      toast.success("Activity updated successfully");
      form.reset();
      onOpenChange(false);
    },
  });

  const onSubmit = (data: ActivityFormData) => {
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
          <SheetTitle>{isUpdate ? "Update" : "Create"} Activity</SheetTitle>
          <SheetDescription>
            {isUpdate
              ? "Update the activity by providing necessary info."
              : "Add a new activity by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="activities-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
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
            form="activities-form"
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
