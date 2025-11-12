import { Plus, Download, Upload, FileUp, ChevronDown, FileSpreadsheet, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useActivities } from "../context/activities-provider";
import { toast } from "sonner";
import { useState } from "react";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";

export function ActivitiesPrimaryButtons() {
  const { setSelectedActivity, setIsDrawerOpen, setIsEditMode, setIsBulkUploadDialogOpen } =
    useActivities();
  
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  
  const { isExporting, handleExport } = useExport({
    entityName: 'Activity',
    exportEndpoint: '/api/activity/export',
  });

  const handleCreate = () => {
    setSelectedActivity(null);
    setIsEditMode(false);
    setIsDrawerOpen(true);
  };

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile('/api/activity/bulk-upload/template', 'Activity_Upload_Template.xlsx');
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
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
          <DropdownMenuItem onClick={() => setIsBulkUploadDialogOpen(true)}>
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
        Add Activity
      </Button>
    </div>
  );
}
