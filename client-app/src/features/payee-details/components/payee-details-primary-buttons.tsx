import { Download, Plus, Upload, Loader2, FileUp, ChevronDown, FileSpreadsheet } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { usePayeeDetails } from "../context/payee-details-provider";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";

export function PayeeDetailsPrimaryButtons() {
  const { handleCreate, setIsBulkUploadDialogOpen } = usePayeeDetails();

  const { handleExport, isExporting } = useExport({
    entityName: "PayeeDetails",
    exportEndpoint: "/api/payee-details/export",
  });

  const handleDownloadTemplate = async () => {
    await downloadFile("/api/payee-details/download-template", "PayeeDetails_Template.xlsx");
  };

  const handleBulkUpload = () => {
    setIsBulkUploadDialogOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="outline" size="sm" className="h-9 px-3">
            <FileUp className="mr-2 h-4 w-4" />
            Bulk Actions
            <ChevronDown className="ml-2 h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start" className="w-64">
          <DropdownMenuItem onClick={handleDownloadTemplate}>
            <Download className="mr-2 h-4 w-4 text-blue-600" />
            <span>Download Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={handleBulkUpload}>
            <Upload className="mr-2 h-4 w-4 text-orange-600" />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleExport} disabled={isExporting}>
            {isExporting ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-600" />
            ) : (
              <FileSpreadsheet className="mr-2 h-4 w-4 text-green-600" />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <Button onClick={handleCreate} size="sm" className="h-9">
        <Plus className="mr-2 h-4 w-4" />
        Add Payee Details
      </Button>
    </div>
  );
}
