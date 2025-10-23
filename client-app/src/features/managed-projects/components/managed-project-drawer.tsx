import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
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
import { useManagedProjectContext } from "../context/managed-project-provider";
import { managedProjectApi } from "../api/managed-project-api";
import { managedProjectSchema, type ManagedProjectFormData } from "../api/schema";
import { getAllBanksList } from "@/lib/banks-api";

export function ManagedProjectDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingManagedProject, setEditingManagedProject } =
    useManagedProjectContext();

  const createMutation = managedProjectApi.useCreate();
  const updateMutation = managedProjectApi.useUpdate();

  const { data: banksResponse } = useQuery({
    queryKey: ["banks", "list"],
    queryFn: getAllBanksList,
  });

  const banks = banksResponse?.data || [];

  const form = useForm<ManagedProjectFormData>({
    resolver: zodResolver(managedProjectSchema),
    defaultValues: {
      bankId: 0,
      projectType: "",
      projectName: "",
      projectCode: "",
      projectDescription: "",
    },
  });

  useEffect(() => {
    if (editingManagedProject) {
      form.reset({
        bankId: editingManagedProject.bankId,
        projectType: editingManagedProject.projectType || "",
        projectName: editingManagedProject.projectName,
        projectCode: editingManagedProject.projectCode || "",
        projectDescription: editingManagedProject.projectDescription || "",
      });
    } else {
      form.reset({
        bankId: 0,
        projectType: "",
        projectName: "",
        projectCode: "",
        projectDescription: "",
      });
    }
  }, [editingManagedProject, form]);

  const onSubmit = async (data: ManagedProjectFormData) => {
    if (editingManagedProject) {
      updateMutation.mutate(
        {
          id: editingManagedProject.id,
          data,
        },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingManagedProject(null);
            form.reset();
          },
        }
      );
    } else {
      createMutation.mutate(data, {
        onSuccess: () => {
          setIsDrawerOpen(false);
          form.reset();
        },
      });
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setEditingManagedProject(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {editingManagedProject ? "Update" : "Create"} Managed Project
          </SheetTitle>
          <SheetDescription>
            {editingManagedProject
              ? "Update the managed project by providing necessary info."
              : "Add a new managed project by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="managed-project-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="bankId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bank *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value ? String(field.value) : ""}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a bank" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {banks.map((bank) => (
                        <SelectItem key={bank.id} value={String(bank.id)}>
                          {bank.bankName}
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
              name="projectName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Project Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter project name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="projectCode"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Project Code</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter project code" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="projectType"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Project Type</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter project type" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="projectDescription"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Project Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter project description"
                      className="min-h-[80px]"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>
        <SheetFooter className="flex-shrink-0 px-4">
          <SheetClose asChild>
            <Button variant="outline">Cancel</Button>
          </SheetClose>
          <Button
            type="submit"
            form="managed-project-form"
            disabled={isLoading}
          >
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {editingManagedProject ? "Update" : "Save"}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
