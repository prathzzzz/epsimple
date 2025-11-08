import { Download, FileDown, Plus, Upload } from "lucide-react";
import { Button } from "@/components/ui/button";
import { usePayeeDetails } from "../context/payee-details-provider";
import { downloadFile } from "@/lib/api-utils";
import { useExport } from "@/hooks/useExport";

export function PayeeDetailsPrimaryButtons() {
  const { handleCreate, setIsBulkUploadDialogOpen } = usePayeeDetails();

  const { handleExport, isExporting } = useExport({
    entityName: "PayeeDetails",
    exportEndpoint: "/api/payee-details/export",
  });

  const handleDownloadTemplate = async () => {
    await downloadFile("/api/payee-details/download-template", "PayeeDetails_Template.xlsx");
  };

  const handleBulkUpload = () => {
    setIsBulkUploadDialogOpen(true);
  };

  return (
    <div className="flex items-center gap-2">
      <Button
        variant="outline"
        size="sm"
        className="h-8 border-blue-500 text-blue-600 hover:bg-blue-50 dark:border-blue-600 dark:text-blue-400 dark:hover:bg-blue-950"
        onClick={handleDownloadTemplate}
      >
        <FileDown className="mr-2 h-4 w-4" />
        Download Template
      </Button>
      <Button
        variant="outline"
        size="sm"
        className="h-8 border-green-500 text-green-600 hover:bg-green-50 dark:border-green-600 dark:text-green-400 dark:hover:bg-green-950"
        onClick={handleExport}
        disabled={isExporting}
      >
        <Download className="mr-2 h-4 w-4" />
        {isExporting ? "Exporting..." : "Export"}
      </Button>
      <Button
        variant="outline"
        size="sm"
        className="h-8 border-orange-500 text-orange-600 hover:bg-orange-50 dark:border-orange-600 dark:text-orange-400 dark:hover:bg-orange-950"
        onClick={handleBulkUpload}
      >
        <Upload className="mr-2 h-4 w-4" />
        Bulk Upload
      </Button>
      <Button onClick={handleCreate} size="sm" className="h-8">
        <Plus className="mr-2 h-4 w-4" />
        Add Payee Details
      </Button>
    </div>
  );
}
