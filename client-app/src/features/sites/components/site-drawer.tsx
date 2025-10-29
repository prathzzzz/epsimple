import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Loader2 } from "lucide-react";
import { useQuery } from "@tanstack/react-query";

import { Button } from "@/components/ui/button";
import { Form } from "@/components/ui/form";
import {
  Sheet,
  SheetClose,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useSiteContext } from "../context/site-provider";
import { siteApi } from "../api/site-api";
import { siteSchema, type SiteFormData } from "../api/schema";
import { locationApi } from "@/features/locations/api/location-api";
import { managedProjectApi } from "@/features/managed-projects/api/managed-project-api";
import { siteCategoryApi } from "@/features/site-categories/api/site-category-api";
import { siteTypeApi } from "@/features/site-types/api/site-type-api";
import { genericStatusTypeApi } from "@/features/generic-status-types/api/generic-status-type-api";
import { personDetailsApi } from "@/features/person-details/api/person-details-api";
import { stateApi } from "@/features/states/api/state-api";
import {
  BasicTab,
  DatesTab,
  InfrastructureTab,
  NetworkTab,
  TechnicalTab,
  CassettesTab,
} from "./site-drawer-tabs";

export function SiteDrawer() {
  const { isDrawerOpen, setIsDrawerOpen, editingSite, setEditingSite } = useSiteContext();
  const [activeTab, setActiveTab] = useState("basic");

  const createMutation = siteApi.useCreate();
  const updateMutation = siteApi.useUpdate();

  const { data: locationsResponse } = useQuery({
    queryKey: ["locations", "list"],
    queryFn: () => locationApi.getList(),
  });

  const { data: projectsResponse } = useQuery({
    queryKey: ["managed-projects", "list"],
    queryFn: () => managedProjectApi.getList(),
  });

  const { data: categoriesResponse } = useQuery({
    queryKey: ["site-categories", "list"],
    queryFn: () => siteCategoryApi.getList(),
  });

  const { data: typesResponse } = useQuery({
    queryKey: ["site-types", "list"],
    queryFn: () => siteTypeApi.getList(),
  });

  const { data: statusResponse } = useQuery({
    queryKey: ["generic-status-types", "list"],
    queryFn: () => genericStatusTypeApi.getList(),
  });

  const { data: personDetailsResponse } = useQuery({
    queryKey: ["person-details", "list"],
    queryFn: () => personDetailsApi.getList(),
  });

  const { data: statesResponse } = useQuery({
    queryKey: ["states", "list"],
    queryFn: () => stateApi.getList(),
  });

  const locations = locationsResponse || [];
  const projects = projectsResponse || [];
  const categories = categoriesResponse || [];
  const types = typesResponse || [];
  const statuses = statusResponse?.data || [];
  const personDetails = personDetailsResponse || [];
  const states = statesResponse || [];

  const form = useForm<SiteFormData>({
    resolver: zodResolver(siteSchema),
    defaultValues: {
      siteCode: "",
      locationId: 0,
      projectId: null,
      siteCategoryId: null,
      siteTypeId: null,
      siteStatusId: null,
      projectPhase: "",
      oldSiteCode: "",
      previousMspTermId: "",
      locationClass: "",
      techLiveDate: null,
      cashLiveDate: null,
      siteCloseDate: null,
      possessionDate: null,
      actualPossessionDate: null,
      groutingStatus: "",
      itStabilizer: "",
      rampStatus: "",
      upsBatteryBackupCapacity: "",
      connectivityType: "",
      acUnits: "",
      mainDoorGlassWidth: null,
      fixedGlassWidth: null,
      signboardSize: "",
      brandingSize: "",
      channelManagerContactId: null,
      regionalManagerContactId: null,
      stateHeadContactId: null,
      bankPersonContactId: null,
      masterFranchiseeContactId: null,
      gatewayIp: "",
      atmIp: "",
      subnetMask: "",
      natIp: "",
      switchIp: "",
      tlsPort: "",
      tlsDomainName: "",
      ejDocket: "",
      tssDocket: "",
      otcActivationStatus: "",
      otcActivationDate: null,
      craName: "",
      cassetteSwapStatus: "",
      cassetteType1: "",
      cassetteType2: "",
      cassetteType3: "",
      cassetteType4: "",
    },
  });

  useEffect(() => {
    if (editingSite) {
      form.reset({
        siteCode: editingSite.siteCode,
        locationId: editingSite.locationId,
        projectId: editingSite.projectId || null,
        siteCategoryId: editingSite.siteCategoryId || null,
        siteTypeId: editingSite.siteTypeId || null,
        siteStatusId: editingSite.siteStatusId || null,
        projectPhase: editingSite.projectPhase || "",
        oldSiteCode: editingSite.oldSiteCode || "",
        previousMspTermId: editingSite.previousMspTermId || "",
        locationClass: editingSite.locationClass || "",
        techLiveDate: editingSite.techLiveDate || null,
        cashLiveDate: editingSite.cashLiveDate || null,
        siteCloseDate: editingSite.siteCloseDate || null,
        possessionDate: editingSite.possessionDate || null,
        actualPossessionDate: editingSite.actualPossessionDate || null,
        groutingStatus: editingSite.groutingStatus || "",
        itStabilizer: editingSite.itStabilizer || "",
        rampStatus: editingSite.rampStatus || "",
        upsBatteryBackupCapacity: editingSite.upsBatteryBackupCapacity || "",
        connectivityType: editingSite.connectivityType || "",
        acUnits: editingSite.acUnits || "",
        mainDoorGlassWidth: editingSite.mainDoorGlassWidth || null,
        fixedGlassWidth: editingSite.fixedGlassWidth || null,
        signboardSize: editingSite.signboardSize || "",
        brandingSize: editingSite.brandingSize || "",
        channelManagerContactId: editingSite.channelManagerContactId || null,
        regionalManagerContactId: editingSite.regionalManagerContactId || null,
        stateHeadContactId: editingSite.stateHeadContactId || null,
        bankPersonContactId: editingSite.bankPersonContactId || null,
        masterFranchiseeContactId: editingSite.masterFranchiseeContactId || null,
        gatewayIp: editingSite.gatewayIp || "",
        atmIp: editingSite.atmIp || "",
        subnetMask: editingSite.subnetMask || "",
        natIp: editingSite.natIp || "",
        switchIp: editingSite.switchIp || "",
        tlsPort: editingSite.tlsPort || "",
        tlsDomainName: editingSite.tlsDomainName || "",
        ejDocket: editingSite.ejDocket || "",
        tssDocket: editingSite.tssDocket || "",
        otcActivationStatus: editingSite.otcActivationStatus || "",
        otcActivationDate: editingSite.otcActivationDate || null,
        craName: editingSite.craName || "",
        cassetteSwapStatus: editingSite.cassetteSwapStatus || "",
        cassetteType1: editingSite.cassetteType1 || "",
        cassetteType2: editingSite.cassetteType2 || "",
        cassetteType3: editingSite.cassetteType3 || "",
        cassetteType4: editingSite.cassetteType4 || "",
      });
    } else {
      form.reset();
    }
  }, [editingSite, form]);

  const onSubmit = async (data: SiteFormData) => {
    if (editingSite) {
      updateMutation.mutate(
        {
          id: editingSite.id,
          data,
        },
        {
          onSuccess: () => {
            setIsDrawerOpen(false);
            setEditingSite(null);
            form.reset();
            setActiveTab("basic");
          },
        }
      );
    } else {
      createMutation.mutate(data, {
        onSuccess: () => {
          setIsDrawerOpen(false);
          form.reset();
          setActiveTab("basic");
        },
      });
    }
  };

  const handleClose = () => {
    setIsDrawerOpen(false);
    setEditingSite(null);
    form.reset();
    setActiveTab("basic");
  };

  const isLoading = createMutation.isPending || updateMutation.isPending;

  return (
    <Sheet open={isDrawerOpen} onOpenChange={handleClose}>
      <SheetContent className="flex w-full flex-col sm:max-w-3xl overflow-hidden">
        <SheetHeader className="text-start">
          <SheetTitle>{editingSite ? "Update" : "Create"} Site</SheetTitle>
          <SheetDescription>
            {editingSite
              ? "Update the site by providing necessary info."
              : "Add a new site by providing necessary info. "}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="site-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 overflow-hidden flex flex-col"
          >
            <Tabs value={activeTab} onValueChange={setActiveTab} className="flex-1 flex flex-col overflow-hidden">
              <TabsList className="grid grid-cols-6 flex-shrink-0 mx-4 mb-1 w-[calc(100%-2rem)]">
                <TabsTrigger value="basic" className="text-xs sm:text-sm">Basic</TabsTrigger>
                <TabsTrigger value="dates" className="text-xs sm:text-sm">Dates</TabsTrigger>
                <TabsTrigger value="infrastructure" className="text-xs sm:text-sm">Infrastructure</TabsTrigger>
                <TabsTrigger value="network" className="text-xs sm:text-sm">Network</TabsTrigger>
                <TabsTrigger value="technical" className="text-xs sm:text-sm">Technical</TabsTrigger>
                <TabsTrigger value="cassettes" className="text-xs sm:text-sm">Cassettes</TabsTrigger>
              </TabsList>

              <BasicTab
                form={form}
                locations={locations}
                projects={projects}
                categories={categories}
                types={types}
                statuses={statuses}
                personDetails={personDetails}
                states={states}
              />
              <DatesTab form={form} />
              <InfrastructureTab form={form} />
              <NetworkTab form={form} />
              <TechnicalTab form={form} />
              <CassettesTab form={form} />
            </Tabs>
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 px-4">
          <SheetClose asChild>
            <Button variant="outline">Cancel</Button>
          </SheetClose>
          <Button type="submit" form="site-form" disabled={isLoading}>
            {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
            {editingSite ? "Update" : "Save"}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
