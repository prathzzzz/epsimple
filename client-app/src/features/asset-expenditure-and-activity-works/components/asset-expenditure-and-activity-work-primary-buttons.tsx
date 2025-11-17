import { useState } from "react";
import { Download, FileSpreadsheet, FileUp, Loader2, ChevronDown } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAssetExpenditureAndActivityWork } from "../context/asset-expenditure-and-activity-work-provider";
import { toast } from "sonner";
import { downloadFile } from "@/lib/api-utils";

export function AssetExpenditureAndActivityWorkPrimaryButtons() {
  const { openDrawer, openBulkUploadDialog } = useAssetExpenditureAndActivityWork();
  const [isDownloadingTemplate, setIsDownloadingTemplate] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  const handleDownloadTemplate = async () => {
    setIsDownloadingTemplate(true);
    try {
      await downloadFile(
        '/api/asset-expenditure-and-activity-works/bulk-upload/template',
        'asset_expenditure_and_activity_work_template.xlsx'
      );
      toast.success("Template downloaded successfully");
    } catch (error) {
      toast.error("Failed to download template", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsDownloadingTemplate(false);
    }
  };

  const handleExportData = async () => {
    setIsExporting(true);
    try {
      const timestamp = new Date().toISOString().split("T")[0];
      await downloadFile(
        '/api/asset-expenditure-and-activity-works/bulk-upload/export',
        `asset_expenditure_and_activity_works_export_${timestamp}.xlsx`
      );
      toast.success("Data exported successfully");
    } catch (error) {
      toast.error("Failed to export data", {
        description: error instanceof Error ? error.message : 'An error occurred',
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <div className="flex items-center gap-2">
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="outline" className="h-9 px-3">
            <FileUp className="mr-2 h-4 w-4" />
            Bulk Actions
            <ChevronDown className="ml-2 h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent className="w-64" align="end">
          <DropdownMenuItem onClick={handleDownloadTemplate} disabled={isDownloadingTemplate}>
            {isDownloadingTemplate ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin text-blue-500" />
            ) : (
              <Download className="mr-2 h-4 w-4 text-blue-500" />
            )}
            <span>Download Template</span>
          </DropdownMenuItem>
          <DropdownMenuItem onClick={openBulkUploadDialog}>
            <FileUp className="mr-2 h-4 w-4 text-orange-500" />
            <span>Bulk Upload</span>
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleExportData} disabled={isExporting}>
            {isExporting ? (
              <Loader2 className="mr-2 h-4 w-4 animate-spin text-green-500" />
            ) : (
              <FileSpreadsheet className="mr-2 h-4 w-4 text-green-500" />
            )}
            <span>Export All Data</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <Button onClick={openDrawer} className="h-9">
        Add Asset Expenditure
      </Button>
    </div>
  );
}
