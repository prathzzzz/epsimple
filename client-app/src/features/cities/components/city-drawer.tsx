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
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover";
import { Command, CommandInput, CommandList, CommandEmpty, CommandItem } from "@/components/ui/command";
import { cn } from "@/lib/utils";
import { Input } from "@/components/ui/input";
import { useCityContext } from "../context/city-provider";
import { cityApi } from "../api/city-api";
import { cityFormSchema, type CityFormData } from "../api/schema";
import { statesApi } from "@/features/states/api/states-api";

export function CityDrawer() {
  const {
    isDrawerOpen,
    setIsDrawerOpen,
    editingCity,
    setEditingCity,
  } = useCityContext();
  const [stateSearch, setStateSearch] = useState("");
  const [stateOpen, setStateOpen] = useState(false);

  const form = useForm<CityFormData>({
    resolver: zodResolver(cityFormSchema),
    defaultValues: {
      cityName: "",
      cityCode: "",
      stateId: 0,
    },
  });

  // Fetch states using search hook
  const { data: states = [], isLoading: isLoadingStates } = statesApi.useSearch(stateSearch);
  
  // Fetch all states to get the selected state when editing
  const { data: allStatesData = [] } = statesApi.useSearch("");
  
  // Combine search results with selected state to ensure it's always visible
  const displayStates = (() => {
    if (!editingCity?.stateId) return states;
    const selectedState = allStatesData.find(s => s.id === editingCity.stateId);
    if (!selectedState) return states;
    // Check if selected state is already in the states list
    if (states.some(s => s.id === selectedState.id)) return states;
    // Add selected state to the top of the list
    return [selectedState, ...states];
  })();

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
                  <Popover open={stateOpen} onOpenChange={setStateOpen}>
                    <PopoverTrigger asChild>
                      <FormControl>
                        <Button
                          variant="outline"
                          role="combobox"
                          aria-expanded={stateOpen}
                          className={cn(
                            "w-full justify-between",
                            !field.value && "text-muted-foreground"
                          )}
                        >
                          {field.value
                            ? displayStates.find((s) => s.id === field.value)?.stateName || "Select state"
                            : "Select a state"}
                          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                        </Button>
                      </FormControl>
                    </PopoverTrigger>
                    <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                      <Command shouldFilter={false}>
                        <CommandInput
                          placeholder="Search states..."
                          value={stateSearch}
                          onValueChange={setStateSearch}
                        />
                        <CommandList>
                          <CommandEmpty>
                            {isLoadingStates ? (
                              <div className="flex items-center justify-center py-6">
                                <Loader2 className="h-4 w-4 animate-spin" />
                              </div>
                            ) : (
                              "No state found."
                            )}
                          </CommandEmpty>
                          {displayStates.map((state) => (
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
                                  field.value === state.id ? "opacity-100" : "opacity-0"
                                )}
                              />
                              {state.stateName} {state.stateCode ? `(${state.stateCode})` : ""}
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
