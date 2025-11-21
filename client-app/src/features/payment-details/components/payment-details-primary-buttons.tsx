import { Download, Plus, Upload, Loader2, FileUp, ChevronDown, FileSpreadsheet } from "lucide-react";
import { Button } from "@/components/ui/button";
import { PermissionGuard } from "@/components/permission-guard";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { usePaymentDetails } from "../context/payment-details-provider";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";
import { useState } from "react";
import { toast } from "sonner";

export function PaymentDetailsPrimaryButtons() {
  const { setSelectedPaymentDetails, setIsDrawerOpen, setIsEditMode, openBulkUploadDialog } =
    usePaymentDetails();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);

  const { isExporting, handleExport } = useExport({
    entityName: "PaymentDetails",
    exportEndpoint: "/api/payment-details/export",
  });

  const handleCreate = () => {
    setSelectedPaymentDetails(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile(
        "/api/payment-details/download-template",
        "PaymentDetails_Upload_Template.xlsx"
      );
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template", {
        description: error instanceof Error ? error.message : "An error occurred",
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      <PermissionGuard anyPermissions={["PAYMENT:BULK_UPLOAD", "PAYMENT:EXPORT"]}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" size="sm" className="h-9 px-3">
              <FileUp className="mr-2 h-4 w-4" />
              Bulk Actions
              <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="start" className="w-64">
            <PermissionGuard permission="PAYMENT:BULK_UPLOAD">
              <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
                {isDownloadingTemplate ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-600" />
                ) : (
                  <Download className="mr-2 h-4 w-4 text-blue-600" />
                )}
                <span>Download Template</span>
              </DropdownMenuItem>
              <DropdownMenuItem onClick={openBulkUploadDialog}>
                <Upload className="mr-2 h-4 w-4 text-orange-600" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="PAYMENT:EXPORT">
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleExport} disabled={isExporting}>
                {isExporting ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-600" />
                ) : (
                  <FileSpreadsheet className="mr-2 h-4 w-4 text-green-600" />
                )}
                <span>Export All Data</span>
              </DropdownMenuItem>
            </PermissionGuard>
          </DropdownMenuContent>
        </DropdownMenu>
      </PermissionGuard>

      <PermissionGuard permission="PAYMENT:CREATE">
        <Button onClick={handleCreate} size="sm" className="h-9">
          <Plus className="mr-2 h-4 w-4" />
          Add Payment Details
        </Button>
      </PermissionGuard>
    </div>
  );
}
