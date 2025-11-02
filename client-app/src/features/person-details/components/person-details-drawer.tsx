import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Check, ChevronsUpDown, Loader2 } from "lucide-react";
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
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
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
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { usePersonDetailsContext } from "../context/person-details-provider";
import { personDetailsApi } from "../api/person-details-api";
import { personDetailsFormSchema, type PersonDetailsFormData } from "../api/schema";
import { personTypesApi } from "@/lib/person-types-api";

export function PersonDetailsDrawer() {
  const {
    isDrawerOpen,
    setIsDrawerOpen,
    editingPersonDetails,
    setEditingPersonDetails,
  } = usePersonDetailsContext();

  const [personTypeSearch, setPersonTypeSearch] = useState("");
  const [personTypeOpen, setPersonTypeOpen] = useState(false);

  const form = useForm<PersonDetailsFormData>({
    resolver: zodResolver(personDetailsFormSchema),
    defaultValues: {
      personTypeId: 0,
      firstName: "",
      middleName: "",
      lastName: "",
      contactNumber: "",
      email: "",
      permanentAddress: "",
      correspondenceAddress: "",
    },
  });

  const { data: personTypes = [], isLoading: isLoadingPersonTypes } = 
    personTypesApi.useSearch(personTypeSearch);
  const { data: allPersonTypes = [] } = personTypesApi.useSearch('');

  const createMutation = personDetailsApi.useCreate();
  const updateMutation = personDetailsApi.useUpdate();

  // Display logic for person types dropdown
  const displayPersonTypes = (() => {
    if (!editingPersonDetails?.personTypeId) return personTypes;
    const selectedType = allPersonTypes.find((t) => t.id === editingPersonDetails.personTypeId);
    if (!selectedType || personTypes.some((t) => t.id === selectedType.id)) {
      return personTypes;
    }
    return [selectedType, ...personTypes];
  })();

  useEffect(() => {
    if (editingPersonDetails) {
      form.reset({
        personTypeId: editingPersonDetails.personTypeId,
        firstName: editingPersonDetails.firstName || "",
        middleName: editingPersonDetails.middleName || "",
        lastName: editingPersonDetails.lastName || "",
        contactNumber: editingPersonDetails.contactNumber || "",
        email: editingPersonDetails.email,
        permanentAddress: editingPersonDetails.permanentAddress || "",
        correspondenceAddress: editingPersonDetails.correspondenceAddress || "",
      });
    } else {
      form.reset({
        personTypeId: 0,
        firstName: "",
        middleName: "",
        lastName: "",
        contactNumber: "",
        email: "",
        permanentAddress: "",
        correspondenceAddress: "",
      });
    }
  }, [editingPersonDetails, form]);

  const onSubmit = (data: PersonDetailsFormData) => {
    if (editingPersonDetails) {
      updateMutation.mutate(
        { id: editingPersonDetails.id, data },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingPersonDetails(null);
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
    setEditingPersonDetails(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col sm:max-w-[600px]">
        <SheetHeader className="text-start">
          <SheetTitle>
            {editingPersonDetails ? "Update" : "Create"} Person Details
          </SheetTitle>
          <SheetDescription>
            {editingPersonDetails
              ? "Update the person details by providing necessary info."
              : "Add a new person by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="person-details-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="personTypeId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Person Type *</FormLabel>
                  <Popover open={personTypeOpen} onOpenChange={setPersonTypeOpen}>
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
                            ? displayPersonTypes.find((type) => type.id === field.value)?.typeName
                            : "Select a person type"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-full p-0" align="start">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search person types..."
                          value={personTypeSearch}
                          onValueChange={setPersonTypeSearch}
                        />
                        <CommandList>
                          {isLoadingPersonTypes ? (
                            <div className="flex items-center justify-center py-6">
                              <Loader2 className="h-4 w-4 animate-spin" />
                            </div>
                          ) : displayPersonTypes.length === 0 ? (
                            <CommandEmpty>No person types found.</CommandEmpty>
                          ) : (
                            <CommandGroup>
                              {displayPersonTypes.map((type) => (
                                <CommandItem
                                  key={type.id}
                                  value={String(type.id)}
                                  onSelect={() => {
                                    field.onChange(type.id);
                                    setPersonTypeOpen(false);
                                    setPersonTypeSearch("");
                                  }}
                                >
                                  <Check
                                    className={cn(
                                      "mr-2 h-4 w-4",
                                      type.id === field.value ? "opacity-100" : "opacity-0"
                                    )}
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

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-3">
              <FormField
                control={form.control}
                name="firstName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>First Name</FormLabel>
                    <FormControl>
                      <Input placeholder="e.g., John" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="middleName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Middle Name</FormLabel>
                    <FormControl>
                      <Input placeholder="e.g., Robert" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="lastName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Last Name</FormLabel>
                    <FormControl>
                      <Input placeholder="e.g., Doe" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email *</FormLabel>
                    <FormControl>
                      <Input
                        type="email"
                        placeholder="e.g., john.doe@example.com"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="contactNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Contact Number</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="10-digit number"
                        maxLength={10}
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            <FormField
              control={form.control}
              name="permanentAddress"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Permanent Address</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter permanent address"
                      className="min-h-[80px]"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="correspondenceAddress"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Correspondence Address</FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="Enter correspondence address"
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
        <SheetFooter className="mt-4 gap-2 px-4 sm:space-x-0">
          <SheetClose asChild>
            <Button
              type="button"
              variant="outline"
              disabled={isLoading}
            >
              Cancel
            </Button>
          </SheetClose>
          <Button
            type="submit"
            form="person-details-form"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : editingPersonDetails ? (
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
