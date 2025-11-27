import { useState } from 'react';
import { format } from 'date-fns';
import { Download, Calendar, Building2, Tag, Loader2, CalendarRange } from 'lucide-react';
import { toast } from 'sonner';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Calendar as CalendarComponent } from '@/components/ui/calendar';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { cn } from '@/lib/utils';
import { useQuery } from '@tanstack/react-query';
import { managedProjectApi } from '@/features/managed-projects/api/managed-project-api';
import { assetCategoryApi } from '@/features/asset-categories/api/asset-categories-api';
import { downloadFileWithPost } from '@/lib/api-utils';

interface AssetFinancialExportDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
}

export function AssetFinancialExportDialog({
  open,
  onOpenChange,
}: AssetFinancialExportDialogProps) {
  const [managedProjectId, setManagedProjectId] = useState<string>('all');
  const [assetCategoryId, setAssetCategoryId] = useState<string>('all');
  
  // WDV calculation: Tech Live Date -> wdvToDate (required)
  const [wdvToDate, setWdvToDate] = useState<Date | undefined>(new Date());
  
  // Custom depreciation: customFromDate -> customToDate (optional)
  const [customFromDate, setCustomFromDate] = useState<Date | undefined>(undefined);
  const [customToDate, setCustomToDate] = useState<Date | undefined>(undefined);
  
  const [isExporting, setIsExporting] = useState(false);

  // Fetch managed projects
  const { data: managedProjects = [], isLoading: isLoadingProjects } = useQuery({
    queryKey: ['managed-projects', 'list'],
    queryFn: () => managedProjectApi.getList(),
    enabled: open,
  });

  // Fetch asset categories
  const { data: assetCategories = [], isLoading: isLoadingCategories } = useQuery({
    queryKey: ['asset-categories', 'list'],
    queryFn: () => assetCategoryApi.getList(),
    enabled: open,
  });

  const handleExport = async () => {
    if (!wdvToDate) {
      toast.error('Please select a WDV calculation date');
      return;
    }

    // If custom range is partially filled, show error
    if ((customFromDate && !customToDate) || (!customFromDate && customToDate)) {
      toast.error('Please select both From and To dates for custom depreciation, or leave both empty');
      return;
    }

    setIsExporting(true);

    try {
      const data = {
        managedProjectId: managedProjectId !== 'all' ? Number(managedProjectId) : null,
        assetCategoryId: assetCategoryId !== 'all' ? Number(assetCategoryId) : null,
        wdvToDate: format(wdvToDate, 'yyyy-MM-dd'),
        customFromDate: customFromDate ? format(customFromDate, 'yyyy-MM-dd') : null,
        customToDate: customToDate ? format(customToDate, 'yyyy-MM-dd') : null,
      };

      const filename = `asset_financial_report_${format(new Date(), 'yyyyMMdd')}.xlsx`;
      await downloadFileWithPost('/api/assets/financial-export', data, filename);
      
      toast.success('Financial report exported successfully');
      onOpenChange(false);
    } catch {
      toast.error('Failed to export financial report');
    } finally {
      setIsExporting(false);
    }
  };

  const handleReset = () => {
    setManagedProjectId('all');
    setAssetCategoryId('all');
    setWdvToDate(new Date());
    setCustomFromDate(undefined);
    setCustomToDate(undefined);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-md sm:max-w-lg max-h-[90vh] overflow-y-auto" aria-describedby="financial-export-description">
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2 text-base">
            <Download className="h-4 w-4 text-primary" />
            Export Financial Report
          </DialogTitle>
          <DialogDescription id="financial-export-description" className="text-sm">
            Export with WDV and optional custom depreciation.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-3 py-1">
          {/* Managed Project Filter */}
          <div className="space-y-1">
            <Label className="flex items-center gap-1.5 text-sm">
              <Building2 className="h-3.5 w-3.5 text-muted-foreground" />
              Managed Project
              <span className="text-xs text-muted-foreground">(Optional)</span>
            </Label>
            <Select
              value={managedProjectId}
              onValueChange={setManagedProjectId}
            >
              <SelectTrigger className="h-8">
                <SelectValue placeholder={isLoadingProjects ? 'Loading...' : 'All Projects'} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Projects</SelectItem>
                {managedProjects.map((project) => (
                  <SelectItem key={project.id} value={project.id.toString()}>
                    {project.projectName}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* Asset Category Filter */}
          <div className="space-y-1">
            <Label className="flex items-center gap-1.5 text-sm">
              <Tag className="h-3.5 w-3.5 text-muted-foreground" />
              Asset Category
              <span className="text-xs text-muted-foreground">(Optional)</span>
            </Label>
            <Select
              value={assetCategoryId}
              onValueChange={setAssetCategoryId}
            >
              <SelectTrigger className="h-8">
                <SelectValue placeholder={isLoadingCategories ? 'Loading...' : 'All Categories'} />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Categories</SelectItem>
                {assetCategories.map((category) => (
                  <SelectItem key={category.id} value={category.id.toString()}>
                    {category.categoryName}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* WDV Calculation Date */}
          <div className="space-y-1 rounded-md border p-2">
            <Label className="flex items-center gap-1.5 text-sm font-medium">
              <Calendar className="h-3.5 w-3.5 text-muted-foreground" />
              WDV Calculation <span className="text-red-500">*</span>
            </Label>
            <p className="text-xs text-muted-foreground">
              From Tech Live Date to selected date.
            </p>
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  size="sm"
                  className={cn(
                    'h-8 w-full justify-start text-left font-normal',
                    !wdvToDate && 'text-muted-foreground'
                  )}
                >
                  <Calendar className="mr-2 h-3.5 w-3.5" />
                  {wdvToDate ? format(wdvToDate, 'dd-MM-yyyy') : 'Select date'}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0" align="start">
                <CalendarComponent
                  mode="single"
                  selected={wdvToDate}
                  onSelect={setWdvToDate}
                  initialFocus
                />
              </PopoverContent>
            </Popover>
          </div>

          {/* Custom Depreciation Date Range */}
          <div className="space-y-1 rounded-md border p-2">
            <Label className="flex items-center gap-1.5 text-sm font-medium">
              <CalendarRange className="h-3.5 w-3.5 text-muted-foreground" />
              Custom Depreciation
              <span className="text-xs text-muted-foreground font-normal">(Optional)</span>
            </Label>
            <p className="text-xs text-muted-foreground">
              Custom date range (separate from WDV).
            </p>
            <div className="grid grid-cols-2 gap-2">
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant="outline"
                    size="sm"
                    className={cn(
                      'h-8 w-full justify-start text-left font-normal',
                      !customFromDate && 'text-muted-foreground'
                    )}
                  >
                    <Calendar className="mr-2 h-3.5 w-3.5" />
                    {customFromDate ? format(customFromDate, 'dd-MM-yyyy') : 'From'}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <CalendarComponent
                    mode="single"
                    selected={customFromDate}
                    onSelect={setCustomFromDate}
                    disabled={(date) => customToDate ? date > customToDate : false}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant="outline"
                    size="sm"
                    className={cn(
                      'h-8 w-full justify-start text-left font-normal',
                      !customToDate && 'text-muted-foreground'
                    )}
                  >
                    <Calendar className="mr-2 h-3.5 w-3.5" />
                    {customToDate ? format(customToDate, 'dd-MM-yyyy') : 'To'}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0" align="start">
                  <CalendarComponent
                    mode="single"
                    selected={customToDate}
                    onSelect={setCustomToDate}
                    disabled={(date) => customFromDate ? date < customFromDate : false}
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
            </div>
          </div>

          {/* Info text */}
          <p className="text-xs text-muted-foreground">
            Includes WDV, custom depreciation, and loss value for scraped assets.
          </p>
        </div>

        <DialogFooter className="gap-2 sm:gap-2">
          <Button variant="outline" size="sm" onClick={handleReset}>
            Reset
          </Button>
          <Button size="sm" onClick={handleExport} disabled={isExporting || !wdvToDate}>
            {isExporting ? (
              <>
                <Loader2 className="mr-2 h-3.5 w-3.5 animate-spin" />
                Exporting...
              </>
            ) : (
              <>
                <Download className="mr-2 h-3.5 w-3.5" />
                Export
              </>
            )}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
