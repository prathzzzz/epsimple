import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Download, FileUp, Plus, ChevronDown, Loader2, FileSpreadsheet } from "lucide-react";
import { useSite } from "../context/site-provider";
import { toast } from "sonner";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";
import { PermissionGuard } from "@/components/permission-guard";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

export function SitePrimaryButtons() {
  const { setIsDrawerOpen, setEditingSite, setIsBulkUploadDialogOpen } = useSite();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  
  const { isExporting, handleExport } = useExport({
    entityName: 'Site',
    exportEndpoint: '/api/sites/export',
  });

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile('/api/sites/bulk-upload/template', 'Site_Upload_Template.xlsx');
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleCreate = () => {
    setEditingSite(null);
    setIsDrawerOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      {/* Bulk Actions Dropdown */}
      <PermissionGuard anyPermissions={["SITE:BULK_UPLOAD", "SITE:EXPORT"]}>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="outline"
              size="sm"
              className="h-9 px-3 text-sm font-medium"
              disabled={isDownloadingTemplate || isExporting}
            >
              <FileUp className="h-4 w-4 mr-2" />
              Bulk Actions
              <ChevronDown className="h-4 w-4 ml-2" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="start" className="w-64">
            <PermissionGuard permission="SITE:BULK_UPLOAD">
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
                <span>Download Template</span>
              </DropdownMenuItem>
              
              <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)} className="cursor-pointer">
                <FileUp className="h-4 w-4 mr-2 text-orange-600" />
                <span>Bulk Upload</span>
              </DropdownMenuItem>
            </PermissionGuard>
            
            <PermissionGuard permission="SITE:EXPORT">
              <DropdownMenuSeparator />
              
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
          </DropdownMenuContent>
        </DropdownMenu>
      </PermissionGuard>

      {/* Primary Action */}
      <PermissionGuard permission="SITE:CREATE">
        <Button 
          onClick={handleCreate}
          size="sm"
          className="h-9 px-4 text-sm font-medium"
        >
          <Plus className="h-4 w-4 mr-2" />
          Add Site
        </Button>
      </PermissionGuard>
    </div>
  );
}
