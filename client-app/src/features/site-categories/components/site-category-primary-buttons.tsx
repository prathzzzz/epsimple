import { Download, FileUp, Upload, ChevronDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useSiteCategoryContext } from "../context/site-category-provider";
import { toast } from "sonner";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export function SiteCategoryPrimaryButtons() {
  const { setIsBulkUploadDialogOpen } = useSiteCategoryContext();

  const handleDownloadTemplate = async () => {
    try {
      const response = await fetch("/api/site-categories/bulk-upload/template", {
        method: "GET",
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Failed to download template");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "SiteCategory_BulkUpload_Template.xlsx";
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      toast.success("Template downloaded successfully");
    } catch (error) {
      console.error("Error downloading template:", error);
      toast.error("Failed to download template");
    }
  };

  const handleExportData = async () => {
    try {
      const response = await fetch("/api/site-categories/bulk-upload/export", {
        method: "GET",
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Failed to export data");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "SiteCategories_Export.xlsx";
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      toast.success("Data exported successfully");
    } catch (error) {
      console.error("Error exporting data:", error);
      toast.error("Failed to export data");
    }
  };

  return (
    <div className="flex items-center gap-2">
      {/* Bulk Actions Dropdown */}
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant="outline"
            size="sm"
            className="h-9 px-3 text-sm font-medium"
          >
            <FileUp className="h-4 w-4 mr-2" />
            Bulk Actions
            <ChevronDown className="h-4 w-4 ml-2" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start" className="w-64">
          <DropdownMenuItem onClick={handleDownloadTemplate} className="cursor-pointer">
            <Download className="h-4 w-4 mr-2 text-blue-600" />
            <span>Download Template</span>
          </DropdownMenuItem>
          
          <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)} className="cursor-pointer">
            <FileUp className="h-4 w-4 mr-2 text-orange-600" />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          
          <DropdownMenuSeparator />
          
          <DropdownMenuItem onClick={handleExportData} className="cursor-pointer">
            <Upload className="h-4 w-4 mr-2 text-purple-600" />
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
}
