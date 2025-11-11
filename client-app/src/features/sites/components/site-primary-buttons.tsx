import { Button } from "@/components/ui/button";
import { Download, FileUp, Upload, Plus, ChevronDown } from "lucide-react";
import { useSite } from "../context/site-provider";
import { toast } from "sonner";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export function SitePrimaryButtons() {
  const { setIsDrawerOpen, setEditingSite, setIsBulkUploadDialogOpen } = useSite();

  const handleDownloadTemplate = async () => {
    try {
      const response = await fetch('/api/sites/bulk-upload/template', {
        method: 'GET',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error('Failed to download template');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'Site_BulkUpload_Template.xlsx';
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
      const response = await fetch('/api/sites/bulk-upload/export', {
        method: 'GET',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error('Failed to export data');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'Sites_Export.xlsx';
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

  const handleCreate = () => {
    setEditingSite(null);
    setIsDrawerOpen(true);
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

      {/* Primary Action */}
      <Button 
        onClick={handleCreate}
        size="sm"
        className="h-9 px-4 text-sm font-medium"
      >
        <Plus className="h-4 w-4 mr-2" />
        Add Site
      </Button>
    </div>
  );
}
