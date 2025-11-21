import { useState } from "react";
import {
  Download,
  Plus,
  Upload,
  Loader2,
  FileSpreadsheet,
  ChevronDown,
  FileUp,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { PermissionGuard } from "@/components/permission-guard";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useInvoice } from "../hooks/use-invoice";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";
import { toast } from "sonner";

export function InvoicePrimaryButtons() {
  const {
    setSelectedInvoice,
    setIsDrawerOpen,
    setIsEditMode,
    setIsBulkUploadDialogOpen,
  } = useInvoice();

  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);

  const { isExporting, handleExport } = useExport({
    entityName: "Invoice",
    exportEndpoint: "/api/invoices/export",
  });

  const handleCreate = () => {
    setSelectedInvoice(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile(
        "/api/invoices/bulk-upload/template",
        "Invoice_Upload_Template.xlsx"
      );
      toast.success("Template downloaded successfully");
    } catch (_error) {
      toast.error("Failed to download template");
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleBulkUpload = () => {
    setIsBulkUploadDialogOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <PermissionGuard anyPermissions={["INVOICE:BULK_UPLOAD", "INVOICE:EXPORT"]}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" size="sm" className="h-9 px-3">
              <FileUp className="mr-2 h-4 w-4" />
              Bulk Actions
              <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-64">
            <PermissionGuard permission="INVOICE:BULK_UPLOAD">
              <DropdownMenuItem
                onClick={handleDownloadTemplate}
                disabled={isDownloadingTemplate}
              >
                {isDownloadingTemplate ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-600" />
                ) : (
                  <Download className="mr-2 h-4 w-4 text-blue-600" />
                )}
                <span>Download Template</span>
              </DropdownMenuItem>
              <DropdownMenuItem onClick={handleBulkUpload}>
                <Upload className="mr-2 h-4 w-4 text-orange-600" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="INVOICE:EXPORT">
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

      <PermissionGuard permission="INVOICE:CREATE">
        <Button onClick={handleCreate} size="sm" className="h-9">
          <Plus className="mr-2 h-4 w-4" />
          Add Invoice
        </Button>
      </PermissionGuard>
    </div>
  );
}
