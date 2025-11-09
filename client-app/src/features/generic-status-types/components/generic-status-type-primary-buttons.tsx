import { Download, Plus, Upload, FileSpreadsheet } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useGenericStatusType } from "../context/generic-status-type-provider";
import { toast } from "sonner";

export function GenericStatusTypePrimaryButtons() {
  const { setSelectedStatusType, setIsDrawerOpen, setIsEditMode, setIsBulkUploadDialogOpen } =
    useGenericStatusType();

  const handleCreate = () => {
    setSelectedStatusType(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    try {
      const response = await fetch("/api/generic-status-types/download-template", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
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
    }
  };

  const handleExportData = async () => {
    try {
      const response = await fetch("/api/generic-status-types/export", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
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
    }
  };

  const handleBulkUpload = () => {
    setIsBulkUploadDialogOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button
        onClick={handleDownloadTemplate}
        variant="outline"
        className="border-blue-500 text-blue-500 hover:bg-blue-50"
      >
        <Download className="mr-2 h-4 w-4" />
        Download Template
      </Button>
      <Button
        onClick={handleExportData}
        variant="outline"
        className="border-green-500 text-green-500 hover:bg-green-50"
      >
        <FileSpreadsheet className="mr-2 h-4 w-4" />
        Export Data
      </Button>
      <Button
        onClick={handleBulkUpload}
        variant="outline"
        className="border-orange-500 text-orange-500 hover:bg-orange-50"
      >
        <Upload className="mr-2 h-4 w-4" />
        Bulk Upload
      </Button>
      <Button onClick={handleCreate}>
        <Plus className="mr-2 h-4 w-4" />
        Add Status Type
      </Button>
    </div>
  );
}
