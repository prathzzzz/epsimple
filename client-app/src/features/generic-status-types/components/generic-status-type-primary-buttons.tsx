import { Download, Plus, Upload, FileSpreadsheet, Loader2, FileUp, ChevronDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useGenericStatusType } from "../context/generic-status-type-provider";
import { toast } from "sonner";
import { useState } from "react";

export function GenericStatusTypePrimaryButtons() {
  const { setSelectedStatusType, setIsDrawerOpen, setIsEditMode, setIsBulkUploadDialogOpen } =
    useGenericStatusType();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const handleCreate = () => {
    setSelectedStatusType(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      const response = await fetch("/api/generic-status-types/download-template", {
        method: "GET",
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Failed to download template");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "generic-status-types-template.xlsx";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template");
      console.error("Download error:", error);
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleExportData = async () => {
    setIsExporting(true);
    try {
      const response = await fetch("/api/generic-status-types/export", {
        method: "GET",
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Failed to export data");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "generic-status-types-export.xlsx";
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      toast.success("Data exported successfully");
    } catch (error) {
      toast.error("Failed to export data");
      console.error("Export error:", error);
    } finally {
      setIsExporting(false);
    }
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
          <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
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
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleExportData} disabled={isExporting}>
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
        Add Status Type
      </Button>
    </div>
  );
}
