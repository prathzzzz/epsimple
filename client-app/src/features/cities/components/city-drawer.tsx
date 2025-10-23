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
import { useCityContext } from "../context/city-provider";
import { cityApi } from "../api/city-api";
import { cityFormSchema, type CityFormData } from "../api/schema";
import { useQuery } from "@tanstack/react-query";
import api from "@/lib/api";

interface State {
  id: number;
  stateName: string;
  stateCode?: string;
}

interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export function CityDrawer() {
  const {
    isDrawerOpen,
    setIsDrawerOpen,
    editingCity,
    setEditingCity,
  } = useCityContext();

  const form = useForm<CityFormData>({
    resolver: zodResolver(cityFormSchema),
    defaultValues: {
      cityName: "",
      cityCode: "",
      stateId: 0,
    },
  });

  // Fetch states list for dropdown
  const { data: states } = useQuery<State[]>({
    queryKey: ["states", "list"],
    queryFn: async () => {
      const response = await api.get<ApiResponse<State[]>>("/api/states/list");
      return response.data.data;
    },
  });

  const createMutation = cityApi.useCreate();
  const updateMutation = cityApi.useUpdate();

  useEffect(() => {
    if (editingCity) {
      form.reset({
        cityName: editingCity.cityName,
        cityCode: editingCity.cityCode || "",
        stateId: editingCity.stateId,
      });
    } else {
      form.reset({
        cityName: "",
        cityCode: "",
        stateId: 0,
      });
    }
  }, [editingCity, form]);

  const onSubmit = (data: CityFormData) => {
    if (editingCity) {
      updateMutation.mutate(
        { id: editingCity.id, data },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingCity(null);
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
    setEditingCity(null);
    form.reset();
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {editingCity ? "Update" : "Create"} City
          </SheetTitle>
          <SheetDescription>
            {editingCity
              ? "Update the city by providing necessary info."
              : "Add a new city by providing necessary info."}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>
        <Form {...form}>
          <form
            id="city-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            <FormField
              control={form.control}
              name="cityName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>City Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="e.g., Mumbai" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="cityCode"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>City Code</FormLabel>
                  <FormControl>
                    <Input 
                      placeholder="e.g., MUM, DEL" 
                      {...field}
                      className="font-mono"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="stateId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>State *</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(parseInt(value))}
                    value={field.value ? field.value.toString() : ""}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a state" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {states?.map((state) => (
                        <SelectItem key={state.id} value={state.id.toString()}>
                          {state.stateName} {state.stateCode ? `(${state.stateCode})` : ""}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
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
            form="city-form"
            disabled={isLoading}
          >
            {isLoading ? (
              <>
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Saving...
              </>
            ) : editingCity ? (
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
