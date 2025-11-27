import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Download, FileUp, Plus, MapPin, ChevronDown, Loader2, FileSpreadsheet, Calculator } from "lucide-react";
import { useAssetContext } from "../context/asset-provider";
import { toast } from "sonner";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";
import { PermissionGuard } from "@/components/permission-guard";
import { AssetFinancialExportDialog } from "@/features/asset-financials";
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

  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isDownloadingPlacementTemplate, setIsDownloadingPlacementTemplate] = useState(false);
  const [isFinancialExportDialogOpen, setIsFinancialExportDialogOpen] = useState(false);
  
  const { isExporting, handleExport } = useExport({
    entityName: 'Asset',
    exportEndpoint: '/api/assets/export',
  });

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile('/api/assets/bulk/export-template', 'Asset_Upload_Template.xlsx');
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleDownloadPlacementTemplate = async () => {
    setIsDownloadingPlacementTemplate(true);
    try {
      await downloadFile('/api/asset-location/export-template', 'Asset_Placement_Template.xlsx');
      toast.success("Placement template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download placement template", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingPlacementTemplate(false);
    }
  };

  const handleCreate = () => {
    setEditingAsset(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      {/* Bulk Actions Dropdown */}
      <PermissionGuard anyPermissions={["ASSET:BULK_UPLOAD", "ASSET:EXPORT", "ASSET:FINANCIAL_EXPORT"]}>
        <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            variant="outline"
            size="sm"
            className="h-9 px-3 text-sm font-medium"
            disabled={isDownloadingTemplate || isDownloadingPlacementTemplate || isExporting}
          >
            <FileUp className="h-4 w-4 mr-2" />
            Bulk Actions
            <ChevronDown className="h-4 w-4 ml-2" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="start" className="w-80">
          <DropdownMenuLabel className="text-xs font-semibold text-muted-foreground">
            Asset Creation
          </DropdownMenuLabel>
          <PermissionGuard permission="ASSET:BULK_UPLOAD">
          <DropdownMenuItem 
            onClick={handleDownloadTemplate} 
            className="cursor-pointer"
            disabled={isDownloadingTemplate}
          >
            {isDownloadingTemplate ? (
              <Loader2 className="h-4 w-4 mr-2 animate-spin text-blue-600" />
            ) : (
              <Download className="h-4 w-4 mr-2 text-blue-600" />
            )}
            <span>Download Asset Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)} className="cursor-pointer">
            <FileUp className="h-4 w-4 mr-2 text-blue-600" />
            <span>Bulk Upload Assets</span>
          </DropdownMenuItem>
          </PermissionGuard>
          
          <DropdownMenuSeparator />
          
          <DropdownMenuLabel className="text-xs font-semibold text-muted-foreground">
            Asset Placement
          </DropdownMenuLabel>
          <PermissionGuard permission="ASSET:PLACE">
          <DropdownMenuItem 
            onClick={handleDownloadPlacementTemplate} 
            className="cursor-pointer"
            disabled={isDownloadingPlacementTemplate}
          >
            {isDownloadingPlacementTemplate ? (
              <Loader2 className="h-4 w-4 mr-2 animate-spin text-teal-600" />
            ) : (
              <Download className="h-4 w-4 mr-2 text-teal-600" />
            )}
            <span>Download Placement Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setIsPlacementBulkUploadDialogOpen(true)} className="cursor-pointer">
            <MapPin className="h-4 w-4 mr-2 text-teal-600" />
            <span>Bulk Place Assets</span>
          </DropdownMenuItem>
          </PermissionGuard>
          
          <DropdownMenuSeparator />
          
          <PermissionGuard permission="ASSET:EXPORT">
          
          <DropdownMenuItem 
            onClick={handleExport} 
            className="cursor-pointer"
            disabled={isExporting}
          >
            {isExporting ? (
              <Loader2 className="h-4 w-4 mr-2 animate-spin text-purple-600" />
            ) : (
              <FileSpreadsheet className="h-4 w-4 mr-2 text-purple-600" />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
          </PermissionGuard>
          <PermissionGuard permission="ASSET:FINANCIAL_EXPORT">
          <DropdownMenuItem 
            onClick={() => setIsFinancialExportDialogOpen(true)} 
            className="cursor-pointer"
          >
            <Calculator className="h-4 w-4 mr-2 text-green-600" />
            <span>Export Financial Report</span>
          </DropdownMenuItem>
          </PermissionGuard>
        </DropdownMenuContent>
      </DropdownMenu>
      </PermissionGuard>

      {/* Primary Action */}
      <PermissionGuard permission="ASSET:CREATE">
      <Button 
        onClick={handleCreate}
        size="sm"
        className="h-9 px-4 text-sm font-medium"
      >
        <Plus className="h-4 w-4 mr-2" />
        Add Asset
      </Button>
      </PermissionGuard>

      {/* Financial Export Dialog */}
      <AssetFinancialExportDialog 
        open={isFinancialExportDialogOpen} 
        onOpenChange={setIsFinancialExportDialogOpen} 
      />
    </div>
  );
}
