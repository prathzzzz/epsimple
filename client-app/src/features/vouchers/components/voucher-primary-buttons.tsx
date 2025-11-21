import { useState } from "react";
import {
  Plus,
  FileUp,
  ChevronDown,
  Download,
  Upload,
  FileSpreadsheet,
  Loader2,
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
import { useVoucher } from "../hooks/use-voucher";
import { downloadFile } from "@/lib/api-utils";
import { toast } from "sonner";

export function VoucherPrimaryButtons() {
  const { setIsDrawerOpen, setEditingVoucher, setIsBulkUploadDialogOpen } =
    useVoucher();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const handleAdd = () => {
    setEditingVoucher(null);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, "-").slice(0, -5);
      await downloadFile(
        "/api/vouchers/bulk-upload/template",
        `Voucher_Upload_Template_${timestamp}.xlsx`
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

  const handleBulkUpload = () => {
    setIsBulkUploadDialogOpen(true);
  };

  const handleExport = async () => {
    setIsExporting(true);
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, "-").slice(0, -5);
      await downloadFile(
        "/api/vouchers/export",
        `Vouchers_Export_${timestamp}.xlsx`
      );
      toast.success("Vouchers exported successfully");
    } catch (error) {
      toast.error("Failed to export vouchers", {
        description: error instanceof Error ? error.message : "An error occurred",
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className="flex gap-2">
      <PermissionGuard anyPermissions={["VOUCHER:BULK_UPLOAD", "VOUCHER:EXPORT"]}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="outline" className="h-9 px-3">
              <FileUp className="mr-2 h-4 w-4" />
              Bulk Actions
              <ChevronDown className="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-64">
            <PermissionGuard permission="VOUCHER:BULK_UPLOAD">
              <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
                {isDownloadingTemplate ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-500" />
                ) : (
                  <Download className="mr-2 h-4 w-4 text-blue-500" />
                )}
                <span>Download Template</span>
              </DropdownMenuItem>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleBulkUpload}>
                <Upload className="mr-2 h-4 w-4 text-orange-500" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            <PermissionGuard permission="VOUCHER:EXPORT">
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={handleExport} disabled={isExporting}>
                {isExporting ? (
                  <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-500" />
                ) : (
                  <FileSpreadsheet className="mr-2 h-4 w-4 text-green-500" />
                )}
                <span>Export All Data</span>
              </DropdownMenuItem>
            </PermissionGuard>
          </DropdownMenuContent>
        </DropdownMenu>
      </PermissionGuard>

      <PermissionGuard permission="VOUCHER:CREATE">
        <Button onClick={handleAdd} className="h-9">
          <Plus className="mr-2 h-4 w-4" />
          Add Voucher
        </Button>
      </PermissionGuard>
    </div>
  );
}
