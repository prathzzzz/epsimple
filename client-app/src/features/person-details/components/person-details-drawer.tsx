import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
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
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { usePersonDetailsContext } from "../context/person-details-provider";
import { personDetailsApi } from "../api/person-details-api";
import { personDetailsFormSchema, type PersonDetailsFormData } from "../api/schema";
import { useQuery } from "@tanstack/react-query";
import api from "@/lib/api";

interface PersonType {
  id: number;
  typeName: string;
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export function PersonDetailsDrawer() {
  const {
    isDrawerOpen,
    setIsDrawerOpen,
    editingPersonDetails,
    setEditingPersonDetails,
  } = usePersonDetailsContext();

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

  // Fetch person types list for dropdown
  const { data: personTypes } = useQuery<PersonType[]>({
    queryKey: ["person-types", "list"],
    queryFn: async () => {
      const response = await api.get<ApiResponse<PersonType[]>>("/api/person-types/list");
      return response.data.data;
    },
  });

  const createMutation = personDetailsApi.useCreate();
  const updateMutation = personDetailsApi.useUpdate();

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
                  <Select
                    onValueChange={(value) => field.onChange(parseInt(value))}
                    value={field.value ? field.value.toString() : ""}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a person type" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {personTypes?.map((type) => (
                        <SelectItem key={type.id} value={type.id.toString()}>
                          {type.typeName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
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
