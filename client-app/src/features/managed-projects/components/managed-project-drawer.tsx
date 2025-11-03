import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";

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
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem } from "@/components/ui/command";
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
import { useManagedProjectContext } from "../context/managed-project-provider";
import { managedProjectApi } from "../api/managed-project-api";
import { managedProjectSchema, type ManagedProjectFormData } from "../api/schema";
import { useSearchBanks, type Bank } from "@/features/banks/api/banks-api";

export function ManagedProjectDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingManagedProject, setEditingManagedProject } =
    useManagedProjectContext();
  const [bankSearch, setBankSearch] = useState("");
  const [bankOpen, setBankOpen] = useState(false);

  const createMutation = managedProjectApi.useCreate();
  const updateMutation = managedProjectApi.useUpdate();

  const { data: banks = [], isLoading: isLoadingBanks } = useSearchBanks(bankSearch);
  
  // Fetch initial banks to ensure selected bank is displayed when editing
  const { data: allBanks = [] } = useSearchBanks("");
  
  // Combine search results with selected bank
  const displayBanks = (() => {
    if (!editingManagedProject?.bankId) return banks;
    const selectedBank = allBanks.find((b: Bank) => b.id === editingManagedProject.bankId);
    if (!selectedBank) return banks;
    // Check if selected bank is already in the banks list
    if (banks.some((b: Bank) => b.id === selectedBank.id)) return banks;
    // Add selected bank to the top of the list
    return [selectedBank, ...banks];
  })();

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
    // Convert empty strings to undefined for optional fields only
    const payload = {
      ...data,
      projectType: data.projectType || undefined,
      projectDescription: data.projectDescription || undefined,
    };

    if (editingManagedProject) {
      updateMutation.mutate(
        {
          id: editingManagedProject.id,
          data: payload,
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
      createMutation.mutate(payload, {
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
                  <Popover open={bankOpen} onOpenChange={setBankOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={bankOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayBanks.find((b: Bank) => b.id === field.value)?.bankName || "Select bank"
                            : "Select a bank"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search banks..."
                          value={bankSearch}
                          onValueChange={setBankSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingBanks ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No bank found."
                            )}
                          </CommandEmpty>
                          {displayBanks.map((bank: Bank) => (
                            <CommandItem
                              key={bank.id}
                              value={String(bank.id)}
                              onSelect={() => {
                                field.onChange(bank.id);
                                setBankOpen(false);
                                setBankSearch("");
                              }}
                            >
                              <Check
                                className={cn(
                                  "mr-2 h-4 w-4",
                                  field.value === bank.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {bank.bankName}
                            </CommandItem>
                          ))}
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

            <FormField
              control={form.control}
              name="projectCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Project Code *</FormLabel>
                  <FormControl>
                    <Input 
                      placeholder="e.g., PROJ-001 (letters, numbers, - and _ only)" 
                      className="font-mono"
                      {...field} 
                    />
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
                    <Input placeholder="e.g., Infrastructure, Development, etc." {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="projectDescription"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Project Description</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter project description (optional)"
                      className="min-h-[80px] resize-none"
                      rows={3}
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
