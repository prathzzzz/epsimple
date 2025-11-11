import { Button } from "@/components/ui/button";
import { Download, FileUp, Upload, Plus, MapPin, ChevronDown } from "lucide-react";
import { useAssetContext } from "../context/asset-provider";
import { toast } from "sonner";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export function AssetPrimaryButtons() {
  const { 
    setIsDrawerOpen, 
    setEditingAsset, 
    setIsBulkUploadDialogOpen,
    setIsPlacementBulkUploadDialogOpen 
  } = useAssetContext();

  const handleDownloadTemplate = async () => {
    try {
      const response = await fetch('/api/assets/bulk/export-template', {
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
      a.download = 'Asset_BulkUpload_Template.xlsx';
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

  const handleDownloadPlacementTemplate = async () => {
    try {
      const response = await fetch('/api/asset-location/export-template', {
        method: 'GET',
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error('Failed to download placement template');
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'Asset_Placement_Template.xlsx';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);

      toast.success("Placement template downloaded successfully");
    } catch (error) {
      console.error("Error downloading placement template:", error);
      toast.error("Failed to download placement template");
    }
  };

  const handleExportData = async () => {
    try {
      const response = await fetch('/api/assets/bulk/export-data', {
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
      a.download = 'Assets_Export.xlsx';
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
    setEditingAsset(null);
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
          <DropdownMenuLabel className="text-xs font-semibold text-muted-foreground">
            Asset Creation
          </DropdownMenuLabel>
          <DropdownMenuItem onClick={handleDownloadTemplate} className="cursor-pointer">
            <Download className="h-4 w-4 mr-2 text-blue-600" />
            <span>Download Asset Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)} className="cursor-pointer">
            <FileUp className="h-4 w-4 mr-2 text-blue-600" />
            <span>Bulk Upload Assets</span>
          </DropdownMenuItem>
          
          <DropdownMenuSeparator />
          
          <DropdownMenuLabel className="text-xs font-semibold text-muted-foreground">
            Asset Placement
          </DropdownMenuLabel>
          <DropdownMenuItem onClick={handleDownloadPlacementTemplate} className="cursor-pointer">
            <Download className="h-4 w-4 mr-2 text-teal-600" />
            <span>Download Placement Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setIsPlacementBulkUploadDialogOpen(true)} className="cursor-pointer">
            <MapPin className="h-4 w-4 mr-2 text-teal-600" />
            <span>Bulk Place Assets</span>
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
        Add Asset
      </Button>
    </div>
  );
}
