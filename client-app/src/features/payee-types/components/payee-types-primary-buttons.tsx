import { Download, Plus, Upload, Loader2, FileUp, ChevronDown, FileSpreadsheet } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { usePayeeTypes } from "../context/payee-types-provider";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";

export function PayeeTypesPrimaryButtons() {
  const {
    setSelectedPayeeType,
    setIsDrawerOpen,
    setIsEditMode,
    setIsBulkUploadDialogOpen,
  } = usePayeeTypes();

  const { handleExport, isExporting } = useExport({
    entityName: "PayeeType",
    exportEndpoint: "/api/payee-types/export",
  });

  const handleCreate = () => {
    setSelectedPayeeType(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    await downloadFile("/api/payee-types/download-template", "PayeeType_Template.xlsx");
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
        Add Payee Type
      </Button>
    </div>
  );
}
